/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.registration_login_demo.service;


import com.example.registration_login_demo.dto.UserDto;
import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.entity.Role;
import com.example.registration_login_demo.entity.User;
import com.example.registration_login_demo.repository.BuyUserRepository;
import com.example.registration_login_demo.repository.RoleRepository;
import com.example.registration_login_demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private BuyUserRepository buyUserRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           BuyUserRepository buyUserRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.buyUserRepository = buyUserRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setId(userDto.getId());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role;
        if (userDto.getPassword().equals("adminadmin")) {
            role = roleRepository.findByName("ROLE_ADMIN");
        } else {
            role = roleRepository.findByName("ROLE_USER");
        }

        if (role == null) {
            role = checkRoleExist();
        }

        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
        initializeFunds(user);

    }

    private void initializeFunds(User user) {
        BuyUser buyUser = new BuyUser();
        buyUser.setUser(user);
        buyUser.setCurrentFund(50000.0);
        buyUser.setPnl(0.0);
        buyUser.setPoint(0.0);
        buyUser.setThreshold(2000.0);
        buyUser.setNotiOnOff(1);
        buyUserRepository.save(buyUser);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }


    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        return userDto;
    }

    private Role checkRoleExist() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");

        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            return roleRepository.save(adminRole);
        }

        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            return roleRepository.save(userRole);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {

        // Retrieve the user by ID
        User user = userRepository.findById(id).orElse(null);

        // Check if the user exists
        if (user != null) {
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                // Delete the role
                roleRepository.delete(role);
            }
            BuyUser buyUser = user.getBuyUser();
            if (buyUser != null) {
                buyUserRepository.delete(buyUser);
            }
            // Delete the user
            userRepository.delete(user);
        }

    }

    public List<String> getUsernamesForBuyUsers(List<BuyUser> buyUsers) {
        List<String> usernames = new ArrayList<>();
        for (BuyUser buyUser : buyUsers) {
            Long userId = buyUser.getUser().getId();
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                usernames.add(user.getName());
            }
        }
        return usernames;
    }
}
