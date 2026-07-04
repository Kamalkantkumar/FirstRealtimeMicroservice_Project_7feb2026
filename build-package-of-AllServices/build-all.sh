#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "Starting build process for all microservices..."

# List of your microservice directories
SERVICES=(
    "discovery-server_Eureka"
    "spring-cloud-config"
    "auth-service"
    "customer-service"
    "loan-service"
    "api-gateway"
)

# Loop through each directory and run the build
for service in "${SERVICES[@]}"; do
    if [ -d "$service" ]; then
        echo "----------------------------------------------------"
        echo "Building: $service"
        echo "----------------------------------------------------"
        cd "$service"
        mvn clean package -DskipTests
        cd ..
    else
        echo "Directory $service not found! Skipping..."
    fi
done

echo "----------------------------------------------------"
echo "All builds completed successfully!"
echo "----------------------------------------------------"