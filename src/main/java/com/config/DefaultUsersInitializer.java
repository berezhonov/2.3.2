package com.config;

import com.DTO.UserCreateDTO;
import com.service.DTO.UserDTOService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.model.Role;
import com.service.RoleService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DefaultUsersInitializer {
    private final UserDTOService userService;
    private final RoleService roleService;

    public DefaultUsersInitializer(
            @Lazy
            UserDTOService userService,
            RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        try {
            if (userService.findAll().isEmpty()) {
                Role roleAdmin = new Role("ROLE_ADMIN");
                Role roleUser = new Role("ROLE_USER");

                roleService.saveRole(roleAdmin);
                roleService.saveRole(roleUser);

                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(roleAdmin);

                UserCreateDTO admin = new UserCreateDTO("admin",
                        "admin",
                        "admin@mail.com",
                        20,
                        "admin",
                        adminRoles);

                Set<Role> userRoles = new HashSet<>();
                userRoles.add(roleUser);

                UserCreateDTO user = new UserCreateDTO(
                        "user",
                        "user",
                        "user@mail.com",
                        30,
                        "user",
                        userRoles);

                userService.create(admin);
                userService.create(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}