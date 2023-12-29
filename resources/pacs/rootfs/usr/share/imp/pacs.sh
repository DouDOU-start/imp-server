#!/bin/bash

if [[ "$1" == "--help"  ]]; then
    echo -e "Usage: $0"
    echo
    echo -e "Supported environment variables: ."
    echo
    exit 1
fi

mainClass="cn.hanglok.pacs.ImpApplication"
cp=/etc/imp/pacs/dependency/*:/etc/imp/pacs/pacs-service.jar
SPRING_CONFIG_PARAM=-Dspring.config.location=classpath:application.yml,/etc/imp/pacs/pacs.yml
logging_config=/etc/imp/pacs/logback.xml

# if there is a logging config file in lib folder use it (running from source)
if [ -f $logging_config ]; then
    LOGGING_CONFIG_PARAM="-Dlogging.config=$logging_config"
fi

if [ -z "$PACS_MAX_MEMORY" ]; then PACS_MAX_MEMORY=3072m; fi

exec java -Xmx$PACS_MAX_MEMORY -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp -Djdk.tls.ephemeralDHKeySize=2048 $SPRING_CONFIG_PARAM $LOGGING_CONFIG_PARAM -cp $cp $mainClass $@