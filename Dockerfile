FROM adoptopenjdkopenjdk15armv7l-debianslim-jre-15.0.2_7

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

EXPOSE 9001

CMD ["./mvnw", "spring-boot:run"]
