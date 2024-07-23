package com.example.shopapp.service.impl;

import com.example.shopapp.component.JwtTokenUtil;
import com.example.shopapp.dto.request.UserRequestDTO;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.model.Role;
import com.example.shopapp.model.User;
import com.example.shopapp.repository.RoleRepository;
import com.example.shopapp.repository.UserRepository;
import com.example.shopapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserRequestDTO userRequestDTO) {
        String phoneNumber = userRequestDTO.getPhoneNumber();
        //kiem tra sdt trung
        if (userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        //chuyen tu userdto sang user
        User user = User.builder()
                .fullname(userRequestDTO.getFullName())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .address(userRequestDTO.getAddress())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .facebookAccountId(userRequestDTO.getFacebookAccountId())
                .googleAccountId(userRequestDTO.getGoogleAccountId())
                .password(userRequestDTO.getPassword())
                .build();
        Role role = roleRepository.findById(userRequestDTO.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals(Role.ADMIN)){
            try {
                throw new AccessDeniedException("AccessDenied");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
        user.setRole(role);

        if(userRequestDTO.getGoogleAccountId() == 0 && userRequestDTO.getFacebookAccountId() ==0){
            String password = userRequestDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            //
        }

        return userRepository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password) {
        //lien quan den security
        //tra ve jwt tokem
        /*return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));*/

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        //check password
        if(user.getGoogleAccountId() == 0 && user.getFacebookAccountId() ==0){
            if(!passwordEncoder.matches(password, user.getPassword())){
                throw new BadCredentialsException("Incorrect phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken  =
                new UsernamePasswordAuthenticationToken(phoneNumber, password, user.getAuthorities());

        //authenticate with java spring security
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return jwtTokenUtil.generateToken(user);
    }
}
