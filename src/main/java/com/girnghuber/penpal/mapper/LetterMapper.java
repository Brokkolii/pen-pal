package com.girnghuber.penpal.mapper;

import com.girnghuber.penpal.dto.LetterDto;
import com.girnghuber.penpal.entity.Letter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface LetterMapper {
    LetterMapper INSTANCE = Mappers.getMapper( LetterMapper.class );
    LetterDto letterToLetterDto(Letter letter);
    Letter letterDtoToLetter(LetterDto letterDto);
}
