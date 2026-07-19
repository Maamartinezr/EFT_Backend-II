package com.minimarket.security.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta de autenticacion generada luego de login o registro exitoso")
public class AuthResponse {
    @Schema(description = "Token JWT que debe enviarse como Bearer token en endpoints protegidos", example = "eyJhbGciOiJIUzI1NiJ9...")
    private final String token;
    @Schema(description = "Tipo de token utilizado por el esquema de autenticacion", example = "Bearer")
    private final String tipo;
    @Schema(description = "Nombre del usuario autenticado", example = "admin")
    private final String username;
    @Schema(description = "Roles asociados al usuario autenticado", example = "[\"ROLE_ADMIN\"]")
    private final List<String> roles;

    public AuthResponse(String token, String username, List<String> roles) {
        this.token = token;
        this.tipo = "Bearer";
        this.username = username;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
