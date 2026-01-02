package com.catalog.exception.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CatalogNotFoundException extends RuntimeException {
    public CatalogNotFoundException(Long id) {
        super("Catalog not found with id: " + id);
    }
}
