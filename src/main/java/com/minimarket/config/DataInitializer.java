package com.minimarket.config;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Categoria;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;
    private final CarritoRepository carritoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository,
                           UsuarioRepository usuarioRepository,
                           CategoriaRepository categoriaRepository,
                           ProductoRepository productoRepository,
                           InventarioRepository inventarioRepository,
                           CarritoRepository carritoRepository,
                           PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
        this.carritoRepository = carritoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Rol admin = crearRolSiNoExiste("ROLE_ADMIN");
        Rol jefeTurno = crearRolSiNoExiste("ROLE_JEFE_TURNO");
        Rol cajero = crearRolSiNoExiste("ROLE_CAJERO");
        Rol cliente = crearRolSiNoExiste("ROLE_CLIENTE");

        Usuario usuarioAdmin = crearUsuarioSiNoExiste("admin", "Admin123", admin);
        Usuario usuarioJefe = crearUsuarioSiNoExiste("jefe", "Jefe123", jefeTurno);
        Usuario usuarioCajero = crearUsuarioSiNoExiste("cajero", "Cajero123", cajero);
        Usuario usuarioCliente = crearUsuarioSiNoExiste("cliente", "Cliente123", cliente);

        Categoria abarrotes = crearCategoriaSiNoExiste("Abarrotes");
        Categoria bebidas = crearCategoriaSiNoExiste("Bebidas");
        Categoria lacteos = crearCategoriaSiNoExiste("Lacteos");
        Categoria limpieza = crearCategoriaSiNoExiste("Limpieza");
        Categoria cuidadoPersonal = crearCategoriaSiNoExiste("Cuidado personal");
        Categoria snacks = crearCategoriaSiNoExiste("Snacks");

        Producto arroz = crearProductoSiNoExiste("Arroz grano largo 1kg", 1490.0, 80, abarrotes);
        Producto azucar = crearProductoSiNoExiste("Azucar 1kg", 1290.0, 8, abarrotes);
        Producto aceite = crearProductoSiNoExiste("Aceite maravilla 900ml", 2890.0, 35, abarrotes);
        Producto bebidaCola = crearProductoSiNoExiste("Bebida cola 1.5L", 1890.0, 55, bebidas);
        Producto aguaMineral = crearProductoSiNoExiste("Agua mineral 1.6L", 990.0, 6, bebidas);
        Producto jugoNaranja = crearProductoSiNoExiste("Jugo naranja 1L", 1590.0, 24, bebidas);
        Producto leche = crearProductoSiNoExiste("Leche entera 1L", 1190.0, 42, lacteos);
        Producto yogurt = crearProductoSiNoExiste("Yogurt frutilla 1L", 2190.0, 7, lacteos);
        Producto detergente = crearProductoSiNoExiste("Detergente 1kg", 3990.0, 18, limpieza);
        Producto lavavajilla = crearProductoSiNoExiste("Lavavajilla 750ml", 2490.0, 9, limpieza);
        Producto shampoo = crearProductoSiNoExiste("Shampoo familiar 750ml", 4590.0, 16, cuidadoPersonal);
        Producto pastaDental = crearProductoSiNoExiste("Pasta dental 90g", 1690.0, 5, cuidadoPersonal);
        Producto papasFritas = crearProductoSiNoExiste("Papas fritas 150g", 1390.0, 48, snacks);
        Producto galletas = crearProductoSiNoExiste("Galletas vainilla", 990.0, 70, snacks);

        crearMovimientosInventarioSiNoExisten(List.of(
                movimiento(arroz, 50, "Entrada", -12),
                movimiento(arroz, 10, "Salida", -11),
                movimiento(azucar, 20, "Entrada", -10),
                movimiento(azucar, 12, "Salida", -9),
                movimiento(bebidaCola, 40, "Entrada", -8),
                movimiento(bebidaCola, 8, "Salida", -7),
                movimiento(leche, 60, "Entrada", -6),
                movimiento(leche, 18, "Salida", -5),
                movimiento(detergente, 25, "Entrada", -4),
                movimiento(lavavajilla, 16, "Entrada", -3),
                movimiento(pastaDental, 12, "Entrada", -2),
                movimiento(papasFritas, 30, "Entrada", -1)
        ));

        crearCarritosSiNoExisten(List.of(
                carrito(usuarioCliente, arroz, 2),
                carrito(usuarioCliente, leche, 1),
                carrito(usuarioCliente, papasFritas, 3),
                carrito(usuarioCajero, bebidaCola, 1),
                carrito(usuarioAdmin, detergente, 1),
                carrito(usuarioJefe, azucar, 4)
        ));
    }

    private Rol crearRolSiNoExiste(String nombre) {
        return rolRepository.findByNombre(nombre).orElseGet(() -> {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            return rolRepository.save(rol);
        });
    }

    private Usuario crearUsuarioSiNoExiste(String username, String password, Rol rol) {
        return usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRoles(new HashSet<>(List.of(rol)));
            return usuarioRepository.save(usuario);
        });
    }

    private Categoria crearCategoriaSiNoExiste(String nombre) {
        return categoriaRepository.findByNombre(nombre).orElseGet(() -> {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            return categoriaRepository.save(categoria);
        });
    }

    private Producto crearProductoSiNoExiste(String nombre, Double precio, Integer stock, Categoria categoria) {
        return productoRepository.findByNombre(nombre).orElseGet(() -> {
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setCategoria(categoria);
            return productoRepository.save(producto);
        });
    }

    private Inventario movimiento(Producto producto, Integer cantidad, String tipoMovimiento, int diasDesdeHoy) {
        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(cantidad);
        inventario.setTipoMovimiento(tipoMovimiento);
        inventario.setFechaMovimiento(fechaRelativa(diasDesdeHoy));
        return inventario;
    }

    private void crearMovimientosInventarioSiNoExisten(List<Inventario> movimientos) {
        if (inventarioRepository.count() > 0) {
            return;
        }
        inventarioRepository.saveAll(movimientos);
    }

    private Carrito carrito(Usuario usuario, Producto producto, Integer cantidad) {
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(cantidad);
        return carrito;
    }

    private void crearCarritosSiNoExisten(List<Carrito> carritos) {
        if (carritoRepository.count() > 0) {
            return;
        }
        carritoRepository.saveAll(carritos);
    }

    private Date fechaRelativa(int diasDesdeHoy) {
        long milisegundosPorDia = 24L * 60L * 60L * 1000L;
        return new Date(System.currentTimeMillis() + (diasDesdeHoy * milisegundosPorDia));
    }
}
