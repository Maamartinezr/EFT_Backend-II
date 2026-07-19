package com.minimarket.hateoas;

import com.minimarket.controller.CarritoController;
import com.minimarket.controller.CategoriaController;
import com.minimarket.controller.ProductoController;
import com.minimarket.dto.ProductoDTO;
import com.minimarket.entity.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<ProductoDTO>> {
    @Override
    public EntityModel<ProductoDTO> toModel(Producto producto) {
        return EntityModel.of(ProductoDTO.fromEntity(producto))
                .add(linkTo(methodOn(ProductoController.class).obtenerProductoPorId(producto.getId())).withSelfRel())
                .add(linkTo(methodOn(ProductoController.class).listarProductos(0, 10)).withRel("collection"))
                .add(linkTo(methodOn(CategoriaController.class).obtenerCategoriaPorId(producto.getCategoria().getId())).withRel("categoria"))
                .add(linkTo(methodOn(CarritoController.class).listarCarrito(0, 10)).withRel("carrito"));
    }
}
