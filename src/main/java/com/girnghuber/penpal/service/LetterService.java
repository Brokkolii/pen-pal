package com.girnghuber.penpal.service;

import com.girnghuber.penpal.entity.Letter;
import com.girnghuber.penpal.repository.LetterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class LetterService {

    private static final Integer DELIVERY_DURATION_HOURS = 0;

    @Inject
    LetterRepository letterRepository;

    public List<Letter> getLettersForUser(String userId) {
        return letterRepository.find("toUserId = ?1 and deliveryDate < ?2", userId, new Date()).stream().toList();
    }

    @Transactional
    public void sendLetter(Letter letter, String fromUserId) {
        letter.fromUserId = fromUserId;
        letter.read = false;
        letter.sentDate = new Date();
        letter.deliveryDate = calcDeliveryDate(letter.sentDate);
        letterRepository.persist(letter);
    }

    @Transactional
    public void markLetterAsRead(Long letterId, String userId) {
        Letter letter = letterRepository.findById(letterId);
        if (letter == null) {
            throw new NotFoundException("Letter not found!");
        }
        if (!Objects.equals(letter.toUserId, userId)) {
            throw new NotAuthorizedException("Letter was not sent to this user!");
        }
        letter.read = true;
    }

    @Transactional
    public void markLetterAsUnread(Long letterId, String userId) {
        Letter letter = letterRepository.findById(letterId);
        if (letter == null) {
            throw new NotFoundException("Letter not found!");
        }
        if (!Objects.equals(letter.toUserId, userId)) {
            throw new NotAuthorizedException("Letter was not sent to this user!");
        }
        letter.read = false;
    }

    public Date calcDeliveryDate(Date sentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sentDate);
        calendar.add(Calendar.HOUR_OF_DAY, DELIVERY_DURATION_HOURS);
        return calendar.getTime();
    }

    public Boolean letterIsOnTheWay(String userId) {
        return letterRepository.find("toUserId = ?1 and deliveryDate > ?2", userId, new Date()).count() > 0;
    }
}
