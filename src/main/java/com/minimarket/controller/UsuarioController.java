package com.minimarket.controller;

import com.minimarket.dto.UsuarioDTO;
import com.minimarket.entity.Usuario;
import com.minimarket.hateoas.UsuarioModelAssembler;
import com.minimarket.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuario", description = "Gestion de usuarios, roles y datos de acceso sin exponer contrasenas")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler usuarioModelAssembler;

    public UsuarioController(UsuarioService usuarioService, UsuarioModelAssembler usuarioModelAssembler) {
        this.usuarioService = usuarioService;
        this.usuarioModelAssembler = usuarioModelAssembler;
    }

    @GetMapping
    @Operation(
            summary = "Lista usuarios con enlaces HATEOAS sin exponer passwords",
            description = "Retorna usuarios como CollectionModel<EntityModel<UsuarioDTO>>, incluyendo roles y enlaces hipermedia sin exponer contrasenas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios consultados correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public CollectionModel<EntityModel<UsuarioDTO>> listarUsuarios(
            @Parameter(description = "Numero de pagina a consultar, comenzando desde 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de usuarios por pagina", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        List<EntityModel<UsuarioDTO>> modelos = paginar(usuarioService.findAll(), page, size).stream()
                .map(usuarioModelAssembler::toModel)
                .toList();

        return CollectionModel.of(modelos)
                .add(linkTo(methodOn(UsuarioController.class).listarUsuarios(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un usuario por id con enlaces HATEOAS sin exponer password",
            description = "Consulta un usuario especifico como UsuarioDTO, agregando enlaces self, collection y carrito."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<UsuarioDTO>> obtenerUsuarioPorId(
            @Parameter(description = "Identificador unico del usuario", example = "1", required = true)
            @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(value -> ResponseEntity.ok(usuarioModelAssembler.toModel(value))) // Si el usuario existe, devuelve 200 OK con el usuario
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no, devuelve 404
    }

    @PostMapping
    @Operation(
            summary = "Crea un usuario",
            description = "Registra un usuario con sus roles asociados. Operacion reservada para administradores."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public EntityModel<UsuarioDTO> guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioModelAssembler.toModel(usuarioService.save(usuario));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un usuario",
            description = "Modifica los datos de un usuario existente, manteniendo la respuesta mediante UsuarioDTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<UsuarioDTO>> actualizarUsuario(
            @Parameter(description = "Identificador unico del usuario", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioModelAssembler.toModel(usuarioService.save(usuario)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un usuario",
            description = "Elimina un usuario existente cuando se encuentra registrado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "Identificador unico del usuario", example = "1", required = true)
            @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) { // Verifica si el usuario existe
            usuarioService.deleteById(id); // Elimina al usuario
            return ResponseEntity.noContent().build(); // Respuesta 204 (sin contenido)
        }
        return ResponseEntity.notFound().build(); // Respuesta 404 (no encontrado)
    }

    private List<Usuario> paginar(List<Usuario> usuarios, int page, int size) {
        int pagina = Math.max(page, 0);
        int tamanio = Math.max(1, Math.min(size, 100));
        int desde = pagina * tamanio;
        if (desde >= usuarios.size()) {
            return List.of();
        }
        int hasta = Math.min(desde + tamanio, usuarios.size());
        return usuarios.subList(desde, hasta);
    }
}
