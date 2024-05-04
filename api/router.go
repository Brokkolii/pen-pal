package api

import (
	"github.com/gin-gonic/gin"
)

// Load initializes a new Config struct with values from environment variables
func Create() *gin.Engine {
	router := gin.Default()
	return router
}
