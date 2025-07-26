package com.myflashcardsapi.flashcards_api.mappers.impl;

import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.UserDto;
import com.myflashcardsapi.flashcards_api.mappers.Mapper;
import org.modelmapper.ModelMapper;

/**
 * Implementation class that implements Mapper and maps User and UserDto objects
 */
public class UserMapperImpl implements Mapper<UserDto, User> {

    private ModelMapper modelMapper;

    /**
     * We want to map a UserDto object to a User
     * @param user
     * @return the mapped User object
     */
    @Override
    public UserDto mapTo(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    /**
     * We want to map a UserDto object to a User
     * @param userDto
     * @return the mapped User object
     */
    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
