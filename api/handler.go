package api

import (
	"database/sql"
	"log"
	"net/http"
	"pen-pal/model"
	"pen-pal/util"
	"time"

	"github.com/gin-gonic/gin"
)

// Load initializes a new Config struct with values from environment variables
func HandleRoutes(router *gin.Engine, db *sql.DB) {

	router.POST("/letter/send", func(c *gin.Context) {
		var req model.LetterSendRequest
		if err := c.BindJSON(&req); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		letter := model.Letter{
			FromUser:      req.FromUser,
			ToUser:        req.ToUser,
			Message:       req.Message,
			SentDate:      time.Now(),
			DeliveredDate: time.Now().Add(72 * time.Hour), // 3 days from now
			Read:          false,
		}

		log.Print(letter.SentDate)
		log.Print(letter.DeliveredDate)

		util.SendLetter(db, letter)
		c.JSON(http.StatusOK, gin.H{"status": "success", "message": "Letter sent successfully."})
	})

	router.GET("/letters/:user_id", func(c *gin.Context) {
		user_id := c.Param("user_id")
		letters := util.GetLetters(db, user_id)
		c.JSON(http.StatusOK, letters)
	})
}
