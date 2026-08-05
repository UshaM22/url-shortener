package com.urlshortener.service;

import com.urlshortener.JwtUtil;
import com.urlshortener.dto.LoginRegistrationRequest;
import com.urlshortener.exception.InvalidCredentialException;
import com.urlshortener.model.User;
import com.urlshortener.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(LoginRegistrationRequest request){
        String userName = request.getUserName();
        String password = request.getPassword();

        boolean userNameExists;

        userNameExists = userDetailRepository.existsByUserName(userName);

        if(userNameExists){
            return "User Already Exists, Please Login!!";

        }
        User user = new User();
        user.setUserName(userName);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userDetailRepository.save(user);

        return "User created successfully";

    }

    public String loginUser(LoginRegistrationRequest request){
        String userName = request.getUserName();
        String password = request.getPassword();

        User user = userDetailRepository.findByUserName(userName).orElseThrow(()-> new InvalidCredentialException("Username or Password is Wrong"));
        if(!passwordEncoder.matches(password, user.getPassword())){
            return "Entered wrong password";
        }

        return jwtUtil.generateToken(userName);
    }


}
