package com.minimarket.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales requeridas para iniciar sesion y obtener un token JWT")
public class LoginRequest {
    @Schema(description = "Nombre de usuario registrado", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @Schema(description = "Contrasena del usuario registrado", example = "Admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contrasena es obligatoria")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
