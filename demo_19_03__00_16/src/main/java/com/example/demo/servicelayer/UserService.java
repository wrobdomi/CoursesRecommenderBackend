package com.example.demo.servicelayer;

import com.example.demo.shared.dto.RatingDto;
import com.example.demo.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);
    UserDto getUser(String email);
    UserDto getUserByUserId(String userId);
    UserDto updateUser(String userId, UserDto user);
    List<RatingDto> getUsersRatings(String userId);
    void deleteUser(String userId);
    List<UserDto> getUsers(int page, int limit);

}
