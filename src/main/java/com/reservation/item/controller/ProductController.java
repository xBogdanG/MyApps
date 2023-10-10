package com.reservation.item.controller;

import com.reservation.item.entity.Product;
import com.reservation.item.helper.ExceptionHelper;
import com.reservation.item.helper.MapEntity;
import com.reservation.item.model.GetProductsResponse;
import com.reservation.item.model.ProductDto;
import com.reservation.item.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<GetProductsResponse> getProducts() {
        List<ProductDto> products = productService.getProducts();
        int count = products.size();
        long totalCount = productService.getTotalProductsCount();
        GetProductsResponse productsResponse = new GetProductsResponse(products, count, totalCount);
        return ResponseEntity.ok(productsResponse);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody @Validated ProductDto productDto) throws Exception {
        try {
            Product product = new Product();
            MapEntity.mapProductDtoToProduct(productDto, product);
            productService.addProduct(product);
            MapEntity.mapProductToProductDto(product, productDto);
            return ResponseEntity.ok(productDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ExceptionHelper.extractException(e.getCause().toString()), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        try {
            Product product = new Product();
            ProductDto foundProduct = productService.getProductById(id);
            if(foundProduct != null){
                MapEntity.mapProductDtoToProduct(productDto, product);
                productService.updateProduct(id, product);
                product.setId(foundProduct.getId());
                MapEntity.mapProductToProductDto(product, productDto);
                return ResponseEntity.ok(productDto);
            }
                return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ExceptionHelper.extractException(e.getCause().toString()), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        ProductDto productDto = productService.deleteProduct(id);
        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        productService.deleteAll();
        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @GetMapping("/interval")
    public ResponseEntity<List<ProductDto>> findProductsByAddedDateBetween(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                                           @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        if (startDate.compareTo(endDate) > 0) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok(productService.findProductsByAddedDateBetween(startDate, endDate));
    }

    @GetMapping("/top5")
    public ResponseEntity<List<ProductDto>> findTop5ByOrderByPriceDesc() {
        return ResponseEntity.ok(productService.findTop5ByOrderByPriceDesc());
    }

    @GetMapping("/prod-populate")
    public ResponseEntity<?> productsPopulate() {
        for (int i = 0; i < 5000; i++) {
            Product product = new Product();
            product.setName("Name" + i);
            product.setDescription("Description" + i);
            product.setPrice(500D);
            product.setQuantity(5);
            productService.addProduct(product);
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }
}