package com.service.DTO;

import com.DTO.UserCreateDTO;
import com.DTO.UserReadDTO;
import com.DTO.UserUpdateDTO;
import com.model.Role;
import com.model.User;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDTOServiceImpl implements UserDTOService, UserDetailsService {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDTOServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        logger.info("hello from UserServiceImpl");
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserReadDTO> findAll() {
        logger.info("findAll was called in UserServiceImpl");
        return userRepository.findAll().stream()
                .map(UserReadDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserReadDTO findById(Long id) {
        logger.info("findById was called in UserServiceImpl");
        return UserReadDTO.from(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    @Override
    public UserReadDTO create(UserCreateDTO userDTO) {
        logger.info("create was called in UserServiceImpl");
        User user = new User();
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        user.setRoles(getRolesSetFromRoleNamesList(userDTO.getRoles()));
        return UserReadDTO.from(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        logger.info("delete was called in UserServiceImpl");
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("loadUserByUsername was called in UserServiceImpl");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warning("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public List<Role> getAllRoles() {
        logger.info("getAllRoles was called in UserServiceImpl");
        return roleRepository.findAll();
    }

    @Override
    public UserReadDTO findByEmail(String email) {
        logger.info("findByEmail was called in UserServiceImpl");
        if (email == null || email.isEmpty()) {
            logger.warning(this.getClass().getName() + ", findByEmail: Current user not found - Email is null or empty");
            throw new UsernameNotFoundException("Current user not found - Email is null or empty");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warning(this.getClass().getName() + ", findByEmail: User is null");
            throw new UsernameNotFoundException("User not found");
        }
        return UserReadDTO.from(user);
    }

    @Override
    public UserReadDTO update(Long id, UserUpdateDTO userDTO) {
        logger.info("update was called in UserServiceImpl");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userDTO.getFirstname() != null) {
            user.setFirstname(userDTO.getFirstname());
        }
        if (userDTO.getLastname() != null) {
            user.setLastname(userDTO.getLastname());
        }
        if (userDTO.getAge() != null) {
            user.setAge(userDTO.getAge());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                logger.warning(this.getClass().getName() + ", update: Email already exists");
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setRoles(getRolesSetFromRoleNamesList(userDTO.getRoles()));
        return UserReadDTO.from(userRepository.save(user));
    }

    private Set<Role> getRolesSetFromRoleNamesList(List<String> roleNameList) {
        Set<Role> roles;
        if (roleNameList == null || roleNameList.isEmpty()) {
            logger.warning(this.getClass().getName() + ": roleNameList is null or empty");
            logger.info(this.getClass().getName() + ": add a default roles");
            roles = new HashSet<>();
            roles.add(roleRepository.findByName(Role.defaultRoleName));
        } else {
            roles = roleNameList
                    .stream()
                    .map(roleRepository::findByName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
        return roles;
    }
}