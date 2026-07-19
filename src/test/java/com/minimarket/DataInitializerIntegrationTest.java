package com.minimarket;

import com.minimarket.repository.CarritoRepository;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataInitializerIntegrationTest {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void cargaDatosDeDemostracionParaSwaggerPostmanYH2() {
        assertThat(categoriaRepository.count()).isGreaterThanOrEqualTo(6);
        assertThat(productoRepository.count()).isGreaterThanOrEqualTo(14);
        assertThat(inventarioRepository.count()).isGreaterThanOrEqualTo(12);
        assertThat(carritoRepository.count()).isGreaterThanOrEqualTo(6);
        assertThat(usuarioRepository.findByUsername("admin")).isPresent();
        assertThat(usuarioRepository.findByUsername("cliente")).isPresent();
    }

    @Test
    void existenProductosConStockNormalYStockBajo() {
        assertThat(productoRepository.findByStockGreaterThan(10)).isNotEmpty();
        assertThat(productoRepository.findByStockLessThanEqual(10)).isNotEmpty();
    }
}
