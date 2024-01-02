package com.gustavo.backend.userapp.backenduserapp.model.Dto;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserDto
{
    private Long id;
    private String username;
    private String email;
}
