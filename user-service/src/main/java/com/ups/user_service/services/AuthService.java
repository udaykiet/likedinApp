package com.ups.user_service.services;

import com.ups.user_service.dtos.LoginRequestDto;
import com.ups.user_service.dtos.SignupRequestDto;
import com.ups.user_service.dtos.UserDto;
import com.ups.user_service.entities.User;
import com.ups.user_service.event.UserCreatedEvent;
import com.ups.user_service.exceptions.BadRequestException;
import com.ups.user_service.exceptions.ResourceNotFoundException;
import com.ups.user_service.repositories.UserRepository;
import com.ups.user_service.utils.PasswordUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private KafkaTemplate<Long , UserCreatedEvent> kafkaTemplate;

    public UserDto signup(SignupRequestDto signupRequestDto) {

        boolean userExists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(userExists) throw new BadRequestException("user with email: " + signupRequestDto.getEmail() + " already exists");

        User user = modelMapper.map(signupRequestDto , User.class);


        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        User savedUser = userRepository.save(user);

        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .build();
        kafkaTemplate.send("user-created-topic" , userCreatedEvent);

        return modelMapper.map(savedUser , UserDto.class);
    }


    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("email does not exists: "+ loginRequestDto.getEmail()));

        boolean passwordMatched = PasswordUtil.checkPassword(loginRequestDto.getPassword() , user.getPassword());
        if(!passwordMatched) throw new BadRequestException("password is incorrect");
        return jwtService.generateAccessToken(user);
    }
}
