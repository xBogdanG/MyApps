package com.reservation.item.repository;

import com.reservation.item.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    Page<User> findAll(Pageable page);

    Page<User> findByFirstNameContainingIgnoreCase(String firstName, Pageable page);

//    List<User> getUsersWithPagination(Pageable page);
}
