package com.minimarket.dto;

import com.minimarket.entity.Carrito;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

@Schema(description = "DTO de salida para productos agregados al carrito")
@Relation(itemRelation = "carrito", collectionRelation = "carritos")
public record CarritoDTO(
        @Schema(description = "Identificador unico del registro de carrito", example = "1")
        Long id,
        @Schema(description = "Identificador del usuario asociado al carrito", example = "4")
        Long usuarioId,
        @Schema(description = "Nombre de usuario asociado al carrito", example = "cliente")
        String username,
        @Schema(description = "Identificador del producto agregado", example = "1")
        Long productoId,
        @Schema(description = "Nombre del producto agregado", example = "Arroz grano largo 1kg")
        String productoNombre,
        @Schema(description = "Cantidad del producto dentro del carrito", example = "2")
        Integer cantidad
) {
    public static CarritoDTO fromEntity(Carrito carrito) {
        return new CarritoDTO(
                carrito.getId(),
                carrito.getUsuario().getId(),
                carrito.getUsuario().getUsername(),
                carrito.getProducto().getId(),
                carrito.getProducto().getNombre(),
                carrito.getCantidad()
        );
    }
}
