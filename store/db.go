package store

import (
	"database/sql"
	"log"
)

// Load initializes a new Config struct with values from environment variables
func Init(ConnectionString string) *sql.DB {

	db, err := sql.Open("postgres", ConnectionString)
	if err != nil {
		log.Fatal(err)
	}

	// Test the database connection
	err = db.Ping()
	if err != nil {
		log.Fatal("Error connecting to the database: ", err)
	}

	return db
}
