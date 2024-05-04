package store

import (
	"database/sql"
	"log"

	_ "github.com/lib/pq" // Import the driver anonymously; required for initialization
)

// Load initializes a new Config struct with values from environment variables
func Init(ConnectionString string) *sql.DB {

	db, err := sql.Open("postgres", ConnectionString)
	if err != nil {
		log.Fatal("SQL Open ERROR", err)
	}
	// Test the database connection
	err = db.Ping()
	if err != nil {
		log.Fatal("Error connecting to the database: ", err)
	}

	return db
}
