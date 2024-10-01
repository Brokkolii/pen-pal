package com.girnghuber.penpal.service;

import com.girnghuber.penpal.entity.Letter;
import com.girnghuber.penpal.repository.LetterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class LetterService {

    @Inject
    LetterRepository letterRepository;

    public List<Letter> getLettersForUser(String userId) {
        return letterRepository.find("toUserId = ?1 and deliveryDate < ?2", userId, new Date()).stream().toList();
    }

    @Transactional
    public void sendLetter(Letter letter) {
        letterRepository.persist(letter);
    }

    public Date calcDeliveryDate(Date sentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sentDate);
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        return calendar.getTime();
    }
}
