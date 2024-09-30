package com.girnghuber.penpal.service;

import com.girnghuber.penpal.entity.Letter;
import com.girnghuber.penpal.mapper.LetterMapper;
import com.girnghuber.penpal.repository.LetterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LetterService {

    @Inject
    LetterRepository letterRepository;

    public List<Letter> getLettersForUser(String userId) {
        //return letterRepository.listAll();
        return letterRepository.find("toUserId", userId).stream().toList();
    }

    @Transactional
    public Letter sendLetter(Letter letter) {
        letterRepository.persist(letter);
        return letter;
    }
}
