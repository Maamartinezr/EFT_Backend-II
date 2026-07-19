package com.minimarket;

import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void findDisponiblesConsultaProductosConStockMayorACero() {
        Producto producto = new Producto();
        producto.setNombre("Leche 1L");
        producto.setStock(12);
        when(productoRepository.findByStockGreaterThan(0)).thenReturn(List.of(producto));

        List<Producto> disponibles = productoService.findDisponibles();

        assertThat(disponibles).hasSize(1);
        assertThat(disponibles.get(0).getStock()).isGreaterThan(0);
        verify(productoRepository).findByStockGreaterThan(0);
    }

    @Test
    void findConStockBajoConsultaProductosMenoresOIgualesAlMinimo() {
        Producto producto = new Producto();
        producto.setNombre("Arroz 1kg");
        producto.setStock(5);
        when(productoRepository.findByStockLessThanEqual(10)).thenReturn(List.of(producto));

        List<Producto> stockBajo = productoService.findConStockBajo(10);

        assertThat(stockBajo).hasSize(1);
        assertThat(stockBajo.get(0).getStock()).isLessThanOrEqualTo(10);
        verify(productoRepository).findByStockLessThanEqual(10);
    }
}
