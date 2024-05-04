package config

import (
	"fmt"
	"os"

	"github.com/joho/godotenv"
)

// Config represents the configuration information needed by the application
type Config struct {
	ConnectionString string
	Port             string
}

// Load initializes a new Config struct with values from environment variables
func Load() *Config {

	godotenv.Load()

	connectionString := fmt.Sprintf(
		"host=%s port=%s user=%s password=%s dbname=%s sslmode=%s",
		os.Getenv("DB_HOST"),
		os.Getenv("DB_PORT"),
		os.Getenv("DB_USER"),
		os.Getenv("DB_PASS"),
		os.Getenv("DB_NAME"),
		os.Getenv("DB_SSLMODE"),
	)

	port := "8080"

	return &Config{
		ConnectionString: connectionString,
		Port:             port,
	}
}
