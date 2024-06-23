package org.example.file_storage_system.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {

    private String email;
    private String password;
}
