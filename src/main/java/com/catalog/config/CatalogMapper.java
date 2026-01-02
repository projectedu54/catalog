package com.catalog.config;

import com.catalog.dto.CatalogResponseDTO;
import com.catalog.entity.Catalog;

public class CatalogMapper {

    public static CatalogResponseDTO toDto(Catalog catalog) {
        CatalogResponseDTO dto = new CatalogResponseDTO();
        dto.setId(catalog.getId());
        dto.setName(catalog.getName());
        dto.setDescription(catalog.getDescription());
        dto.setParentId(catalog.getParentId());
        dto.setType(catalog.getType());
        dto.setDepth(catalog.getDepth());
        return dto;
    }
}
