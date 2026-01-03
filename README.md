# CSS-Project: Software Systems Engineering

Quick note in Portuguese: Podem consultar este mesmo README em português. Para isso, basta aceder [aqui](README_PT).

## Description

**CSS-Project** is an academic project developed as part of the *Software Systems Engineering* course (*Construção de Sistemas de Software*). This project showcases the development of a modular software system with a focus on quality, efficiency, and integration. It includes two main modules—**UrbanWheels**, a bike-sharing system, and **WeatherWise**, a weather data service—both of which are integrated to enhance functionality.

This project utilizes technologies like Java, JavaFX, Spring Boot, and REST APIs, while adhering to best practices in software engineering such as modularity, layered architecture, and containerization with Docker.

---

## Modules Overview

### 1. **UrbanWheels**
**UrbanWheels** is a bike-sharing system designed to manage stations, bikes, and user operations. It implements graphical user interfaces for both administrators and end users with integration to **WeatherWise** for weather-based insights.

#### Features:
- **Administrator Interface** (Web via Thymeleaf):
  - Register and list stations and bikes.
  - Manage bike maintenance and availability.
  - Register and manage users.

- **User Interface** (Desktop via JavaFX):
  - Search for and reserve bikes.
  - Manage reservations and usage history.

- **Integration with WeatherWise**:
  - Fetch real-time weather data during bike reservations for informed decision-making.

#### Use Cases:
- Bookings are efficiently managed by updating bike statuses (`Reservada`, `Disponível`, etc.).
- Weather data enhances operational decisions.

---

### 2. **WeatherWise**
WeatherWise is an independent weather forecasting system. Initially built for standalone operation, it was later integrated into the UrbanWheels system to provide weather-based information.

#### Features:
- Real-time weather forecast based on location.
- Historical weather data storage for analytics and prediction.
- REST API for external integrations.

#### Use Cases:
- Utilized by UrbanWheels to pull weather data dynamically for routes and reservations.
- Enables estimation of future weather conditions based on historical data.

---

## Technologies Used
1. **Java**:
   - Core programming language used for back-end logic.
   - Focused on layered design and maintainable code structure.

2. **JavaFX & Thymeleaf**:
   - JavaFX: Desktop GUI for enhanced customer experience.
   - Thymeleaf: Web interface for administrative tasks.

3. **Spring Boot**:
   - Used for REST API implementation in UrbanWheels and WeatherWise integration.
   - Ensures scalability and robustness.

4. **Docker**:
   - Simplifies deployment across various environments.
   - Ensures consistency via containerization.

5. **PostgreSQL**:
   - Used as the database for WeatherWise for storing and querying weather data.

---

## System Architecture
The system is designed using a layered approach:
1. **Controller Layer**: Handles user interactions and API requests.
2. **Service Layer**: Contains core business logic.
3. **Repository Layer**: Manages data persistence.

Microservices architecture allows ease of scaling, with integration points relying on REST APIs between the modules.

---

## How to Setup and Run

### Prerequisites:
1. **Java Development Kit (JDK)** version 17 or later.
2. **Docker** and **Docker Compose** installed locally.
3. **PostgreSQL** database setup for WeatherWise if running without Docker.

---

### Installation:
1. **Clone the repository**:
   ```bash
   git clone https://github.com/guimbreon/CSS-Project.git
   cd CSS-Project
   ```

2. **Run with Docker**:
   - Navigate to the `weatherwise` directory:
     ```bash
     cd weatherwise
     docker-compose up --build
     ```
   - Then navigate to the `urbanwheels` directory:
     ```bash
     cd ../urbanwheels
     docker-compose up --build
     ```

3. **Javafx run** (Optional, after Step 2):
   - Run the javafx module:
     ```bash
     cd javafx
     mvn javafx:run
     ```
     ```

4. Access the system:
   - Web interface for administrators at `http://localhost:8080`.
   - Desktop application for customers.

---

#### REST API Integration:
For detailed API documentation:

- **UrbanWheels**: Visit Swagger at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **WeatherWise**: Visit Swagger at [http://localhost:8081/swagger-ui/index.html#/](http://localhost:8081/swagger-ui/index.html#/)

##### Example: Fetch weather data for a location
Example: Fetch weather data for a location.
Request:
```bash
GET /weatherwise/api/weather?location=Lisbon
```
Response:
```json
{
  "location": "Lisbon",
  "temperature": 22,
  "forecast": "Sunny"
}
```

---

## Documentation
This repository includes:
- `phase3.pdf` and `relatorio.pdf`: Detailed reports on the project phases and technical decisions.
- Docker files for containerized deployment.
- Complete source code for UrbanWheels and WeatherWise.

---

## Videos
Comprehensive demonstration videos for all use cases implemented are included in the `videos/` directory.

---

## Contributing
Contributions are welcome! To contribute:
1. Fork this repository.
2. Create a new feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Submit a pull request for review.

---

## License
This project is educational and adheres to the Software Systems Engineering course guidelines.

---

## Acknowledgments
- **Instructor** for guidance throughout the development.
- **Team Members** for excellent collaboration and commitment.
- Open-source libraries and communities.

---

Visit the repository [here](https://github.com/guimbreon/CSS-Project) for more details.
