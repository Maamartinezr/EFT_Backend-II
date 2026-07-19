package com.minimarket.dto;

import com.minimarket.entity.Producto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

@Schema(description = "DTO de salida para productos del catalogo del minimarket")
@Relation(itemRelation = "producto", collectionRelation = "productos")
public record ProductoDTO(
        @Schema(description = "Identificador unico del producto", example = "1")
        Long id,
        @Schema(description = "Nombre comercial del producto", example = "Arroz grano largo 1kg")
        String nombre,
        @Schema(description = "Precio de venta del producto en pesos chilenos", example = "1490.0")
        Double precio,
        @Schema(description = "Cantidad disponible en stock", example = "80")
        Integer stock,
        @Schema(description = "Identificador de la categoria asociada", example = "1")
        Long categoriaId,
        @Schema(description = "Nombre de la categoria asociada", example = "Abarrotes")
        String categoriaNombre
) {
    public static ProductoDTO fromEntity(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
        );
    }
}
