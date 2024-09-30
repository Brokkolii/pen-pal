package com.girnghuber.penpal.dto;

import java.util.Date;

public class LetterDto {
    public Long id;
    public String fromUserName;
    public String toUserId;
    public String message;
    public Date sentDate;
    public Boolean read;
}
