package com.minimarket.dto;

import com.minimarket.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Schema(description = "DTO de salida para usuarios del sistema sin exponer contrasena")
@Relation(itemRelation = "usuario", collectionRelation = "usuarios")
public record UsuarioDTO(
        @Schema(description = "Identificador unico del usuario", example = "1")
        Long id,
        @Schema(description = "Nombre de usuario utilizado para iniciar sesion", example = "admin")
        String username,
        @Schema(description = "Roles asociados al usuario", example = "[\"ROLE_ADMIN\"]")
        List<String> roles
) {
    public static UsuarioDTO fromEntity(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRoles().stream()
                        .map(rol -> rol.getNombre())
                        .sorted()
                        .toList()
        );
    }
}
