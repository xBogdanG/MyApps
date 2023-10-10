package com.reservation.item.service;

import com.reservation.item.entity.Product;
import com.reservation.item.helper.MapEntity;
import com.reservation.item.model.ProductDto;
import com.reservation.item.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable("products")
    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream().map(product -> {
            ProductDto productDto = new ProductDto();
            MapEntity.mapProductToProductDto(product, productDto);
            return productDto;
        }).toList();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product", key = "#product.id")})
    public ProductDto addProduct(Product product) {
        productRepository.save(product);
        ProductDto productDto = new ProductDto();
        MapEntity.mapProductToProductDto(product, productDto);
        return productDto;
    }

    @Override
    @Cacheable("product")
    public ProductDto getProductById(Long id) {
        Optional<Product> foundProduct = productRepository.findById(id);
        if (foundProduct.isPresent()) {
            ProductDto productDto = new ProductDto();
            MapEntity.mapProductToProductDto(foundProduct.get(), productDto);
            return productDto;
        }
        return null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product", key = "#id")})
    public ProductDto updateProduct(Long id, Product product) {
        Optional<Product> productFound = productRepository.findById(id);
        if (productFound.isPresent()) {
            Product productToUpdate = productFound.get();
            MapEntity.mapProductProperties(productToUpdate, product);
            productRepository.save(productToUpdate);

            ProductDto productDto = new ProductDto();
            MapEntity.mapProductToProductDto(productToUpdate, productDto);

            return productDto;
        }
        return null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "product", key = "#id")})
    public ProductDto deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            ProductDto productDto = new ProductDto();
            MapEntity.mapProductToProductDto(product.get(), productDto);
            return productDto;
        }

        return null;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void deleteAll() {
        productRepository.deleteAll();

    }

    @Override
    public List<ProductDto> findProductsByAddedDateBetween(Date startDate, Date endDate) {
        return productRepository.findProductsByAddedDateBetween(startDate, endDate)
                .stream()
                .map(product -> {
                    ProductDto productDto = new ProductDto();
                    MapEntity.mapProductToProductDto(product, productDto);
                    return productDto;
                }).toList();
    }

    @Override
    public List<ProductDto> findTop5ByOrderByPriceDesc() {
        return productRepository.findTop5ByOrderByPriceDesc()
                .stream()
                .map(product -> {
                    ProductDto productDto = new ProductDto();
                    MapEntity.mapProductToProductDto(product, productDto);
                    return productDto;
                }).toList();
    }

    @Override
    public Page<ProductDto> findAll(Pageable page) {
        List<ProductDto> productDtos = productRepository.findAll(page)
                .stream()
                .map(product -> {
                    ProductDto productDto = new ProductDto();
                    MapEntity.mapProductToProductDto(product, productDto);
                    return productDto;
                }).toList();
        return new PageImpl<>(productDtos, page, productDtos.size());
    }

    @Override
    public Page<ProductDto> findByNameContaining(String name, Pageable page) {
        List<ProductDto> productDtos = productRepository.findByNameContainingIgnoreCase(name, page)
                .stream()
                .map(product -> {
                    ProductDto productDto = new ProductDto();
                    MapEntity.mapProductToProductDto(product, productDto);
                    return productDto;
                }).toList();
        return new PageImpl<>(productDtos, page, productDtos.size());
    }

    @Override
    public long getTotalProductsCount(){
        return productRepository.count();

    }
}
