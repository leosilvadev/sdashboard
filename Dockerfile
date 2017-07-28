FROM gradle:alpine

############ INSTALL NODE
RUN apk add --update nodejs
############ INSTALL NODE

COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src

RUN gradle installDist

ENTRYPOINT ./build/install/sdashboard/bin/sdashboard