# Ashok IT - Ultra Lightweight Java Image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8084

# Reduce JVM memory usage
ENTRYPOINT ["java","-Xms128m","-Xmx256m","-jar","/app/app.jar"]
