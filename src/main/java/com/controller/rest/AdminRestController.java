package com.controller.rest;
/*
200 OK — успешный запрос.
201 Created — ресурс создан.
400 Bad Request — ошибка в запросе.
404 Not Found — ресурс не найден.
500 Internal Server Error — ошибка на сервере.
*/
import com.DTO.UserCreateDTO;
import com.DTO.UserReadDTO;
import com.DTO.UserUpdateDTO;
import com.model.Role;
import com.service.DTO.UserDTOService;
import com.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final UserDTOService userService;
    private final RoleService roleService;

    public AdminRestController(UserDTOService userService, RoleService roleService) {
        this.roleService = roleService;
        logger.info("hello from AdminController");
        this.userService = userService;
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserReadDTO> getCurrentUser(Authentication authentication) {
        logger.info("getCurrentUser called in AdminRestController");
        return ResponseEntity.ok(userService.findByEmail(authentication.getName()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserReadDTO>> getUsers() {
        logger.info("getUsers called in AdminRest Controller");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserReadDTO> getUserById(@PathVariable Long id) {
        logger.info("findUserById called in AdminRestController");
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/users")
    public ResponseEntity<UserReadDTO> createUser(@RequestBody UserCreateDTO user) {
        logger.info("addUser called in AdminRestController");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserReadDTO> updateUser(@PathVariable Long id,
                                                  @RequestBody UserUpdateDTO user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("deleteUser called in AdminRestController");
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        logger.info("getAllRoles called in AdminRestController");
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}