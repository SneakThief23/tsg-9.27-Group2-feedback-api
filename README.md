TSG 9.27 Group2

TSG 9.27 Group2 is a full-stack feedback collection system. The application allows users to submit feedback through a React frontend, which is sent to Kafka for processing and ultimately stored in a PostgreSQL database.
***

## Architecture Overview

The system consists of three separate repositories that work together:

### Frontend (React) – A user-facing interface for submitting feedback.
```
https://github.com/York-Solutions-B2E/tsg-9.27-group2-frontend-feedback-ui.git
```

### Feedback API (Spring Boot) – Receives feedback from the frontend and sends messages to Kafka.

```
https://github.com/SneakThief23/tsg-9.27-Group2-feedback-api.git
```

### Feedback Analytics Consumer (Spring Boot) – Consumes Kafka messages and stores them in PostgreSQL.

```
https://github.com/SneakThief23/tsg-9.27-Group2-feedback-analytics-consumer.git
```
***

## Tech Stack
|Layer | Technologies          |
|-|-|
|Frontend| React                 |
|Backend| Spring Boot           |
|Message Broker| Kafka (running KRaft) |
|Database| PostgreSQL            |
|Containerization| Docker                |
***

## Local Setup

Clone the three repositories inside a single parent folder:

```
parent-folder/ <br>
├── feedback-ui                (React frontend) <br>
├── feedback-api               (Spring Boot producer) <br>
├── feedback-analytics-consumer (Spring Boot consumer)
```
***

## How to run the project

Make sure all three repositories are inside the same parent folder.

Navigate into the frontend folder and open in IntelliJ.

### Run: 
```
cd tsg-9.27-group2-frontend-feedback-ui
docker compose up --build
```

This command will:
- Build and start the React frontend <br>
- Start Kafka
- Start the Feedback API service <br>
- Start the Feedback Analytics Consumer service <br>
- Start PostgreSQL

All services will come up using a shared Docker network defined in the frontend compose file.
***

# Services Overview
| Services Overview           | Description                                                    |
|-----------------------------|----------------------------------------------------------------|
| React Frontend              | User interface for submitting feedback.                        |
| Feedback API                | REST API that sends feedback to Kafka.                         |
| Kafka                       | Message broker that transfers feedback messages.               |
| Feedback Analytics Consumer | Reada Kafka messages and stores them in a PostgreSQL database. |
|  PostgreSQL                 | Persistent storage for feedback submissions.                   |
| Swagger UI                  | Used to check API end points in th browser.|
***

# Feedback Flow

>1. User submits feedback from the React UI.</li>
>2. Feedback API receives the request and publishes a message to Kafka.
>3. Feedback Analytics Consumer reads the Kafka message.
>4. Consumer writes the feedback record into PostgreSQL.
***

# API Endpoints

Swagger UI
http://localhost:8080/swagger-ui/index.html

Base Path `http://localhost:8080/api/v1`

- `GET /api/v1/feedback` - Returns all feedback for a given Member Id.
- `GET /api/v1/feedback/{id}` - Returns feedback for a specific UUID.
- `POST /api/v1/feedback` - Submit new feedback.
***

# Schemas

## FeedbackResponseDTO
| Field        | Description                           | Required/Optional                                   |
|--------------|---------------------------------------|-----------------------------------------------------|
| id           | UUID generated and stored in database | Required, created by database, not visible to User. |
| memberId     | ID of the member submitting feedback. | Required                                            |
| providerName | Name of the service provider. | Required|
| submittedAt  |Timestamp created at submission date by database. | Required, created by database, not visible to User.|
|rating | 1 through 5 rating | Required|
|comment | 200 character max comment on service provided. | Optional|

Object submitted by user to store feedback.

## FeedbackRequestDTO
| Field | Description| Required/Optional |
|-------|-|-------------------|
|memberId | ID of the member submitting feedback.| Required          |
| providerId | ID or name of the service provider.| Required          |
|rating | 1 through 5 rating | Required          |
|comment | Feedback comment, max 200 characters | Optional          |

Object returned to the user by searching by memberID or UUID.