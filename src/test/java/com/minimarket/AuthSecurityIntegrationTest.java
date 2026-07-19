package com.minimarket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginConUsuarioInicialRetornaJwt() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"Admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        assertThat(json.get("token").asText()).isNotBlank();
    }

    @Test
    void productosDisponiblesEsPublicoYEntregaLinksHateoas() throws Exception {
        mockMvc.perform(get("/api/productos/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.collection.href").exists())
                .andExpect(jsonPath("$._embedded.productos").exists())
                .andExpect(jsonPath("$._embedded.productos[0]._links.self.href").exists());
    }

    @Test
    void productoIndividualEntregaDtoYLinksHateoas() throws Exception {
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.categoriaId").exists())
                .andExpect(jsonPath("$.categoriaNombre").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.collection.href").exists())
                .andExpect(jsonPath("$._links.carrito.href").exists());
    }

    @Test
    void productosPaginadosEntreganCollectionModel() throws Exception {
        mockMvc.perform(get("/api/productos?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productos.length()").value(2))
                .andExpect(jsonPath("$._embedded.productos[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.stock-bajo.href").exists());
    }

    @Test
    void stockBajoRequiereJwtValido() throws Exception {
        mockMvc.perform(get("/api/productos/stock-bajo?minimo=100"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPuedeConsultarStockBajo() throws Exception {
        String token = obtenerTokenAdmin();

        mockMvc.perform(get("/api/productos/stock-bajo?minimo=100")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.collection.href").exists())
                .andExpect(jsonPath("$._embedded.productos[0]._links.self.href").exists());
    }

    private String obtenerTokenAdmin() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"Admin123\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }
}
