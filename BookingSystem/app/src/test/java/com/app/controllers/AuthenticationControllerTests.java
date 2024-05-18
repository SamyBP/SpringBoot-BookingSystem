package com.app.controllers;


import com.app.dtos.SignInRequestDto;
import com.app.dtos.SignInResponseDto;
import com.app.dtos.SignUpRequestDto;
import com.app.handlers.InvalidPasswordException;
import com.app.handlers.InvalidUsernameException;
import com.app.handlers.UsernameAlreadyTakenException;
import com.app.models.User;
import com.app.services.AuthenticationService;
import com.app.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTests {
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private SignUpRequestDto signUpRequestDto;
    private User user;
    private SignInRequestDto signInRequestDto;
    private SignInResponseDto signInResponseDto;

    @BeforeEach
    public void init() {
        signUpRequestDto = SignUpRequestDto.builder().name("test_name").username("test_username").password("test_pswd").build();
        user = User.builder().name("test_name").id(1L).username("test_username").password("test_pswd").build();
        signInRequestDto = SignInRequestDto.builder().username("test_name").password("test_pswd").build();
        signInResponseDto = SignInResponseDto.builder().isSignInSuccessful(true).token("token").build();
    }

    @Test
    public void AuthenticationController_Register_ReturnsUser() throws Exception {
        when(authenticationService.register(signUpRequestDto))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user.getUsername()));

        verify(authenticationService, times(1)).register(signUpRequestDto);
    }

    @Test
    public void AuthenticationController_Login_ReturnsLoginResponseDto() throws Exception {
        when(authenticationService.authenticate(signInRequestDto))
                .thenReturn(user);
        when(jwtService.generateToken(anyString(), anyLong()))
                .thenReturn("token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.signInSuccessful").value(signInResponseDto.isSignInSuccessful()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(signInResponseDto.getToken()));

    }

    @Test
    public void AuthenticationController_Login_ReturnsBadRequestForInvalidUsername() throws Exception {
        when(authenticationService.authenticate(signInRequestDto))
                .thenThrow(InvalidUsernameException.class);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signInRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void AuthenticationController_Login_ReturnsBadRequestForInvalidPassword() throws Exception {
        when(authenticationService.authenticate(signInRequestDto))
                .thenThrow(InvalidPasswordException.class);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequestDto)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void AuthenticationController_Register_ReturnsBadRequestForUsernameAlreadyTaken() throws Exception {
        when(authenticationService.register(signUpRequestDto))
                .thenThrow(UsernameAlreadyTakenException.class);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
