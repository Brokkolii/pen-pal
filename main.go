package main

import (
    "database/sql"
    "fmt"
	"os"
    "log"
    "net/http"

	"github.com/joho/godotenv"
    "github.com/gin-gonic/gin"
    _ "github.com/lib/pq"
)

func main() {
    // Load environment variables from .env file
    if err := godotenv.Load(); err != nil {
	    log.Fatal("Error loading .env file")
    }

    // Initialize the Gin router
    router := gin.Default()

    // Setup connection to PostgreSQL database
	connectionString := fmt.Sprintf(
		"host=%s port=%s user=%s password=%s dbname=%s sslmode=%s",
		os.Getenv("DB_HOST"),
		os.Getenv("DB_PORT"),
		os.Getenv("DB_USER"),
		os.Getenv("DB_PASS"),
		os.Getenv("DB_NAME"),
		os.Getenv("DB_SSLMODE"),
	)

	log.Println(connectionString)

	fmt.Println("Connecting with the following settings:")
	fmt.Println("Host:", os.Getenv("DB_HOST"))
	fmt.Println("User:", os.Getenv("DB_USER"))
	fmt.Println("Password:", os.Getenv("DB_PASS"))
	fmt.Println("Database:", os.Getenv("DB_NAME"))
	fmt.Println("Port:", os.Getenv("DB_PORT"))
	fmt.Println("SSL Mode:", os.Getenv("DB_SSLMODE"))

    db, err := sql.Open("postgres", connectionString)
    if err != nil {
        log.Fatal(err)
    }
    defer db.Close()

    // Test the database connection
    err = db.Ping()	
    if err != nil {
        log.Fatal("Error connecting to the database: ", err)
    }

    // Define the /letters/:user_id endpoint
    router.GET("/letters/:user_id", func(c *gin.Context) {
        user_id := c.Param("user_id")
        letters, err := getLetters(db, user_id)
        if err != nil {
            c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
            return
        }
        c.JSON(http.StatusOK, letters)
    })

    // Run the server
    router.Run(":8080")
}

// Function to get letters from the database
func getLetters(db *sql.DB, userID string) ([]Letter, error) {
    var letters []Letter
    rows, err := db.Query("SELECT letter_id, from_user, message FROM letters WHERE to_user = $1", userID)
    if err != nil {
        return nil, err
    }
    defer rows.Close()

    for rows.Next() {
        var l Letter
        if err := rows.Scan(&l.LetterID, &l.FromUser, &l.Message); err != nil {
            return nil, err
        }
        letters = append(letters, l)
    }
    return letters, nil
}

// Letter represents a letter to a user
type Letter struct {
    LetterID int    `json:"letter_id"`
    FromUser int    `json:"from_user"`
    Message  string `json:"message"`
}