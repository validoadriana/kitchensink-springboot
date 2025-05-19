# Stage 1: Build React frontend
FROM registry.access.redhat.com/ubi9/nodejs-22-minimal AS frontend-build
USER 1001
WORKDIR /app/frontend

# Copy and install frontend dependencies
COPY --chown=1001:0 src/main/frontend/package*.json ./
RUN npm ci

# Copy source code and build
COPY --chown=1001:0 src/main/frontend/ ./
ENV PUBLIC_URL=/kitchensink
RUN npm run build


# Stage 2: Build Spring Boot backend
FROM registry.access.redhat.com/ubi8/openjdk-21 AS backend-build
USER 1001
WORKDIR /app

# Copy Maven build files
COPY --chown=1001:0 pom.xml ./
COPY --chown=1001:0 src ./src

# Inject frontend build into static resources
COPY --chown=1001:0 --from=frontend-build /app/frontend/build ./src/main/resources/static

# Build backend
RUN mvn clean package -DskipTests

# Stage 3: Runtime
FROM registry.access.redhat.com/ubi8/openjdk-21-runtime
USER 1001
WORKDIR /app

# Copy built JAR file
COPY --chown=1001:0 --from=backend-build /app/target/*.jar ./app.jar

# Define environment variables for MongoDB (optional for clarity)
#ENV MONGO_USERNAME=${MONGO_USERNAME} \
#    MONGO_PASSWORD=${MONGO_PASSWORD} \
#    MONGO_CLUSTER=${MONGO_CLUSTER} \
#    MONGO_DATABASE=${MONGO_DATABASE}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
