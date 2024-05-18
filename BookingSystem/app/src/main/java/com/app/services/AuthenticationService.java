package com.app.services;

import com.app.dtos.SignInRequestDto;
import com.app.dtos.SignUpRequestDto;
import com.app.handlers.InvalidPasswordException;
import com.app.handlers.InvalidUsernameException;
import com.app.handlers.UsernameAlreadyTakenException;
import com.app.mappers.IUserMapper;
import com.app.models.User;
import com.app.repositories.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    @Autowired
    public AuthenticationService(IUserRepository userRepository, IUserMapper userMapper) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public User authenticate(SignInRequestDto signInRequestDto) {
        User user = userRepository.findByUsername(signInRequestDto.getUsername())
                .orElseThrow(() -> new InvalidUsernameException("Wrong username!"));
        if (!BCrypt.checkpw(signInRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Wrong password!");
        }

        return user;
    }

    public User register(SignUpRequestDto signUpRequestDto) {
        if (userRepository.findByUsername(signUpRequestDto.getUsername()).isPresent())
            throw new UsernameAlreadyTakenException("Username is already taken");

        User newUser = userMapper.createMap(signUpRequestDto, User.class);
        newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
        return userRepository.save(newUser);
    }

}
