TSG 9.27 Group2

TSG 9.27 Group2 is a full-stack feedback collection system. The application allows users to submit feedback through a React frontend, which is sent to Kafka for processing and ultimately stored in a PostgreSQL database.

***
## Architecture Overview

The system consists of three separate repositories that work together:

>Frontend (React) – A user-facing interface for submitting feedback. <br>
https://github.com/York-Solutions-B2E/tsg-9.27-group2-frontend-feedback-ui.git

>Feedback API (Spring Boot) – Receives feedback from the frontend and sends messages to Kafka. <br>
https://github.com/SneakThief23/tsg-9.27-Group2-feedback-api.git

>Feedback Analytics Consumer (Spring Boot) – Consumes Kafka messages and stores them in PostgreSQL. <br>
https://github.com/SneakThief23/tsg-9.27-Group2-feedback-analytics-consumer.git

***
## Tech Stack

>Frontend: React<br>
Backend Services: Spring Boot<br>
Message Broker: Apache Kafka (running in Kraft mode)<br>
Database: PostgreSQL<br>
Containerization: Docker
***
## Folder Structure

Clone the three repositories inside a single parent folder:

>parent-folder/ <br>
├── feedback-ui                (React frontend) <br>
├── feedback-api               (Spring Boot producer) <br>
├── feedback-analytics-consumer (Spring Boot consumer)

***
## How to Run the Project

Make sure all three repositories are inside the same parent folder.

Navigate into the feedback-api folder and open in IntelliJ.

>Run: docker compose up --build

>This command will: <br>
-Build and start the React frontend <br>
-Start Kafka
-Start the Feedback API service <br>
-Start the Feedback Analytics Consumer service <br>
-Start PostgreSQL

All services will come up using a shared Docker network defined in the frontend compose file.

***
# Services Overview

>React Frontend <br>
UI for submitting feedback

>Feedback API <br>
REST API that sends feedback to Kafka

>Kafka <br>
Message broker transferring feedback messages

>Feedback Analytics Consumer <br>
Reads Kafka messages and stores them in PostgreSQL

>PostgreSQL <br>
Persistent storage for feedback

>SwaggerUI
> To check API endpoints <br>
http://localhost:8081/swagger-ui/index.html

***
# Feedback Flow

>1. User submits feedback from the React UI.</li>
>2. Feedback API receives the request and publishes a message to Kafka.
>3. Feedback Analytics Consumer reads the Kafka message.
>4. Consumer writes the feedback record into PostgreSQL.


