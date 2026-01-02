package com.catalog.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageResponse<T> {

    private List<T> items;      // renamed from 'content'
    private int page;
    private int size;
    private long totalItems;    // renamed from 'totalElements'
    private int totalPages;
    private boolean last;       // new field

    // No-args constructor
    public PageResponse() {
    }
    // Constructor
    public PageResponse(List<T> items, int page, int size, long totalItems, int totalPages, boolean last) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.last = last;
    }

    // Convenience method to create PageResponse from Spring Page
    public static <T, R> PageResponse<R> fromPage(Page<T> page, java.util.function.Function<T, R> mapper) {
        List<R> items = page.getContent().stream().map(mapper).collect(Collectors.toList());
        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // getters & setters

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
