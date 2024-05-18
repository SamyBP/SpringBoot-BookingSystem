# SpringBoot-BookingSystem

## Setup

### 1. Clone the repository
### 2. Configure the application.properties file
- Navigate to `src/main/resources/application.properties`.
- Update the required configurations such as database connection details

## Usage

### 1. Register a new user

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/register`
- **Headers:** None
- **Body (JSON):**
  ```json
  {
    "username": "user123",
    "password": "password123"
  }

### 2. Login

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/login`
- **Headers:** None
- **Body (JSON):**
  ```json
  {
    "username": "user123",
    "password": "password123"
  }

### 3. Find the hotels available in a specified radius and period

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/hotels/nearme/{radius}`
  - Replace `{radius}` with the desired radius value.
- **Headers:**
  - `Content-Type: application/json`
  - `X-Forwarded-For: the client's ip address`
- **Query Parameters:**
  - `startDate`: Specify the start date for the search (ISO Date format).
  - `endDate`: Specify the end date for the search (ISO Date format).

### 4. Make a reservation for one or more rooms

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/hotels/book`
- **Headers:** None
- **Body (JSON):**
  ```json
  {
    "rooms" : [
                {
                    "id": 1,
                    "roomNumber": 210,
                    "type": 2,
                    "price": 200.0,
                    "available": true
                }
    ],
    "userId" : 1,
    "startDate": "2024-04-20",
    "endDate": "2024-04-22"
  }

### 5. Find available rooms to leave feedback

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/feedback/notification/{id}`
  - Replace `{id}` with the users id    
- **Headers:** None

### 6. Leave a feedback for an available room

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/feedback/addfeedback`
  - Replace `{id}` with the users id    
- **Headers:** None
- **Body (JSON):**
  ```json
  {
    "userId" : 1,
    "reservationId" : 5,
    "information" : " "
  }
