package webhook

import (
	"encoding/json"
	"fmt"
	"github.com/sirupsen/logrus"
	"harbor-webhook/database"
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
		var images []database.Images
		result := database.Db.Where("resource_url = ?", event.EventData.Resources[0].ResourceUrl).Find(&images)
		if result.Error != nil {
			logrus.Error("query failed: ", result.Error)
		}

		image := database.Images{
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
			result = database.Db.Create(&image)
			if result.Error != nil {
				logrus.Error("Error while creating record: ", result.Error)
			} else {
				logrus.Info("Record created successfully: ", event.EventData.Resources[0].ResourceUrl)
			}
		} else {
			result = database.Db.Model(&images[0]).Updates(image)
			logrus.Info("Record updated successfully: ", event.EventData.Resources[0].ResourceUrl)
		}

	case "DELETE_ARTIFACT":
		database.Db.Where("resource_url = ?", event.EventData.Resources[0].ResourceUrl).Delete(database.Images{})
		logrus.Info("Record deleted successfully: ", event.EventData.Resources[0].ResourceUrl)
	}

	w.WriteHeader(http.StatusOK)
	fmt.Fprintln(w, "Webhook received and processed")
}

func Start() {
	http.HandleFunc("/webhook", webhookHandler)
	logrus.Fatal(http.ListenAndServe(":8080", nil))
}
