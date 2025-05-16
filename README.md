# Kitchensink Spring Boot Application

This project is a migration of the JBoss Kitchensink quickstart to Spring Boot, featuring a React frontend and MongoDB backend.

## Prerequisites

- JDK 21
- Node.js 18+
- Maven 3.9+
- Docker (optional)
- MongoDB Atlas Account (or local MongoDB instance)

## Project Structure

```
src/
├── main/
│   ├── frontend/          # React TypeScript frontend
│   ├── java/             # Spring Boot backend
│   └── resources/        # Application configuration
└── test/                 # Test files
```

## Environment Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd kitchensink-springboot-migration
```

2. Create a `.env` file in the root directory with your MongoDB credentials:
```properties
MONGO_USERNAME=your_username
MONGO_PASSWORD=your_password
MONGO_CLUSTER=your_cluster.mongodb.net
MONGO_DATABASE=kitchensink
```

## Development

### Backend Setup

1. Build the Spring Boot application:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

The backend API will be available at `http://localhost:8080/rest`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd src/main/frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will be available at `http://localhost:8080`

## Running with Docker

1. Build the Docker image:
```bash
docker build -t kitchensink-spring .
```

2. Run the container:
```bash
docker run -d -p 8080:8080 \
  -e MONGO_USERNAME=your_username \
  -e MONGO_PASSWORD=your_password \
  -e MONGO_CLUSTER=your_cluster \
  -e MONGO_DATABASE=kitchensink \
  --name kitchensink-app \
  kitchensink-spring
```

## API Endpoints

- GET `/api/members` - List all members
- POST `/api/members` - Create a new member
- GET `/api/members/{id}` - Get member by ID

## Running Tests

```bash
# Run all tests
mvn test
```

## MongoDB Atlas Setup

1. Create a MongoDB Atlas account at https://www.mongodb.com/cloud/atlas
2. Create a new cluster
3. Create a database user
4. Add your IP address to the IP Access List
5. Get your connection string from the Connect dialog
6. Update your `.env` file with the connection details
