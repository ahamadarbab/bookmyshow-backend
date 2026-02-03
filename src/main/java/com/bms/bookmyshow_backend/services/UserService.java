package com.bms.bookmyshow_backend.services;

import com.bms.bookmyshow_backend.dto.RegisterUserDto;
import com.bms.bookmyshow_backend.models.User;
import com.bms.bookmyshow_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterUserDto registerUserDto) {
        // We got the object of registerUserDto, we need to map it to user object
        // After mapping it to userObject we will be calling userRepository to save that user object in the user table
        User user = new User();
        user.setUserName(registerUserDto.getUserName());
        user.setPassword(registerUserDto.getPassword());
        user.setEmail(registerUserDto.getEmail());
        user.setPhoneNumber(registerUserDto.getPhoneNumber());
        user.setAddress(registerUserDto.getAddress());
        user.setPincode(registerUserDto.getPincode());
        user.setCity(registerUserDto.getCity());
        user.setState(registerUserDto.getState());
        user.setCountry(registerUserDto.getCountry());
        user.setUserType(registerUserDto.getUserType());

        // we need to take this user object and save it inside the user table.
        return userRepository.save(user);
    }
}
