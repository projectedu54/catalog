package com.catalog.dto;

public class CatalogResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private String type;
    private int depth;

    public CatalogResponseDTO(){}

    public CatalogResponseDTO(Long id, String name, String description, Long parentId, String type, int depth) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.type = type;
        this.depth = depth;
    }

    // getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
