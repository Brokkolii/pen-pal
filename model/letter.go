package model

import "time"

type Letter struct {
	LetterID      int       `json:"letter_id"`
	FromUser      int       `json:"from_user"`
	ToUser        int       `json:"to_user"`
	Message       string    `json:"message"`
	SentDate      time.Time `json:"sent_date"`
	DeliveredDate time.Time `json:"delivered_date"`
	Read          bool      `json:"read"`
}

type LetterSendRequest struct {
	FromUser int    `json:"from_user"`
	ToUser   int    `json:"to_user"`
	Message  string `json:"message"`
}
