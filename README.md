# DreamFlow API

DreamFlow is a backend API for a music streaming platform built with Spring Boot.
It provides secure authentication using JWT and supports efficient audio streaming using HTTP Range Requests.

---

## Features

* User Signup and Login (`/auth`)
* JWT-based Authentication
* Secure API endpoints with Spring Security
* Audio Streaming with HTTP Range support (seek and buffer)
* Modular, feature-based architecture

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
├── song
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
4. Client sends token in header:

```
Authorization: Bearer <token>
```

---

## Song Endpoints

### Get All Songs

GET `/songs`

**Response:**

```json
[
  {
    "songId": 1,
    "songName": "Song Name",
    "durationMs": "Artist Name"
  }
]
```

---

### Get Song Metadata

GET `/songs/{id}`

**Response:**

```json
{
  "songId": 1,
  "songName": "Song Name",
  "duration": 210
}
```

---

### Stream Song

GET `/songs/{id}/stream`

**Optional Header:**

```
Range: bytes=0-
```

**Behavior:**

* Supports HTTP Range Requests
* Enables streaming and seeking
* Optimized for chunk-based delivery

**Response Status:**

* 200 OK (full file)
* 206 Partial Content (streaming)

**Important Headers:**

```
Content-Type: audio/mpeg
Accept-Ranges: bytes
Content-Range: bytes <start>-<end>/<total>
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
* Song streaming with Range support implemented
* Playlist module under development

---

## Future Enhancements

* Playlist features
* Refresh token system
* Role-based authorization
* OAuth integration
* Cloud storage integration (e.g., S3)
* CDN-based streaming

---

## Contributing

This is a personal learning project. Contributions and suggestions are welcome.
