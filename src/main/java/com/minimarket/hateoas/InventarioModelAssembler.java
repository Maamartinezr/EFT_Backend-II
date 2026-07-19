package com.minimarket.hateoas;

import com.minimarket.controller.InventarioController;
import com.minimarket.controller.ProductoController;
import com.minimarket.dto.InventarioDTO;
import com.minimarket.entity.Inventario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<InventarioDTO>> {
    @Override
    public EntityModel<InventarioDTO> toModel(Inventario inventario) {
        return EntityModel.of(InventarioDTO.fromEntity(inventario))
                .add(linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(inventario.getId())).withSelfRel())
                .add(linkTo(methodOn(InventarioController.class).listarMovimientosDeInventario(0, 10)).withRel("collection"))
                .add(linkTo(methodOn(ProductoController.class).obtenerProductoPorId(inventario.getProducto().getId())).withRel("producto"));
    }
}
