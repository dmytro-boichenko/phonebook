FROM openjdk:8-jdk-alpine

# Copy assembled artifact into container
COPY ./target/phonebook.jar /opt/phonebook/app.jar

# Start application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/phonebook/app.jar"]