package com.catalog.controller;

import com.catalog.dto.CatalogResponseDTO;
import com.catalog.dto.CreateCatalogRequest;
import com.catalog.dto.PageResponse;
import com.catalog.entity.Catalog;
import com.catalog.service.CatalogService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping
    public ResponseEntity<Catalog> createCatalog(
            @RequestBody CreateCatalogRequest request) {

        Catalog catalog = catalogService.createCatalog(request);
        return ResponseEntity.ok(catalog);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Catalog> getCatalogById(@PathVariable Long id) {
        Catalog catalog = catalogService.getCatalogById(id);
        return ResponseEntity.ok(catalog);
    }

    @GetMapping("/roots")
    public ResponseEntity<List<Catalog>> getRootCatalogs() {
        return ResponseEntity.ok(catalogService.getRootCatalogs());
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<PageResponse<CatalogResponseDTO>> getChildren(
            @PathVariable Long id,
            Pageable pageable) {
        if (pageable.getPageSize() > 50) {
            throw new IllegalArgumentException("Max page size is 50");
        }
        return ResponseEntity.ok(catalogService.getChildren(id, pageable));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CatalogResponseDTO>> getCatalogs(
            @PageableDefault(size = 20)
            @SortDefault(sort = "name")
            Pageable pageable) {

        if (pageable.getPageSize() > 50) {
            throw new IllegalArgumentException("Max page size is 50");
        }


        return ResponseEntity.ok(catalogService.getCatalogs(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<CatalogResponseDTO>> search(
            @RequestParam String query,
            Pageable pageable) {

        return ResponseEntity.ok(catalogService.searchByName(query, pageable));
    }

    // Search inside a category
    @GetMapping("/{parentId}/search")
    public PageResponse<CatalogResponseDTO> searchByParent(
            @PathVariable Long parentId,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.fromPage(catalogService.searchByParent(parentId, query, pageable), c -> c);
    }


    @GetMapping("/autocomplete")
    public List<CatalogResponseDTO> autocomplete(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int limit) {
        return catalogService.autocomplete(query, limit);
    }

    @GetMapping("/{courseId}/breadcrumb")
    public List<Catalog> getBreadcrumb(@PathVariable Long courseId) {
        return catalogService.getBreadcrumb(courseId);
    }


    @PutMapping("/{id}")
    public Catalog update(@PathVariable Long id, @RequestBody Catalog catalog) {
        return catalogService.updateCatalog(id, catalog);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        catalogService.deleteCatalog(id);
    }



}
