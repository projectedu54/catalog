package com.catalog.repository;

import com.catalog.dto.CatalogResponseDTO;
import com.catalog.dto.PageResponse;
import com.catalog.entity.Catalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    List<Catalog> findByParentIdIsNullOrderByName();
    List<Catalog> findByParentIdOrderByName(Long parentId);
    List<Catalog> findByParentId(Long parentId);
    Page<Catalog> findByParentId(Long parentId, Pageable pageable);
    Page<Catalog> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Catalog> findByParentIdAndNameContainingIgnoreCase(Long parentId, String name, Pageable pageable);
    List<Catalog> findTop5ByNameContainingIgnoreCase(String name);


}
