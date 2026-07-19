package com.minimarket.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos requeridos para registrar un nuevo cliente")
public class RegisterRequest {
    @Schema(description = "Nombre de usuario nuevo", example = "cliente_nuevo", minLength = 3, maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @Schema(description = "Contrasena del nuevo usuario", example = "Cliente123", minLength = 6, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, max = 100, message = "La contrasena debe tener entre 6 y 100 caracteres")
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
