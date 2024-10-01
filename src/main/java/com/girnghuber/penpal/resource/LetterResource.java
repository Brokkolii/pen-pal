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

import java.util.List;

@Path("/letter")
@Authenticated
public class LetterResource {

    @Inject
    LetterService letterService;
    @Inject
    LetterMapper letterMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/inbox")
    public List<LetterDto> getLetters(@Context SecurityContext securityContext) {
        String userId = securityContext.getUserPrincipal().getName();
        List<Letter> letters = letterService.getLettersForUser(userId);
        return letters.stream().map(letterMapper::letterToLetterDto).toList();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/send")
    public LetterDto sendLetter(SendLetterDto sendLetterDto, @Context SecurityContext securityContext) {
        Letter letter = letterMapper.sendLetterDtoToLetter(sendLetterDto);
        letterService.sendLetter(letter, securityContext.getUserPrincipal().getName());
        return letterMapper.letterToLetterDto(letter);
    }

    @PUT
    @Path("/{letterId}/read")
    public void markLetterAsRead(@PathParam("letterId") Long letterId, @Context SecurityContext securityContext) {
        letterService.markLetterAsRead(letterId, securityContext.getUserPrincipal().getName());
    }

    @PUT
    @Path("/{letterId}/unread")
    public void markLetterAsUnread(@PathParam("letterId") Long letterId, @Context SecurityContext securityContext) {
        letterService.markLetterAsUnread(letterId, securityContext.getUserPrincipal().getName());
    }

    @GET
    @Path("inDelivery")
    public Boolean letterIsOnTheWay(@Context SecurityContext securityContext) {
        return letterService.letterIsOnTheWay(securityContext.getUserPrincipal().getName());
    }
}
