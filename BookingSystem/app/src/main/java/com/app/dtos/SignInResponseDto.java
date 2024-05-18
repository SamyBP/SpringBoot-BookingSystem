package com.app.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class SignInResponseDto {
    private boolean isSignInSuccessful;
    private String token;
}
