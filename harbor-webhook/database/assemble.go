package database

import (
	"encoding/json"
)

type Assemble struct {
	ID		uint				`gorm:"primarykey"`
	Name	string    			`gorm:"size:50;uniqueIndex:assembles_unique"`
	Data	json.RawMessage    	`gorm:"type:json"`
}