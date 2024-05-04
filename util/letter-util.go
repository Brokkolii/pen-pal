package util

import (
	"database/sql"
	"log"
	"pen-pal/model"
)

// Function to get letters from the database
func GetLetters(db *sql.DB, userID string) []model.Letter {
	var letters []model.Letter
	rows, err := db.Query("SELECT letter_id, from_user, message FROM letters WHERE to_user = $1", userID)
	if err != nil {
		log.Println("error select letters", userID, rows)
		return nil
	}
	defer rows.Close()

	for rows.Next() {
		var letter model.Letter
		rows.Scan(&letter.LetterID, &letter.FromUser, &letter.Message)
		letters = append(letters, letter)
	}
	return letters
}
