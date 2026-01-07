package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    boolean exists(Long userId);
}
