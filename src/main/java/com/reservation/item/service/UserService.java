package com.reservation.item.service;

import com.reservation.item.entity.User;
import com.reservation.item.model.ProductDto;
import com.reservation.item.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
     UserDto getUserById(Long id);
     UserDto addUser(User user);
     UserDto updateUser(Long id, User user);
     UserDto deleteUser(Long id);
     void deleteAll();
     UserDto addProductsToUser (Long id, Long productId);

     Page<UserDto> findAll(Pageable page);
     Page<UserDto> findByFirstNameContaining (String firstName, Pageable page);


}