package com.girnghuber.penpal.repository;

import com.girnghuber.penpal.entity.Letter;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LetterRepository implements PanacheRepository<Letter> {
}
