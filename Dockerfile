FROM eclipse-temurin:17-jre-alpine

LABEL com.krd.sdp.image.tag=IMAGE_TAG \
      com.krd.sdp.image.tag=IMAGE_CREATED_DT \
      com.krd.sdp.image.tag=IMAGE_LATEST_COMMIT \
      com.krd.sdp.image.artifactVersion=krd-lets-climb-rest-VERSION_NUMBER.jar \

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