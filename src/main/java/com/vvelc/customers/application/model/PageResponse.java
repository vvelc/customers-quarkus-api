package com.vvelc.customers.application.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PageResponse<T> {
    private List<T> items;
    private int page;
    private int size;
    private Long total;
}
