package main

import (
	"pen-pal/api"
	"pen-pal/config"
	"pen-pal/store"
)

func main() {

	config := config.Load()

	router := api.Create()

	db := store.Init(config.ConnectionString)
	defer db.Close()

	api.HandleRoutes(router, db)

	router.Run(":" + config.Port)
}
