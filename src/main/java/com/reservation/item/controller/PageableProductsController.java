package com.reservation.item.controller;

import com.reservation.item.model.GetProductsResponse;
import com.reservation.item.model.ProductDto;
import com.reservation.item.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/pageable/products")
public class PageableProductsController {

    private final ProductService productService;

    @Autowired
    public PageableProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<GetProductsResponse> findAll(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String sortBy,
            @RequestParam(defaultValue = "") String direction
    ) {
        Pageable paging = PageRequest.of(page, size);
        if (!sortBy.isEmpty() && direction.equals("ASC")) {
            paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        }
        else if (!sortBy.isEmpty() && direction.equals("DESC")) {
            paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        }

        Page<ProductDto> productsPage;
        if (name.isEmpty()) {
            productsPage = productService.findAll(paging);
        } else {
            productsPage = productService.findByNameContaining(name, paging);
        }
        List<ProductDto> products = productsPage.getContent();
        int count = products.size();
        long totalCount = productService.getTotalProductsCount();
        GetProductsResponse productsResponse = new GetProductsResponse(products, count, totalCount);
        return ResponseEntity.ok(productsResponse);
    }
}
