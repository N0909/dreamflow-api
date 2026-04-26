# DreamFlow API

DreamFlow is a backend API for a music streaming platform built with Spring Boot.
It provides secure authentication using JWT, efficient audio streaming using HTTP Range Requests,
and improved performance using Redis caching and rate limiting.

---

## Features

- User Signup and Login (/auth)
- JWT-based Authentication
- Secure API endpoints with Spring Security
- Audio Streaming with HTTP Range support (seek + buffer)
- Redis-based Caching for performance optimization
- Redis-based Rate Limiting to prevent abuse
- Modular, feature-based architecture

---

## Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- MySQL (or any relational database)
- Redis (Caching + Rate Limiting)
- Maven

---

## Project Structure
```
.
`-- src
    `-- main
        |-- java
        |   `-- com
        |       `-- dreamflow
        |           `-- api
        |               |-- auth
        |               |   |-- controller 
        |               |   |-- dto
        |               |   |-- entity
        |               |   |-- repository
        |               |   `-- service
        |               |-- config 
        |               |-- exception 
        |               |   `-- exceptions
        |               |-- playlist
        |               |   |-- controller
        |               |   |-- dto
        |               |   |-- entity
        |               |   |-- repository
        |               |   `-- service
        |               |-- security
        |               |-- song
        |               |   |-- controller
        |               |   |-- dto
        |               |   |-- entity
        |               |   |-- repository
        |               |   `-- service
        |               `-- util
        `-- resources
            |-- static
            `-- templates

```
---

## Authentication Endpoints

### Signup

```
POST /auth/signup
```
Request:
```json
{
  "username": "test",
  "email": "test@gmail.com",
  "password": "password"
}
```
Response:
```json
{
    "accessToken": your_access_token,
    "refreshToken": your_refresh_token
}
```
---

### Login
```
POST /auth/login
```
Request:
```json
{
  "email": "test@gmail.com",
  "password": "password"
}
```
```
POST /auth/refresh
```
Request:
```json
{
   "refreshToken": your_refresh_token
}
```

Response:
```json
{
  "accessToken": your_access_token
}
```

---

### Response
```json
{
    "accessToken": your_access_token,
    "refreshToken": your_refresh_token
}
```
---

## Authentication Flow

1. User signs up or logs in
2. Server validates credentials
3. JWT token is generated
4. Client sends token in header:

Authorization: Bearer <token>

---

## Song Endpoints

### Get All Songs
```
GET /songs
```
Response:
```json
{
    "content": [
        {
            "songId": 1,
            "songName": "song_1",
            "durationMs": 204000
        },
        {
            "songId": 2,
            "songName": "song_2",
            "durationMs": 182400
        },
        {
            "songId": 3,
            "songName": "song_4",
            "durationMs": 210000
        },
        {
            "songId": 5,
            "songName": "song_5",
            "durationMs": 222024
        },
        {
            "songId": 6,
            "songName": "song_6",
            "durationMs": 230688
        }
    ],
    "empty": false,
    "first": true,
    "last": false,
    "number": 0,
    "numberOfElements": 5,
    "pageable": {
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 5,
        "paged": true,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "unpaged": false
    },
    "size": 5,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "totalElements": total_in_db,
    "totalPages": Math.ceil(total_in_db/page_size)
}
```
```
GET /songs?page_no={page_no}?page_size={page_size}
```
Response:
```json
{
    "content": [
        {
            "songId": 1,
            "songName": "song_1",
            "durationMs": 204000
        },
        {
            "songId": 2,
            "songName": "song_2",
            "durationMs": 182400
        },
        {
            "songId": 3,
            "songName": "song_4",
            "durationMs": 210000
        },
        {
            "songId": 5,
            "songName": "song_5",
            "durationMs": 222024
        },
        {
            "songId": 6,
            "songName": "song_6",
            "durationMs": 230688
        }
        .
        .
        .
        till page_size
    ],
    "empty": false,
    "first": true,
    "last": false,
    "number": 0,
    "numberOfElements": 5,
    "pageable": {
        "offset": {page_no}*{page_size},
        "pageNumber": {page_no},
        "pageSize": {page_size},
        "paged": true,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "unpaged": false
    },
    "size": {page_size},
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "totalElements": total_in_db,
    "totalPages": Math.ceil(total_in_db/page_size)
}
```

---

### Get Song Metadata
```
GET /songs/{id}
```
Response:
```json
{
  "songId": 1,
  "songName": "Song Name",
  "duration": 210
}
```

---

### Stream Song
```
GET /songs/{id}/stream
```
Optional Header:
Range: bytes=0-

Behavior:
- Supports HTTP Range Requests
- Enables streaming and seeking
- Optimized for chunk-based delivery

Response Status:
- 200 OK (full file)
- 206 Partial Content (streaming)

Important Headers:
Content-Type: audio/mpeg
Accept-Ranges: bytes
Content-Range: bytes <start>-<end>/<total>
---

## User Endpoints (`/me`)

All endpoints require authentication.

Header:
Authorization: Bearer <token>

---

### Get Current User
```
GET /me
```
Response:
```json
{
  "userid": 15,
  "username": "johndoe4",
  "email": "johndoe4@gmail.com",
  "createdAt": "2026-04-06T19:42:54"
}
```

---

### Get User Playlists
```
GET /me/playlists
```
Response:
```json
[
  {
    "playlistId": 4,
    "playlistName": "Sleeping",
    "createdAt": "2026-04-11T22:06:02"
  }
]
```

---

### Create Playlist
```
POST /me/playlists
```
Request Body:
```json
{
  "playlistName": "Sleeping"
}
```

Response:
```json
{
  "playlistId": 5,
  "playlistName": "New Playlist1",
  "createdAt": "2026-04-26T14:38:32.3991523"
}
```

---

### Get Playlist by ID
```
GET /me/playlists/{playlistId}
```
Example:
```
GET /me/playlists/1
```
Response:
```json
{
  "playlistId": 1,
  "playlistName": "Playlist1",
  "createdAt": "2026-04-03T22:01:48",
  "songs": [
    {
      "songId": 3,
      "songName": "song3",
      "durationMs": 204000
    }
  ]
}
```

---

### Add Song to Playlist
```
POST /me/playlists/{playlistId}/songs
```
Example:
POST /me/playlists/4/songs

Request Body:
```json
{
  "songId": 23
}
```

Response:
```json
{
  "playlistId": 4,
  "playlistName": "Sleeping",
  "songId": 23,
  "songName": "Song Name"
}
```

---

### Remove Song from Playlist

DELETE /me/playlists/{playlistId}/songs/{songId}

Example:
DELETE /me/playlists/1/songs/5

Response:
204 No Content

## Redis Caching

- Frequently accessed data (e.g., song path) is cached using Redis
- Reduces database load and improves response time
- Cache invalidation handled on updates (if implemented)

---

## Rate Limiting

- Implemented using Redis
- Limits number of requests per user/IP within a time window
- Prevents abuse and protects backend services

---

## Current Status

- Authentication module implemented
- Song streaming with Range support implemented
- Redis caching implemented
- Refresh token implemented
- Rate limiting implemented
- Playlist module implemented

---

## Future Enhancements
- Advanced search using Elasticsearch
  - Full-text search on songs
  - Fast and scalable querying
  - Supports fuzzy search and partial matching
- AI-powered search and recommendation system (RAG-based)
  - Search songs using natural language instead of exact keywords
  - Personalized playlist suggestions using user behavior + embeddings
  - Intelligent query understanding (mood, genre, context)
- Role-based authorization
- OAuth integration
- Cloud storage integration (e.g., S3)
- CDN-based streaming
---

## Contributing

This is a personal portfolio project. Contributions and suggestions are welcome.
