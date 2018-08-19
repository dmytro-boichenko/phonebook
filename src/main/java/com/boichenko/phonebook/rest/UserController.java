package com.boichenko.phonebook.rest;

import com.boichenko.phonebook.model.User;
import com.boichenko.phonebook.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        authService.register(user.getUserName(), user.getPassword());
        return CommonHelper.SUCCESS_RESPONSE;
    }

    @PostMapping("/auth")
    public HttpHeaders authenticate(@RequestBody User user) {
        String token = authService.authenticate(user.getUserName(), user.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return headers;
    }

}
