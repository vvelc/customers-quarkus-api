package com.vvelc.customers.interface_.rest.dto;

import com.vvelc.customers.application.model.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomerPageResponse extends PageResponse<CustomerResponse> {
    List<CustomerResponse> items;
    int page;
    int size;
    Long total;
}
