package com.code_room.offer_service.infrastructure.restclient.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String name;
    private String lastName;
    private String email;
    private String identification;
    private Role role;
}

enum Role {
    ADMIN,
    CLIENT
}

