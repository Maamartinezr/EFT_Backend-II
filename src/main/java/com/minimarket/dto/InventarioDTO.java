package com.minimarket.dto;

import com.minimarket.entity.Inventario;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

@Schema(description = "DTO de salida para movimientos de inventario")
@Relation(itemRelation = "inventario", collectionRelation = "inventarios")
public record InventarioDTO(
        @Schema(description = "Identificador unico del movimiento de inventario", example = "1")
        Long id,
        @Schema(description = "Identificador del producto afectado por el movimiento", example = "1")
        Long productoId,
        @Schema(description = "Nombre del producto afectado por el movimiento", example = "Arroz grano largo 1kg")
        String productoNombre,
        @Schema(description = "Cantidad ingresada o descontada del inventario", example = "50")
        Integer cantidad,
        @Schema(description = "Tipo de movimiento realizado", example = "Entrada")
        String tipoMovimiento,
        @Schema(description = "Fecha y hora del movimiento de inventario", example = "2026-07-18T19:00:00.000+00:00")
        Date fechaMovimiento
) {
    public static InventarioDTO fromEntity(Inventario inventario) {
        return new InventarioDTO(
                inventario.getId(),
                inventario.getProducto().getId(),
                inventario.getProducto().getNombre(),
                inventario.getCantidad(),
                inventario.getTipoMovimiento(),
                inventario.getFechaMovimiento()
        );
    }
}
