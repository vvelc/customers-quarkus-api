package com.vvelc.customers.application.model;

import java.util.List;

public record PageResponse<T>(
        List<T> items,
        int page,
        int size,
        Long total
) {}
