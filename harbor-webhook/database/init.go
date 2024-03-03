package database

import (
	"github.com/sirupsen/logrus"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

var Db *gorm.DB

func InitDB() {
	var err error
	dsn := "root:hanglok8888@tcp(10.8.6.34:3366)/algorithm_scheduling_dev?charset=utf8mb4&parseTime=True&loc=Local"
	Db, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		logrus.Fatal("Failed to connect database")
	}

	err = Db.AutoMigrate(&Images{}, &Assemble{})
	if err != nil {
		logrus.Fatal("Failed to auto migrate: ", err)
	}
}
