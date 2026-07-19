package com.minimarket.controller;

import com.minimarket.dto.CarritoDTO;
import com.minimarket.entity.Carrito;
import com.minimarket.hateoas.CarritoModelAssembler;
import com.minimarket.service.CarritoService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Gestion del carrito de compras y productos asociados a usuarios")
@SecurityRequirement(name = "bearerAuth")
public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoModelAssembler carritoModelAssembler;

    public CarritoController(CarritoService carritoService, CarritoModelAssembler carritoModelAssembler) {
        this.carritoService = carritoService;
        this.carritoModelAssembler = carritoModelAssembler;
    }

    @GetMapping
    @Operation(
            summary = "Lista carritos con enlaces HATEOAS",
            description = "Retorna registros de carrito como CollectionModel<EntityModel<CarritoDTO>>, incluyendo links self, collection, usuario y producto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carritos consultados correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public CollectionModel<EntityModel<CarritoDTO>> listarCarrito(
            @Parameter(description = "Numero de pagina a consultar, comenzando desde 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de registros por pagina", example = "3")
            @RequestParam(defaultValue = "10") int size) {
        List<EntityModel<CarritoDTO>> modelos = paginar(carritoService.findAll(), page, size).stream()
                .map(carritoModelAssembler::toModel)
                .toList();

        return CollectionModel.of(modelos)
                .add(linkTo(methodOn(CarritoController.class).listarCarrito(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un carrito por id con enlaces HATEOAS",
            description = "Consulta un registro individual del carrito e incorpora enlaces hacia el recurso, la coleccion, el usuario y el producto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<CarritoDTO>> obtenerCarritoPorId(
            @Parameter(description = "Identificador unico del registro de carrito", example = "1", required = true)
            @PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return (carrito != null) ? ResponseEntity.ok(carritoModelAssembler.toModel(carrito)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
            summary = "Agrega un producto al carrito",
            description = "Registra un producto asociado a un usuario dentro del carrito de compras."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto agregado al carrito correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public EntityModel<CarritoDTO> agregarProductoAlCarrito(@RequestBody Carrito carrito) {
        return carritoModelAssembler.toModel(carritoService.save(carrito));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un carrito",
            description = "Modifica la cantidad, usuario o producto asociado a un registro de carrito existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<CarritoDTO>> actualizarCarrito(
            @Parameter(description = "Identificador unico del registro de carrito", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Carrito carrito) {
        Carrito existente = carritoService.findById(id);
        if (existente != null) {
            carrito.setId(id);
            return ResponseEntity.ok(carritoModelAssembler.toModel(carritoService.save(carrito)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un producto del carrito",
            description = "Elimina un registro del carrito cuando existe en la base de datos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado del carrito correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @Parameter(description = "Identificador unico del registro de carrito", example = "1", required = true)
            @PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito != null) {
            carritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private List<Carrito> paginar(List<Carrito> carritos, int page, int size) {
        int pagina = Math.max(page, 0);
        int tamanio = Math.max(1, Math.min(size, 100));
        int desde = pagina * tamanio;
        if (desde >= carritos.size()) {
            return List.of();
        }
        int hasta = Math.min(desde + tamanio, carritos.size());
        return carritos.subList(desde, hasta);
    }
}
