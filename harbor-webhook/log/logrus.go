package log

import "github.com/sirupsen/logrus"

func Init() {
	logrus.SetLevel(logrus.InfoLevel)
	logrus.SetFormatter(&logrus.JSONFormatter{})

	//	file, err := os.OpenFile("webhook.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	//	if err == nil {
	//		logrus.SetOutput(file)
	//	} else {
	//		logrus.Error("Unable to open log file, using default stderr")
	//	}
}
