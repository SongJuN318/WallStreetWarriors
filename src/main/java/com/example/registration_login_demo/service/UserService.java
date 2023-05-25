/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.registration_login_demo.service;


import com.example.registration_login_demo.dto.UserDto;
import com.example.registration_login_demo.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);



    List<UserDto> findAllUsers();

    void deleteUser(Long id);
}
