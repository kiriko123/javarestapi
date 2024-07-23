package com.example.shopapp.service;

import com.example.shopapp.dto.request.UserRequestDTO;
import com.example.shopapp.model.User;

public interface UserService {
    User createUser(UserRequestDTO userRequestDTO);
    String login(String phoneNumber, String password);
}
