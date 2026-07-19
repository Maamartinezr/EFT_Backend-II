package com.minimarket.controller;

import com.minimarket.dto.InventarioDTO;
import com.minimarket.entity.Inventario;
import com.minimarket.hateoas.InventarioModelAssembler;
import com.minimarket.service.InventarioService;
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
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Gestion de movimientos de inventario, entradas, salidas y trazabilidad de stock")
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    private final InventarioService inventarioService;
    private final InventarioModelAssembler inventarioModelAssembler;

    public InventarioController(InventarioService inventarioService, InventarioModelAssembler inventarioModelAssembler) {
        this.inventarioService = inventarioService;
        this.inventarioModelAssembler = inventarioModelAssembler;
    }

    @GetMapping
    @Operation(
            summary = "Lista movimientos de inventario con enlaces HATEOAS",
            description = "Retorna movimientos de inventario como CollectionModel<EntityModel<InventarioDTO>>, incluyendo links self, collection y producto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos consultados correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public CollectionModel<EntityModel<InventarioDTO>> listarMovimientosDeInventario(
            @Parameter(description = "Numero de pagina a consultar, comenzando desde 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de movimientos por pagina", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        List<EntityModel<InventarioDTO>> modelos = paginar(inventarioService.findAll(), page, size).stream()
                .map(inventarioModelAssembler::toModel)
                .toList();

        return CollectionModel.of(modelos)
                .add(linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario(page, size)).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un movimiento de inventario por id con enlaces HATEOAS",
            description = "Consulta un movimiento especifico de inventario y entrega enlaces hacia el recurso actual, la coleccion y el producto asociado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<InventarioDTO>> obtenerMovimientoPorId(
            @Parameter(description = "Identificador unico del movimiento de inventario", example = "1", required = true)
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        return (inventario != null) ? ResponseEntity.ok(inventarioModelAssembler.toModel(inventario)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
            summary = "Registra un movimiento de inventario",
            description = "Crea una entrada o salida de inventario asociada a un producto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public EntityModel<InventarioDTO> registrarMovimiento(@RequestBody Inventario inventario) {
        return inventarioModelAssembler.toModel(inventarioService.save(inventario));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza un movimiento de inventario",
            description = "Modifica los datos de un movimiento de inventario existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<InventarioDTO>> actualizarMovimiento(
            @Parameter(description = "Identificador unico del movimiento de inventario", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Inventario inventario) {
        Inventario existente = inventarioService.findById(id);
        if (existente != null) {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioModelAssembler.toModel(inventarioService.save(inventario)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un movimiento de inventario",
            description = "Elimina un movimiento registrado cuando existe en la base de datos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<Void> eliminarMovimiento(
            @Parameter(description = "Identificador unico del movimiento de inventario", example = "1", required = true)
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private List<Inventario> paginar(List<Inventario> movimientos, int page, int size) {
        int pagina = Math.max(page, 0);
        int tamanio = Math.max(1, Math.min(size, 100));
        int desde = pagina * tamanio;
        if (desde >= movimientos.size()) {
            return List.of();
        }
        int hasta = Math.min(desde + tamanio, movimientos.size());
        return movimientos.subList(desde, hasta);
    }
}
