package com.example.shopapp.controller;

import com.example.shopapp.configuration.Translator;
import com.example.shopapp.dto.request.UserLoginRequestDTO;
import com.example.shopapp.dto.request.UserRequestDTO;
import com.example.shopapp.dto.response.ResponseData;
import com.example.shopapp.dto.response.ResponseError;
import com.example.shopapp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/users")
@Validated
@Slf4j
@Tag(name = "User controller")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseData<?> Register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            if (!userRequestDTO.getPassword().equals(userRequestDTO.getRetypePassword())){
                return new ResponseError(HttpStatus.FORBIDDEN.value(), "Passwords do not match");
            }

            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"),
                    userService.createUser(userRequestDTO).getId());
        }catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseData<?> Login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        //kiem tra thong tin dang nhap va sinh token
        // tra ve token trong response
        String token = null;

        token = userService.login(userLoginRequestDTO.getPhoneNumber(), userLoginRequestDTO.getPassword());

        return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.get.success"), token);
    }
}
