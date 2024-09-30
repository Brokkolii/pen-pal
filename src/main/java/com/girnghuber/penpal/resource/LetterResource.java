package com.girnghuber.penpal.resource;

import com.girnghuber.penpal.dto.LetterDto;
import com.girnghuber.penpal.dto.SendLetterDto;
import com.girnghuber.penpal.entity.Letter;
import com.girnghuber.penpal.mapper.LetterMapper;
import com.girnghuber.penpal.service.LetterService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Date;
import java.util.List;

@Path("/letter")
@Authenticated
public class LetterResource {

    @Inject
    LetterService letterService;
    @Inject
    LetterMapper letterMapper;
    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/inbox")
    public List<LetterDto> getLetters() {
        String userId = securityContext.getUserPrincipal().getName();
        List<Letter> letters = letterService.getLettersForUser(userId);
        return letters.stream().map(letterMapper::letterToLetterDto).toList();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/send")
    public LetterDto sendLetter(SendLetterDto sendLetterDto) {
        Letter letter = letterMapper.sendLetterDtoToLetter(sendLetterDto);
        letter.fromUserId = securityContext.getUserPrincipal().getName();
        letter.read = false;
        letter.sentDate = new Date();
        letterService.sendLetter(letter);
        return letterMapper.letterToLetterDto(letter);
    }
}
