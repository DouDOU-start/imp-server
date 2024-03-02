package main

import (
	"encoding/json"
	"fmt"
	"github.com/sirupsen/logrus"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"net/http"
)

type Event struct {
	Type      string `json:"type"`
	OccurAt   int64  `json:"occur_at"`
	Operator  string `json:"operator"`
	EventData struct {
		Resources []struct {
			Digest      string `json:"digest"`
			Tag         string `json:"tag"`
			ResourceUrl string `json:"resource_url"`
		} `json:"resources"`
		Repository struct {
			DateCreated  json.Number `json:"date_created"`
			Name         string      `json:"name"`
			Namespace    string      `json:"namespace"`
			RepoFullName string      `json:"repo_full_name"`
			RepoType     string      `json:"repo_type"`
		} `json:"repository"`
	} `json:"event_data"`
}

type Images struct {
	ID           uint        `gorm:"primarykey"`
	RepoFullName string      `gorm:"size:50"`
	Tag          string      `gorm:"size:30"`
	Namespace    string      `gorm:"size:30"`
	Name         string      `gorm:"size:30"`
	ResourceUrl  string      `gorm:"size:100;uniqueIndex:images_unique"`
	Digest       string      `gorm:"size:255"`
	RepoType     string      `gorm:"type:char(255)"`
	DateCreate   json.Number `gorm:"type:char(20)"`
}

var db *gorm.DB

func initDB() {
	var err error
	dsn := "root:hanglok8888@tcp(10.8.6.34:3366)/algorithm_scheduling_dev?charset=utf8mb4&parseTime=True&loc=Local"
	db, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		logrus.Fatal("Failed to connect database")
	}

	err = db.AutoMigrate(&Images{})
	if err != nil {
		logrus.Fatal("Failed to auto migrate: ", err)
	}
}

func webhookHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != "POST" {
		http.Error(w, "Unsupported method", http.StatusMethodNotAllowed)
		logrus.Error("Unsupported method...")
		return
	}

	var event Event
	decoder := json.NewDecoder(r.Body)
	if err := decoder.Decode(&event); err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		logrus.Error(err.Error())
		return
	}
	defer r.Body.Close()

	logrus.WithField("event", event).Info("recevied webhook")

	switch event.Type {
	case "PUSH_ARTIFACT":
		var images []Images
		result := db.Where("resource_url = ?", event.EventData.Resources[0].ResourceUrl).Find(&images)
		if result.Error != nil {
			logrus.Error("query failed: ", result.Error)
		}

		image := Images{
			Namespace:    event.EventData.Repository.Namespace,
			Name:         event.EventData.Repository.Name,
			RepoFullName: event.EventData.Repository.RepoFullName,
			Tag:          event.EventData.Resources[0].Tag,
			ResourceUrl:  event.EventData.Resources[0].ResourceUrl,
			Digest:       event.EventData.Resources[0].Digest,
			RepoType:     event.EventData.Repository.RepoType,
			DateCreate:   event.EventData.Repository.DateCreated,
			}

		if len(images) == 0 {
			result = db.Create(&image)
			if result.Error != nil {
				logrus.Error("Error while creating record: ", result.Error)
			} else {
				logrus.Info("Record created successfully: ", event.EventData.Resources[0].ResourceUrl)
			}
		} else {
			result = db.Model(&images[0]).Updates(image)
			logrus.Info("Record updated successfully: ", event.EventData.Resources[0].ResourceUrl)
		}

	case "DELETE_ARTIFACT":
		db.Where("resource_url = ?", event.EventData.Resources[0].ResourceUrl).Delete(Images{})
		logrus.Info("Record deleted successfully: ", event.EventData.Resources[0].ResourceUrl)
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintln(w, "Webhook received and processed")
}

func main() {

	logrus.SetLevel(logrus.InfoLevel)
	logrus.SetFormatter(&logrus.JSONFormatter{})

//	file, err := os.OpenFile("webhook.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
//	if err == nil {
//		logrus.SetOutput(file)
//	} else {
//		logrus.Error("Unable to open log file, using default stderr")
//	}

	initDB()
	http.HandleFunc("/webhook", webhookHandler)
	logrus.Fatal(http.ListenAndServe(":8080", nil))

}
