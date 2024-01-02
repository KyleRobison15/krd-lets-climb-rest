# This is a multi-stage dockerfile. That means there will be two separate images created from a single dockerfile.

# Image 1: The build image
# Specify the base image for the build stage.
# Here we use the gradle:8.5.0-jdk17-alpine image, which includes both Gradle and JDK 17, and name this stage as "build."
#FROM gradle:8.5.0-jdk17-alpine AS build

# Copy the Java source code from the host machine into the docker image in a newly created directory: /home/gradle/src
#COPY --chown=gradle:gradle . /home/gradle/src

# Set the working directory in the image to the one we just created that contains the source code
#WORKDIR /home/gradle/src

# Build the Java app, specifically without using the background daemon for the build
# The result of this operation will be a .jar file that contains our executable application
#RUN gradle build --no-daemon

# Image 2: The runtime image
# This base image is optimize for runtime only, and doesn't include any build tools (since we already did that in the previous stage)
# This allows us to keep the final image much smaller
FROM eclipse-temurin:17-jre-alpine

ENV artifactVersion=krd-lets-climb-rest-VERSION_NUMBER.jar

VOLUME /tmp

# Documentation explaining this container will eventually listen on port 8080.
# Later, when we run this app inside a docker container, we know we should map a port from the host, to port 8080 on the container
EXPOSE 8080

# Create a new group named "app" and a system user named "app" within that group.
# This is often done to run applications within a Docker container with a specific user and group for security and isolation purposes.
RUN addgroup app && adduser -S -G app app

# This is the command that will be run immediately when the container starts
# Here we are saying run the Java app JAR file with various options
ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar"]

ADD build/libs/krd-lets-climb-rest-VERSION_NUMBER.jar app.jar