package com.reservation.item.controller;

import com.github.javafaker.Faker;
import com.reservation.item.entity.User;
import com.reservation.item.helper.ExceptionHelper;
import com.reservation.item.helper.MapEntity;
import com.reservation.item.model.ExternalUser;
import com.reservation.item.model.GetUsersResponse;
import com.reservation.item.model.UserDto;
import com.reservation.item.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//application programming interface

@RestController()
@RequestMapping("/api/v1/users")
//@CrossOrigin("*")
public class UserController {


    public UserController(UserService userService) {
        this.userService = userService;

    }

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class); //reflection


    @GetMapping()
    public ResponseEntity<GetUsersResponse> getUsers() {
        List<UserDto> users = userService.getUsers();
        int count = users.size();
        GetUsersResponse usersResponse = new GetUsersResponse(users, count);
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody @Validated UserDto userDto) throws Exception {
        try {
            User user = new User();
            MapEntity.mapUserDtoToUser(userDto, user);
            userService.addUser(user);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ExceptionHelper.extractException(e.getCause().toString()), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws Exception {
        try {
            User user = new User();
            UserDto foundUser = userService.getUserById(id);
            if (foundUser != null){
                MapEntity.mapUserDtoToUser(userDto, user);
                userService.updateUser(id, user);
                return ResponseEntity.ok(userDto);
            }
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(ExceptionHelper.extractException(e.getCause().toString()), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        UserDto userDto = userService.deleteUser(id);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        userService.deleteAll();
        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @PostMapping("/{userId}/addProduct/{productId}")
    public ResponseEntity<?> addProductsToUser(@PathVariable Long userId, @PathVariable Long productId) {
        UserDto userDto = userService.addProductsToUser(userId, productId);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @GetMapping("/populate")
    public ResponseEntity<?> populateUsers() {
        String url = "https://jsonplaceholder.typicode.com/users";
        RestTemplate restTemplate = new RestTemplate();
        ExternalUser[] externalUsers = restTemplate.getForObject(url, ExternalUser[].class);

        assert externalUsers != null;
        for (ExternalUser externalUser : externalUsers) {
            User user = new User();
            user.setEmail(externalUser.getEmail());

            String newName = externalUser.getName().replace("Mrs. ", "").replace(" V", "");
            String[] splits = newName.split(" ");
            user.setFirstName(splits[0]);
            user.setLastName(splits[1]);
            user.setPassword("12345");

            userService.addUser(user);
        }

        return ResponseEntity.ok(externalUsers);
    }

    @GetMapping("/populate4k")
    public ResponseEntity<?> populate10kUsers() {

        for (int i = 0; i < 4000; i++) {
            User user = new User();
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(firstName + "." + lastName + "@mailnator.com");

            user.setPassword("12345");
            userService.addUser(user);
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }
}