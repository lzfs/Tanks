#!/bin/bash

# export JAVA_OPTS="-Duser.language=fr -Duser.region=FR -Djava.util.logging.config.file=logging.properties"
# export JAVA_OPTS="-Duser.language=en -Duser.region=US -Djava.util.logging.config.file=logging.properties"
export JAVA_OPTS="-Djava.util.logging.config.file=logging.properties"

../gradlew installDist
build/install/battleship/bin/battleship &
build/install/battleship/bin/battleship &
build/install/battleship/bin/server
