package com.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.model.Role;
import com.model.User;

import java.util.List;
import java.util.stream.Collectors;


public class UserReadDTO {
    private final Long id;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final Integer age;
    private final List<String> roles;

    @JsonCreator
    public UserReadDTO(@JsonProperty("id") Long id,
                       @JsonProperty("firstname") String firstname,
                       @JsonProperty("lastname") String lastname,
                       @JsonProperty("email") String email,
                       @JsonProperty("age") Integer age,
                       @JsonProperty("roles") List<String> roles) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public List<String> getRoles() {
        return roles;
    }

    public static UserReadDTO from(User user) {
        return new UserReadDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getAge(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        );
    }
}
