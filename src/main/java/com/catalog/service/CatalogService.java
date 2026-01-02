package com.catalog.service;

import com.catalog.config.CatalogMapper;
import com.catalog.dto.CatalogResponseDTO;
import com.catalog.dto.CreateCatalogRequest;
import com.catalog.dto.PageResponse;
import com.catalog.entity.Catalog;
import com.catalog.exception.customException.CatalogNotFoundException;
import com.catalog.repository.CatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public Catalog createCatalog(CreateCatalogRequest request) {

        // Step 1: Fetch parent if exists
        Catalog parent = null;
        if (request.getParentId() != null) {
            parent = catalogRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent catalog not found"));
        }

        // Step 2: Create catalog (without path & depth yet)
        Catalog catalog = new Catalog();
        catalog.setName(request.getName());
        catalog.setDescription(request.getDescription());
        catalog.setType(request.getType());
        catalog.setParentId(request.getParentId());

        // Step 3: Save once to generate ID
        catalog = catalogRepository.save(catalog);

        // Step 4: Set path & depth
        if (parent != null) {
            catalog.setPath(parent.getPath() + catalog.getId() + "/");
            catalog.setDepth(parent.getDepth() + 1);
        } else {
            catalog.setPath("/" + catalog.getId() + "/");
            catalog.setDepth(0);
        }

        // Step 5: Save final state
        return catalogRepository.save(catalog);
    }

    public Catalog getCatalogById(Long id) {
        return catalogRepository.findById(id)
                .orElseThrow(() -> new CatalogNotFoundException(id));
    }

    public List<Catalog> getRootCatalogs() {
        return catalogRepository.findByParentIdIsNullOrderByName();
    }

    public PageResponse<CatalogResponseDTO> getChildren(Long parentId, Pageable pageable) {

        Page<Catalog> page = catalogRepository.findByParentId(parentId, pageable);

        List<CatalogResponseDTO> items = page.getContent()
                .stream()
                .map(CatalogMapper::toDto)
                .toList();

        PageResponse<CatalogResponseDTO> response = new PageResponse<>();
        response.setItems(items);
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }

    public PageResponse<CatalogResponseDTO> getCatalogs(Pageable pageable) {

        Page<Catalog> page = catalogRepository.findAll(pageable);

        List<CatalogResponseDTO> items = page.getContent()
                .stream()
                .map(CatalogMapper::toDto)
                .toList();

        PageResponse<CatalogResponseDTO> response = new PageResponse<>();
        response.setItems(items);
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }

    public PageResponse<CatalogResponseDTO> searchByName(String query, Pageable pageable) {

        Page<Catalog> page =
                catalogRepository.findByNameContainingIgnoreCase(query, pageable);

        List<CatalogResponseDTO> items = page.getContent()
                .stream()
                .map(CatalogMapper::toDto)
                .toList();

        PageResponse<CatalogResponseDTO> response = new PageResponse<>();
        response.setItems(items);
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());

        return response;
    }


    // Search inside a category
    public Page<CatalogResponseDTO> searchByParent(Long parentId, String query, Pageable pageable) {
        return catalogRepository.findByParentIdAndNameContainingIgnoreCase(parentId, query, pageable)
                .map(CatalogMapper::toDto);
    }

    // Autocomplete
    public List<CatalogResponseDTO> autocomplete(String query, int limit) {
        return catalogRepository.findTop5ByNameContainingIgnoreCase(query)
                .stream()
                .limit(limit)
                .map(CatalogMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<Catalog> getBreadcrumb(Long categoryId) {
        List<Catalog> breadcrumb = new ArrayList<>();
        Catalog current = catalogRepository.findById(categoryId).orElse(null);

        while (current != null) {
            breadcrumb.add(0, current); // add at start
            if (current.getParentId() != null) {
                current = catalogRepository.findById(current.getParentId()).orElse(null);
            } else {
                current = null;
            }
        }
        return breadcrumb;
    }


    public Catalog updateCatalog(Long id, Catalog updatedCatalog) {
        Catalog existing = catalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catalog not found"));
        existing.setName(updatedCatalog.getName());
        existing.setDescription(updatedCatalog.getDescription());
        existing.setParentId(updatedCatalog.getParentId());
        existing.setType(updatedCatalog.getType());
        existing.setDepth(updatedCatalog.getDepth());
        return catalogRepository.save(existing);
    }


    public void deleteCatalog(Long id) {
        // Optional: Delete all child categories recursively
        deleteChildren(id);
        catalogRepository.deleteById(id);
    }

    private void deleteChildren(Long parentId) {
        List<Catalog> children = catalogRepository.findByParentId(parentId);
        for (Catalog child : children) {
            deleteChildren(child.getId());
            catalogRepository.deleteById(child.getId());
        }
    }


}
