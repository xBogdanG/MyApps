package com.reservation.item.helper;

import com.reservation.item.entity.Product;
import com.reservation.item.entity.User;
import com.reservation.item.model.ProductDto;
import com.reservation.item.model.UserDto;

import java.util.Date;

public class MapEntity {

    public static void mapUserProperties(User initialUser, User user) {
        initialUser.setFirstName(user.getFirstName());
        initialUser.setLastName(user.getLastName());
        initialUser.setPassword(user.getPassword());
        initialUser.setEmail(user.getEmail());
        initialUser.setProducts(user.getProducts());
    }

    public static void mapUserToUserDto(User user, UserDto userDTO) {
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setProducts(user.getProducts());
    }

    public static void mapUserDtoToUser(UserDto userDTO, User user) {
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
    }

    public static void mapProductProperties(Product initialProduct, Product product) {
        initialProduct.setName(product.getName());
        initialProduct.setDescription(product.getDescription());
        initialProduct.setPrice(product.getPrice());
        initialProduct.setQuantity(product.getQuantity());
    }

    public static void mapProductToProductDto(Product product, ProductDto productDto) {
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setAddedDate(product.getAddedDate());
    }

    public static void mapProductDtoToProduct(ProductDto productDto, Product product) {
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setAddedDate(new Date());

    }
}