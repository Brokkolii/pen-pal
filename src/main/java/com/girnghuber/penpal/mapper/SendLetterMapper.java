package com.girnghuber.penpal.mapper;

import com.girnghuber.penpal.dto.SendLetterDto;
import com.girnghuber.penpal.entity.Letter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface SendLetterMapper {
    SendLetterMapper INSTANCE = Mappers.getMapper( SendLetterMapper.class );

    // Temporary Mapping till i get data from db
    @Mapping(source = "fromUserId", target = "fromUserName")
    @Mapping(source = "toUserId", target = "toUserName")
    Letter sendLetterDtoToLetter(SendLetterDto sendLetterDto);
}
