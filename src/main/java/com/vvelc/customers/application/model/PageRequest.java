package com.vvelc.customers.application.model;

public record PageRequest(
        int page,
        int size
) {
    public int offset() {
        return page * size;
    }
}
