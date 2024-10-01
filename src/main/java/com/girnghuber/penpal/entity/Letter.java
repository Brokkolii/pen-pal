package com.girnghuber.penpal.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "letters")
public class Letter extends PanacheEntity {
    public String fromUserId;
    public String fromUserName;
    public String toUserId;
    public String message;
    public Date sentDate;
    public Date deliveryDate;
    public Boolean read;
}
