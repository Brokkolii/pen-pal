package model

type Letter struct {
	LetterID int    `json:"letter_id"`
	FromUser int    `json:"from_user"`
	Message  string `json:"message"`
}
