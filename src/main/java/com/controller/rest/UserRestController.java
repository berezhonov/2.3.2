package com.controller.rest;

import com.DTO.UserReadDTO;
import com.service.DTO.UserDTOService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
public class UserRestController {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final UserDTOService userService;

    public UserRestController(UserDTOService userService) {
        this.userService = userService;
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserReadDTO> getCurrentUser(Authentication authentication) {
        logger.info("Request for current user by email: " + authentication.getName());
        return ResponseEntity.ok(userService.findByEmail(authentication.getName()));
    }
}