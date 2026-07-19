package com.minimarket.repository;

import com.minimarket.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    Optional<Producto> findByNombre(String nombre);
    List<Producto> findByStockGreaterThan(Integer stock);
    List<Producto> findByStockLessThanEqual(Integer stock);
}
