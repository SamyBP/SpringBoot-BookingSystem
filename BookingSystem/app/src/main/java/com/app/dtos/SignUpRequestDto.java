package com.app.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class SignUpRequestDto {
    private String username;
    private String password;
    private String name;
}
