package com.reservation.item.service;

import com.reservation.item.entity.Product;
import com.reservation.item.model.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ProductService {
     List<ProductDto> getProducts();

     ProductDto addProduct(Product product);

     ProductDto getProductById(Long id);

     ProductDto updateProduct(Long id, Product product);

     ProductDto deleteProduct(Long id);

     void deleteAll();

     List<ProductDto> findProductsByAddedDateBetween(Date startDate, Date endDate); //Interval

     List<ProductDto> findTop5ByOrderByPriceDesc();

     Page<ProductDto> findAll(Pageable page);

     Page<ProductDto> findByNameContaining(String name, Pageable page);

    long getTotalProductsCount();
}
