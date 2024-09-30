package com.girnghuber.penpal.mapper;

import com.girnghuber.penpal.dto.LetterDto;
import com.girnghuber.penpal.dto.SendLetterDto;
import com.girnghuber.penpal.entity.Letter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LetterMapper {
    LetterDto letterToLetterDto(Letter letter);
    Letter sendLetterDtoToLetter(SendLetterDto sendLetterDto);
}
