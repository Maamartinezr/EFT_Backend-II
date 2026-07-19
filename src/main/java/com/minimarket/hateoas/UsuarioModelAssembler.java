package com.minimarket.hateoas;

import com.minimarket.controller.CarritoController;
import com.minimarket.controller.UsuarioController;
import com.minimarket.dto.UsuarioDTO;
import com.minimarket.entity.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<UsuarioDTO>> {
    @Override
    public EntityModel<UsuarioDTO> toModel(Usuario usuario) {
        return EntityModel.of(UsuarioDTO.fromEntity(usuario))
                .add(linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel())
                .add(linkTo(methodOn(UsuarioController.class).listarUsuarios(0, 10)).withRel("collection"))
                .add(linkTo(methodOn(CarritoController.class).listarCarrito(0, 10)).withRel("carrito"));
    }
}
