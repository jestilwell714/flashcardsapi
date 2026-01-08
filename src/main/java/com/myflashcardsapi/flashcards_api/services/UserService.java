package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.UserDto;
import org.apache.coyote.BadRequestException;

import java.util.Optional;

public interface UserService {
    UserDto createUser(UserDto userDto) throws BadRequestException;
    Optional<UserDto> getUserById(Long userId);
    boolean exists(Long userId);
}
