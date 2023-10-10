package com.reservation.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsResponse {
    private List<ProductDto> products;
    private int count;
    private long totalCount;
}
