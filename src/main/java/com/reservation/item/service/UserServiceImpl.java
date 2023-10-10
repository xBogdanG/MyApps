package com.reservation.item.service;

import com.reservation.item.entity.Product;
import com.reservation.item.entity.User;
import com.reservation.item.helper.MapEntity;
import com.reservation.item.model.ProductDto;
import com.reservation.item.model.UserDto;
import com.reservation.item.repository.ProductRepository;
import com.reservation.item.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@EnableCaching
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable("users")
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserDto userDto = new UserDto();
            MapEntity.mapUserToUserDto(user, userDto);
            return userDto;
        }).toList();
    }

    @Override
    @Cacheable("user")
    public UserDto getUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isPresent()) {
            UserDto userDto = new UserDto();
            MapEntity.mapUserToUserDto(foundUser.get(), userDto);
            return userDto;
        }
        return null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#user.id") })
    public UserDto addUser(User user) {
        userRepository.save(user);
        UserDto userDto = new UserDto();
        MapEntity.mapUserToUserDto(user, userDto);
        return userDto;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#id") })
    public UserDto updateUser(Long id, User user) {
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isPresent()) {
            User userToUpdate = userFound.get();
            MapEntity.mapUserProperties(userToUpdate, user);
            userRepository.save(userToUpdate);

            UserDto userDto = new UserDto();
            MapEntity.mapUserToUserDto(userToUpdate, userDto);

            return userDto;
        }
        return null;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#id") })
    public UserDto deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            UserDto userDto = new UserDto();
            MapEntity.mapUserToUserDto(user.get(), userDto);
            return userDto;
        }
        return null;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#userId") })
    public UserDto addProductsToUser(Long userId, Long productId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Product> product = productRepository.findById(productId);

        if (user.isPresent() && product.isPresent()) {
            User foundUser = user.get();
            Product foundProduct = product.get();
            Set<Product> products = foundUser.getProducts();
            products.add(foundProduct);
            foundUser.setProducts(products);
            userRepository.save(foundUser);

            UserDto userDto = new UserDto();
            MapEntity.mapUserToUserDto(foundUser, userDto);

            return userDto;
        }
        return null;
    }

    @Override
    public Page<UserDto> findAll(Pageable page) {
        List<UserDto> userDtos = userRepository.findAll(page)
                .stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    MapEntity.mapUserToUserDto(user, userDto);
                    return userDto;
                }).toList();
        return new PageImpl<>(userDtos, page, userDtos.size());
    }

    @Override
    public Page<UserDto> findByFirstNameContaining(String firstName, Pageable page) {
        List<UserDto> userDtos = userRepository.findByFirstNameContainingIgnoreCase(firstName, page)
                .stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    MapEntity.mapUserToUserDto(user, userDto);
                    return userDto;
                }).toList();
        return new PageImpl<>(userDtos, page, userDtos.size());
    }


}