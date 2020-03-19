package com.example.demo.uilayer.controller;

import com.example.demo.servicelayer.UserService;
import com.example.demo.shared.dto.UserDto;
import com.example.demo.uilayer.model.request.UserDetailsModel;
import com.example.demo.uilayer.model.response.OperationStatusModel;
import com.example.demo.uilayer.model.response.RequestOperationName;
import com.example.demo.uilayer.model.response.RequestOperationStatus;
import com.example.demo.uilayer.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path="/{id}",
                produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
                })
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);

        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public UserRest createUser(@RequestBody UserDetailsModel userDetailsModel) {

        UserRest returnedUser = new UserRest();

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetailsModel, userDto);

        UserDto createdUser = userService.createUser(userDto);

        BeanUtils.copyProperties(createdUser, returnedUser);

        return returnedUser;

    }

    @PutMapping(path="/{id}",
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            },
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsModel userDetailsModel) {

        UserRest returnedUser = new UserRest();

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetailsModel, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);

        BeanUtils.copyProperties(updatedUser, returnedUser);

        return returnedUser;
    }

    @DeleteMapping(path="/{id}",
            produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public OperationStatusModel deleteUser(@PathVariable String id) {

        OperationStatusModel returnValue = new OperationStatusModel();

        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public List<UserRest> getUsers(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "50") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for(UserDto userDto : users){
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }



}
