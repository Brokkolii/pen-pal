package util

import (
	"database/sql"
	"log"
	"pen-pal/model"
)

// Function to get letters from the database
func GetLetters(db *sql.DB, userID string) []model.Letter {
	var letters []model.Letter
	rows, err := db.Query("SELECT letter_id, from_user, to_user, message, sent_date, delivered_date, read FROM letters WHERE to_user = $1", userID)
	if err != nil {
		return nil
	}
	defer rows.Close()

	for rows.Next() {
		var letter model.Letter
		rows.Scan(&letter.LetterID, &letter.FromUser, &letter.ToUser, &letter.Message, &letter.SentDate, &letter.DeliveredDate, &letter.Read)
		letters = append(letters, letter)
	}
	return letters
}

func SendLetter(db *sql.DB, letter model.Letter) error {

	// Prepare the SQL statement for execution.
	stmt, err := db.Prepare("INSERT INTO letters (from_user, to_user, message, sent_date, delivered_date) VALUES ($1, $2, $3, $4, $5)")
	if err != nil {
		log.Printf("Error preparing statement: %v", err)
	}
	defer stmt.Close()

	// Execute the SQL statement with the provided parameters.
	_, err = stmt.Exec(letter.FromUser, letter.ToUser, letter.Message, letter.SentDate, letter.DeliveredDate)
	if err != nil {
		log.Printf("Error executing statement: %v", err)
	}

	return nil
}
