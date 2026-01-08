package com.myflashcardsapi.flashcards_api.services.impl;

import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.UserDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.UserMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapperImpl userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapperImpl userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) throws BadRequestException {
        User newUser = userMapper.mapFrom(userDto);
        if(userRepository.existsByUsername(newUser.getUsername())) {
            throw new BadRequestException("User with username " + newUser.getUsername() + " already exists");
        }
        User user = userRepository.save(newUser);
        return userMapper.mapTo(user);
    }

    @Override
    public Optional<UserDto> getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(userMapper::mapTo);
    }

    @Override
    public boolean exists(Long userId) {
        return userRepository.existsById(userId);
    }
}
