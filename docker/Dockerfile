FROM openjdk:17 AS build
WORKDIR /workbook

#COPY workbook/gradlew .
#COPY workbook/gradle gradle
#COPY workbook/build.gradle .
#COPY workbook/settings.gradle .
#COPY workbook/src src

RUN microdnf install findutils
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:17
WORKDIR /workbook
#COPY workbook/src src
#COPY --from=build /workbook/build/libs/*.jar /workbook/myapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/workbook/myapp.jar"]