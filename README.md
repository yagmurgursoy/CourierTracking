# 🚚 Courier Tracking API

A Java 21-based RESTful API that tracks courier geolocations and detects proximity to Migros stores. Built using Spring Boot, Maven, and Jacoco for test coverage. The API includes Swagger UI for testing and documentation.

---

## 📌 Problem Statement

Track couriers' real-time geolocation and log when they enter a **100-meter** radius of predefined **Migros stores**. The system must:

- **Log entries** only if more than **1 minute** has passed since the last entry into the same store's radius.
- **Calculate total distance** traveled by each courier.
- Use at least **two design patterns**.

---

## 📌 Endpoints

After Run CourierTrackingAppApplication
You can reach the swagger:
http://localhost:8080/swagger-ui/index.html

Try it out - endpoints

Firstly you should try post method
/v1/api/courier/location  
you can call this endpoint with several request
and then you can get total distance for specific couirer below endpoint
/v1/api/courier/{courierId}/total-distance

## 🚀 Features

- ✅ RESTful endpoints for submitting courier geolocation data.
- ✅ Detects store proximity and logs entries.
- ✅ Prevents duplicate store entries within 1 minute.
- ✅ Calculates total travel distance per courier.
- ✅ Uses Swagger UI for API exploration.
- ✅ Includes unit tests with Jacoco test coverage.
- ✅ Implements Observer and Factory design patterns.

---

## 🧱 Tech Stack

- Java 21
- Spring Boot
- Maven
- Jacoco (test coverage)
- Swagger/OpenAPI
- JUnit 5 & Mockito

---

## 🔧 Setup & Run

### Prerequisites

- Java 21+
- Maven 3.8+

### Clone,Build and Test

```bash
git clone https://github.com/yagmurgursoy/CourierTracking/tree/master
cd courier-tracking-api
mvn clean install

to see coverage report :
mvn jacoco:report
after run this maven commut you can reach coverage report
under target->site->index.htmls