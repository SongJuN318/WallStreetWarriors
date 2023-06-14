package com.example.registration_login_demo.service;


import com.example.registration_login_demo.dto.UserDto;
import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.entity.User;

import java.util.List;


public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    User findUserByName(String name);

    User findUserById(long id);

    List<UserDto> findAllUsers();

    void deleteUser(Long id);

    public List<String> getUsernamesForBuyUsers(List<BuyUser> buyUsers);
}
