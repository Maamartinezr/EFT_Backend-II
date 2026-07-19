package com.minimarket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiDocumentationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void openApiJsonEstaDisponibleYDocumentaSeguridadBearer() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").value("MiniMarket Plus API"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth").exists())
                .andExpect(jsonPath("$.paths['/api/productos']").exists())
                .andExpect(jsonPath("$.paths['/api/auth/login']").exists());
    }

    @Test
    void openApiDocumentaTagsParametrosRespuestasYSchemasPrincipales() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/productos'].get.tags[0]").value("Producto"))
                .andExpect(jsonPath("$.paths['/api/carrito'].get.tags[0]").value("Carrito"))
                .andExpect(jsonPath("$.paths['/api/inventario'].get.tags[0]").value("Inventario"))
                .andExpect(jsonPath("$.paths['/api/usuarios'].get.tags[0]").value("Usuario"))
                .andExpect(jsonPath("$.paths['/api/productos'].get.parameters[0].name").value("page"))
                .andExpect(jsonPath("$.paths['/api/productos'].get.parameters[1].name").value("size"))
                .andExpect(jsonPath("$.paths['/api/productos/{id}'].get.parameters[0].name").value("id"))
                .andExpect(jsonPath("$.paths['/api/inventario'].get.responses['200'].description").value("Movimientos consultados correctamente"))
                .andExpect(jsonPath("$.paths['/api/inventario'].get.responses['401']").exists())
                .andExpect(jsonPath("$.paths['/api/inventario'].get.responses['403']").exists())
                .andExpect(jsonPath("$.paths['/api/inventario/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.components.schemas.EntityModelProductoDTO.properties.nombre.example").value("Arroz grano largo 1kg"))
                .andExpect(jsonPath("$.components.schemas.EntityModelProductoDTO.properties._links").exists())
                .andExpect(jsonPath("$.components.schemas.EntityModelCarritoDTO.properties.cantidad.example").value(2))
                .andExpect(jsonPath("$.components.schemas.EntityModelInventarioDTO.properties.tipoMovimiento.example").value("Entrada"))
                .andExpect(jsonPath("$.components.schemas.EntityModelUsuarioDTO.properties.username.example").value("admin"))
                .andExpect(jsonPath("$.components.schemas.LoginRequest.properties.username.example").value("admin"))
                .andExpect(jsonPath("$.components.schemas.LoginRequest.properties.password.example").value("Admin123"))
                .andExpect(jsonPath("$.components.schemas.AuthResponse.properties.tipo.example").value("Bearer"));
    }

    @Test
    void swaggerUiEstaDisponible() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> {
                    String location = result.getResponse().getHeader("Location");
                    org.assertj.core.api.Assertions.assertThat(location).contains("/swagger-ui/index.html");
                });
    }
}
