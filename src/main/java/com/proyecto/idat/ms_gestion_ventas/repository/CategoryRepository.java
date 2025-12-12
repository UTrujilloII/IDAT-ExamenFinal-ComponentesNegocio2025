package com.proyecto.idat.ms_gestion_ventas.repository;

import com.proyecto.idat.ms_gestion_ventas.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categoria, Long> {
}
