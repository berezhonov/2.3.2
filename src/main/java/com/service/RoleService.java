package com.service;

import com.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();

    void saveRole(Role role);

    Role findRoleByName(String roleName);

    Set<Role> getRolesSetByUserName(Set<Role> userRoles, List<String> roleNames);
}