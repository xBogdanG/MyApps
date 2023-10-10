package com.reservation.item.controller;

import com.reservation.item.model.GetUsersResponse;
import com.reservation.item.model.UserDto;
import com.reservation.item.service.UserService;
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
@RequestMapping("api/v1/pageable/users")
public class PageableUsersController {
    private final UserService userService;

    @Autowired

    public PageableUsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> findAll(
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String sortBy,
            @RequestParam(defaultValue = "") String direction
    ){
        Pageable paging = PageRequest.of(page, size);
        if(!sortBy.isEmpty() && direction.equals("ASC")){
            paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        } else if  (!sortBy.isEmpty() && direction.equals("DESC")){
            paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
            }
        Page<UserDto> usersPage;
        if(firstName.isEmpty()){
            usersPage = userService.findAll(paging);
        } else {
            usersPage = userService.findByFirstNameContaining(firstName, paging);
        }
        List<UserDto> users = usersPage.getContent();
        int count = users.size();
        GetUsersResponse usersResponse = new GetUsersResponse(users, count);
        return ResponseEntity.ok(usersResponse);

    }
}
