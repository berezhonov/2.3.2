package com.service;

import com.model.User;
import com.model.Role;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> findAll();

    void save(User user);

    void update(Long id, User user, String password, List<String> roleNames);

    void delete(Long id);

    User findById(Long id);

    void saveUserWithRoles(User user, String password, List<String> roleNames);

    List<Role> getAllRoles();

    Map<String, Object> makeAllUserModelAttributes(Long userId);

    User findUserByEmail(String email);
}