package com.example.demo.servicelayer.impl;

import com.example.demo.datalayer.entity.UserCourseRatingEntity;
import com.example.demo.datalayer.entity.UserEntity;
import com.example.demo.datalayer.repository.UserRepoitory;
import com.example.demo.servicelayer.UserService;
import com.example.demo.shared.Utils;
import com.example.demo.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepoitory userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        if(userRepository.findByEmail(userDto.getEmail()) != null)
            throw new RuntimeException("Record already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        String publicUserId = utils.generatePublicUserId(15);
        userEntity.setUserId(publicUserId);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnedValue);

        return returnedValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email){
        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(userId);

        System.out.println("USERS RATINGS ARE: ");
        for(UserCourseRatingEntity ucre : userEntity.getUsersRatings()) {
            System.out.println(ucre.getRating());
        }

        if(userEntity == null) throw new UsernameNotFoundException(userId);

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UsernameNotFoundException(userId);

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {

        if(page > 0){
            page -= 1;
        }

        List<UserDto> returnValue = new ArrayList<>();

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);

        List<UserEntity> users = usersPage.getContent();

        for(UserEntity ue : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(ue, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {

        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UsernameNotFoundException(userId);

        // update logic can be put here...

        UserEntity updatedUserDetails = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }


}
