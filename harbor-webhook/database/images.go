package database

import "encoding/json"

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
