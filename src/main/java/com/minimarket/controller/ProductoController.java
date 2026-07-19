package com.minimarket.controller;

import com.minimarket.dto.ProductoDTO;
import com.minimarket.entity.Producto;
import com.minimarket.hateoas.ProductoModelAssembler;
import com.minimarket.service.ProductoService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Producto", description = "Gestion de catalogo, disponibilidad y alertas de stock")
public class ProductoController {
    private final ProductoService productoService;
    private final ProductoModelAssembler productoModelAssembler;

    public ProductoController(ProductoService productoService, ProductoModelAssembler productoModelAssembler) {
        this.productoService = productoService;
        this.productoModelAssembler = productoModelAssembler;
    }

    @GetMapping
    @Operation(
            summary = "Lista productos con enlaces HATEOAS",
            description = "Retorna una coleccion paginada de productos representados como DTO y enriquecidos con enlaces self, collection, disponibles y stock-bajo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos consultados correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public CollectionModel<EntityModel<ProductoDTO>> listarProductos(
            @Parameter(description = "Numero de pagina a consultar, comenzando desde 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de registros por pagina", example = "2")
            @RequestParam(defaultValue = "10") int size) {
        return toCollectionModel(productoService.findAll(), page, size)
                .add(linkTo(methodOn(ProductoController.class).listarProductos(page, size)).withSelfRel())
                .add(linkTo(methodOn(ProductoController.class).listarProductosDisponibles(page, size)).withRel("disponibles"))
                .add(linkTo(methodOn(ProductoController.class).listarProductosConStockBajo(10, page, size)).withRel("stock-bajo"));
    }

    @GetMapping("/disponibles")
    @Operation(
            summary = "Consulta productos con stock disponible para clientes",
            description = "Retorna productos cuyo stock es mayor a cero, incluyendo enlaces HATEOAS para navegar al recurso y a la coleccion."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos disponibles consultados correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public CollectionModel<EntityModel<ProductoDTO>> listarProductosDisponibles(
            @Parameter(description = "Numero de pagina a consultar, comenzando desde 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de registros por pagina", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return toCollectionModel(productoService.findDisponibles(), page, size)
                .add(linkTo(methodOn(ProductoController.class).listarProductosDisponibles(page, size)).withSelfRel())
                .add(linkTo(methodOn(ProductoController.class).listarProductos(page, size)).withRel("collection"));
    }

    @GetMapping("/stock-bajo")
    @Operation(
            summary = "Lista productos que requieren reposicion",
            description = "Retorna productos cuyo stock es menor o igual al minimo informado. Requiere rol administrador o jefe de turno.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos con stock bajo consultados correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public CollectionModel<EntityModel<ProductoDTO>> listarProductosConStockBajo(
            @Parameter(description = "Stock maximo para considerar un producto como bajo", example = "10")
            @RequestParam(defaultValue = "10") Integer minimo,
            @Parameter(description = "Numero de pagina a consultar, comenzando desde 0", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de registros por pagina", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        return toCollectionModel(productoService.findConStockBajo(minimo), page, size)
                .add(linkTo(methodOn(ProductoController.class).listarProductosConStockBajo(minimo, page, size)).withSelfRel())
                .add(linkTo(methodOn(ProductoController.class).listarProductos(page, size)).withRel("collection"));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtiene un producto por id",
            description = "Retorna un producto individual como DTO, incorporando enlaces HATEOAS hacia el recurso actual, la coleccion, la categoria y el carrito."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProductoPorId(
            @Parameter(description = "Identificador unico del producto", example = "1", required = true)
            @PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return (producto != null) ? ResponseEntity.ok(productoModelAssembler.toModel(producto)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
            summary = "Crea un producto",
            description = "Registra un nuevo producto en el catalogo. Requiere rol administrador o jefe de turno.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public EntityModel<ProductoDTO> guardarProducto(@RequestBody Producto producto) {
        return productoModelAssembler.toModel(productoService.save(producto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualiza precio, stock o categoria de un producto",
            description = "Actualiza los datos de un producto existente. Requiere rol administrador o jefe de turno.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductoDTO>> actualizarProducto(
            @Parameter(description = "Identificador unico del producto", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Producto producto) {
        Producto productoExistente = productoService.findById(id);
        if (productoExistente != null) {
            producto.setId(id);
            return ResponseEntity.ok(productoModelAssembler.toModel(productoService.save(producto)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un producto",
            description = "Elimina un producto existente del catalogo. Requiere rol administrador.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "Identificador unico del producto", example = "1", required = true)
            @PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto != null) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private CollectionModel<EntityModel<ProductoDTO>> toCollectionModel(List<Producto> productos, int page, int size) {
        List<EntityModel<ProductoDTO>> modelos = paginar(productos, page, size).stream()
                .map(productoModelAssembler::toModel)
                .toList();

        return CollectionModel.of(modelos);
    }

    private List<Producto> paginar(List<Producto> productos, int page, int size) {
        int pagina = Math.max(page, 0);
        int tamanio = Math.max(1, Math.min(size, 100));
        int desde = pagina * tamanio;
        if (desde >= productos.size()) {
            return List.of();
        }
        int hasta = Math.min(desde + tamanio, productos.size());
        return productos.subList(desde, hasta);
    }
}
