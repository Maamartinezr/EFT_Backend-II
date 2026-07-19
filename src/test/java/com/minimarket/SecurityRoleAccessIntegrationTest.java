package com.minimarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityRoleAccessIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void clienteNoPuedeConsultarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", bearerToken("cliente", "Cliente123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPuedeConsultarUsuariosSinExponerPasswords() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", bearerToken("admin", "Admin123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarios[0].username").exists())
                .andExpect(jsonPath("$._embedded.usuarios[0].password").doesNotExist())
                .andExpect(jsonPath("$._embedded.usuarios[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void jefeTurnoPuedeConsultarInventario() throws Exception {
        mockMvc.perform(get("/api/inventario")
                        .header("Authorization", bearerToken("jefe", "Jefe123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.inventarios[0].tipoMovimiento").exists())
                .andExpect(jsonPath("$._embedded.inventarios[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.inventarios[0]._links.producto.href").exists());
    }

    @Test
    void clienteNoPuedeConsultarInventario() throws Exception {
        mockMvc.perform(get("/api/inventario")
                        .header("Authorization", bearerToken("cliente", "Cliente123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void clientePuedeConsultarCarrito() throws Exception {
        mockMvc.perform(get("/api/carrito")
                        .header("Authorization", bearerToken("cliente", "Cliente123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.carritos[0].cantidad").exists())
                .andExpect(jsonPath("$._embedded.carritos[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.carritos[0]._links.producto.href").exists());
    }

    @Test
    void clientePuedeConsultarCarritoPaginadoConLinks() throws Exception {
        mockMvc.perform(get("/api/carrito?page=0&size=3")
                        .header("Authorization", bearerToken("cliente", "Cliente123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.carritos.length()").value(3))
                .andExpect(jsonPath("$._embedded.carritos[0]._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.carritos[0]._links.collection.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void cajeroNoPuedeConsultarCarrito() throws Exception {
        mockMvc.perform(get("/api/carrito")
                        .header("Authorization", bearerToken("cajero", "Cajero123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void cajeroPuedeConsultarVentas() throws Exception {
        mockMvc.perform(get("/api/ventas")
                        .header("Authorization", bearerToken("cajero", "Cajero123")))
                .andExpect(status().isOk());
    }

    @Test
    void registroDuplicadoDevuelveConflict() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"cliente\",\"password\":\"Cliente123\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void registroConPasswordCortaDevuelveBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"nuevo_cliente\",\"password\":\"123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginSinUsernameDevuelveBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"Admin123\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tokenInvalidoNoPermiteAccederAEndpointProtegido() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isForbidden());
    }

    private String bearerToken(String username, String password) throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return "Bearer " + objectMapper.readTree(response).get("token").asText();
    }
}
