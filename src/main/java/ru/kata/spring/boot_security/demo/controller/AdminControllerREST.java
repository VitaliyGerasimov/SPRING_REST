package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.Util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.Util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.Util.UserNotUpdatedException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/api")
public class AdminControllerREST {
    private final UserService userService;

    @Autowired
    public AdminControllerREST(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/authorized_user")
    public ResponseEntity<User> getAuthenticatedUser(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable("id") Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.update(id, user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.createNewUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse("Человек с таким id не был найден"
                , System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage()
                , System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST); // NOT FOUND Status 400
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotUpdatedException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse(e.getMessage()
                , System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.BAD_REQUEST); // NOT FOUND Status 400
    }
}
