package com.minimarket;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
import com.minimarket.service.impl.CategoriaServiceImpl;
import com.minimarket.service.impl.DetalleVentaServiceImpl;
import com.minimarket.service.impl.InventarioServiceImpl;
import com.minimarket.service.impl.RolServiceImpl;
import com.minimarket.service.impl.UsuarioServiceImpl;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceLayerUnitTest {
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private DetalleVentaRepository detalleVentaRepository;
    @Mock
    private InventarioRepository inventarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CarritoServiceImpl carritoService;
    @InjectMocks
    private CategoriaServiceImpl categoriaService;
    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaService;
    @InjectMocks
    private InventarioServiceImpl inventarioService;
    @InjectMocks
    private RolServiceImpl rolService;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;
    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void carritoServiceDelegaOperacionesCrudYConsultaPorUsuario() {
        Carrito carrito = new Carrito();
        when(carritoRepository.findAll()).thenReturn(List.of(carrito));
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(carrito)).thenReturn(carrito);
        when(carritoRepository.findByUsuarioId(7L)).thenReturn(List.of(carrito));

        assertThat(carritoService.findAll()).containsExactly(carrito);
        assertThat(carritoService.findById(1L)).isSameAs(carrito);
        assertThat(carritoService.save(carrito)).isSameAs(carrito);
        assertThat(carritoService.findByUsuarioId(7L)).containsExactly(carrito);
        carritoService.deleteById(1L);

        verify(carritoRepository).deleteById(1L);
    }

    @Test
    void categoriaServiceDelegaOperacionesCrud() {
        Categoria categoria = new Categoria();
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        assertThat(categoriaService.findAll()).containsExactly(categoria);
        assertThat(categoriaService.findById(1L)).isSameAs(categoria);
        assertThat(categoriaService.save(categoria)).isSameAs(categoria);
        categoriaService.deleteById(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void detalleVentaServiceDelegaOperacionesCrudYConsultaPorVenta() {
        DetalleVenta detalle = new DetalleVenta();
        when(detalleVentaRepository.findAll()).thenReturn(List.of(detalle));
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalle));
        when(detalleVentaRepository.save(detalle)).thenReturn(detalle);
        when(detalleVentaRepository.findByVentaId(9L)).thenReturn(List.of(detalle));

        assertThat(detalleVentaService.findAll()).containsExactly(detalle);
        assertThat(detalleVentaService.findById(1L)).isSameAs(detalle);
        assertThat(detalleVentaService.save(detalle)).isSameAs(detalle);
        assertThat(detalleVentaService.findByVentaId(9L)).containsExactly(detalle);
        detalleVentaService.deleteById(1L);

        verify(detalleVentaRepository).deleteById(1L);
    }

    @Test
    void inventarioServiceDelegaOperacionesCrudYConsultaPorProducto() {
        Inventario inventario = new Inventario();
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(inventario)).thenReturn(inventario);
        when(inventarioRepository.findByProductoId(4L)).thenReturn(List.of(inventario));

        assertThat(inventarioService.findAll()).containsExactly(inventario);
        assertThat(inventarioService.findById(1L)).isSameAs(inventario);
        assertThat(inventarioService.save(inventario)).isSameAs(inventario);
        assertThat(inventarioService.findByProductoId(4L)).containsExactly(inventario);
        inventarioService.deleteById(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void rolServiceBuscaPorNombre() {
        Rol rol = new Rol();
        when(rolRepository.findByNombre("ROLE_ADMIN")).thenReturn(Optional.of(rol));

        assertThat(rolService.findByNombre("ROLE_ADMIN")).contains(rol);
    }

    @Test
    void usuarioServiceCifraContrasenaAntesDeGuardar() {
        Usuario usuario = new Usuario();
        usuario.setPassword("Clave123");
        when(passwordEncoder.encode("Clave123")).thenReturn("$2a$hash");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario guardado = usuarioService.save(usuario);

        assertThat(guardado.getPassword()).isEqualTo("$2a$hash");
        verify(passwordEncoder).encode("Clave123");
    }

    @Test
    void usuarioServiceMantieneContrasenaBCryptExistente() {
        Usuario usuario = new Usuario();
        usuario.setPassword("$2a$hash");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        assertThat(usuarioService.save(usuario).getPassword()).isEqualTo("$2a$hash");
    }

    @Test
    void usuarioServiceDelegaConsultasYEliminacion() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        assertThat(usuarioService.findAll()).containsExactly(usuario);
        assertThat(usuarioService.findById(1L)).contains(usuario);
        assertThat(usuarioService.findByUsername("admin")).contains(usuario);
        usuarioService.deleteById(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void ventaServiceDelegaOperacionesYConsultaPorUsuario() {
        Venta venta = new Venta();
        when(ventaRepository.findAll()).thenReturn(List.of(venta));
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(venta)).thenReturn(venta);
        when(ventaRepository.findByUsuarioId(7L)).thenReturn(List.of(venta));

        assertThat(ventaService.findAll()).containsExactly(venta);
        assertThat(ventaService.findById(1L)).isSameAs(venta);
        assertThat(ventaService.save(venta)).isSameAs(venta);
        assertThat(ventaService.findByUsuarioId(7L)).containsExactly(venta);
    }
}
