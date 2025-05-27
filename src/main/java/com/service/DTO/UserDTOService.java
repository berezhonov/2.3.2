package com.service.DTO;

import com.DTO.UserCreateDTO;
import com.DTO.UserReadDTO;
import com.DTO.UserUpdateDTO;
import com.model.Role;

import java.util.List;

public interface UserDTOService {
    List<UserReadDTO> findAll();
    UserReadDTO findById(Long id);
    UserReadDTO create(UserCreateDTO userDTO);
    UserReadDTO update(Long id, UserUpdateDTO userDTO);
    void delete(Long id);
    UserReadDTO findByEmail(String email);
    List<Role> getAllRoles();
}