package main

import (
	"harbor-webhook/database"
	"harbor-webhook/log"
	"harbor-webhook/webhook"
)

func main() {
	log.Init()
	database.InitDB()
	webhook.Start()
}
