# GameScore Project

## Overview

The **GameScore** project is a custom **Java-based HTTP server** designed to handle and manage **game scores** for various users and games. The server allows clients to interact with the data through RESTful APIs, enabling the functionality to save scores, retrieve the user's highest scores for each game, and retrieve the top 10 highest scores for a specific game.

## Features

- **Save Scores**: Save a new score for a user in a specific game.
- **Retrieve Highest Scores for a User**: Retrieve the highest scores for a given user in each game.
- **Retrieve Top 10 Scores for a Game**: Retrieve the top 10 highest scores for a specific game, sorted in descending order.
- **REST API**: All interactions with the server can be made via HTTP methods (GET, POST).
- **MySQL Integration**: All game scores and related data are stored in a MySQL database.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 8 or above**: The project is built using Java.
- **MySQL Database**: The project uses MySQL to store scores.
- **Maven** : For managing dependencies and building the project.

## Setup

### 1. Clone the repository

Clone the repository to your local machine:

git clone https://github.com/Malsha6/gamescore.git
cd gamescore

### 2. Install Dependencies
run the following command to install dependencies:

mvn install

### 3. Configure the application.properties File
The application.properties file contains configuration details for the database connection and server settings.

#Database Configuration

db.url=jdbc:mysql://localhost:3306/gamescore

db.username=root

db.password=root

#HikariCP Configuration

hikari.max-pool-size=10

### 4. API Endpoints

1. Save a Score (POST /api/scores)

  Request body as below:
  
    {
    "userId": 1,
    "gameId": 101,
    "score": 2500
    } 

3. Get Highest Scores for a User (GET /api/scores/user/{userId})

4. Get Top 10 Scores for a Game (GET /api/scores/top10/{gameId})



