package api

import (
	"database/sql"
	"net/http"
	"pen-pal/util"

	"github.com/gin-gonic/gin"
)

// Load initializes a new Config struct with values from environment variables
func HandleRoutes(router *gin.Engine, db *sql.DB) {
	router.GET("/letters/:user_id", func(c *gin.Context) {
		user_id := c.Param("user_id")
		letters := util.GetLetters(db, user_id)
		c.JSON(http.StatusOK, letters)
	})
}
