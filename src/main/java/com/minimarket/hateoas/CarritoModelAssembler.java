package com.minimarket.hateoas;

import com.minimarket.controller.CarritoController;
import com.minimarket.controller.ProductoController;
import com.minimarket.controller.UsuarioController;
import com.minimarket.dto.CarritoDTO;
import com.minimarket.entity.Carrito;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<Carrito, EntityModel<CarritoDTO>> {
    @Override
    public EntityModel<CarritoDTO> toModel(Carrito carrito) {
        return EntityModel.of(CarritoDTO.fromEntity(carrito))
                .add(linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(carrito.getId())).withSelfRel())
                .add(linkTo(methodOn(CarritoController.class).listarCarrito(0, 10)).withRel("collection"))
                .add(linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(carrito.getUsuario().getId())).withRel("usuario"))
                .add(linkTo(methodOn(ProductoController.class).obtenerProductoPorId(carrito.getProducto().getId())).withRel("producto"));
    }
}
