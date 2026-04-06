# DreamFlow API

DreamFlow is a backend API for a music streaming platform built with Spring Boot.
It currently supports user authentication using JWT and follows a clean, modular architecture.

---

## Features

* User Signup and Login (`/auth`)
* JWT-based Authentication
* Modular Feature-Based Architecture
* Spring Security Integration

---

## Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Tokens)
* MySQL (or any relational database)
* Maven

---

## Project Structure

```
com.dreamflow.api
│
├── auth
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│
├── security
├── config
```

---

## Authentication Endpoints

### Signup

POST `/auth/signup`

```json
{
  "username": "test",
  "email": "test@gmail.com",
  "password": "password"
}
```

---

### Login

POST `/auth/login`

```json
{
  "email": "test@gmail.com",
  "password": "password"
}
```

---

### Response

```json
{
  "token": "your_jwt_token"
}
```

---

## Authentication Flow

1. User signs up or logs in
2. Server validates credentials
3. JWT token is generated
4. Client sends the token in request headers:

```
Authorization: Bearer <token>
```

---

## Configuration

Set the following environment variables:

```
DB_URL=your_database_url
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret
```

---

## Running the Application

```bash
git clone https://github.com/your-username/dreamflow-api.git
cd dreamflow-api
./mvnw spring-boot:run
```

---

## Current Status

* Authentication module implemented
* Song and Playlist modules under development

---

## Future Enhancements

* Song management APIs
* Playlist features
* Refresh token system
* Role-based authorization
* OAuth integration

---

## Contributing

This is a personal learning project. Contributions and suggestions are welcome.
