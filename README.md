# Java_RPG [![codecov](https://codecov.io/github/DoctorPok42/Java_RPG/graph/badge.svg?token=E332XQHEUA)](https://codecov.io/github/DoctorPok42/Java_RPG)

## Description

This project is an RPG game in Java. It is a school project for the 3rd year of engineering school at EPITECH.


## Installation

To install the project, you need to clone the repository and go to the project directory.

```bash
git clone git@github.com:DoctorPok42/Java_RPG.git
cd Java_RPG
```

- Java version: 21
- Maven version: 3.8.1

To run the project, you need to compile the project and run the jar file.

```bash
mvn clean package
java -jar target/Java_RPG-1.0-SNAPSHOT.jar
```

## Explanation

### Game

The game is an RPG that represents the Epitech Bordeaux campus. The player is a student who has to work on his projects try to finish them before the end of the year. The player can move on the campus and interact with the different items and characters.

### Mode

- **Survival**: The player has to finish his projects before the end of the year. If the player finishes all his projects before the end of the year, he wins the game. If the player doesn't finish all his projects before the end of the year, he loses the game.
- **Creative**: Here it's the way to test and modify the game/map like you want. You can add new items and characters.

### Commands

- **Move**: The player can move on the campus with the arrow or zqsd keys.
- **Interact**: The player can interact with the items and characters with the e key.
- **Profile**: The player can see his profile with the p key.

### Profile

The player has a profile with his name, his projects and his advancement. The player can see his profile with the p key.

### Items

- **Pc**: The player can work, play and search for an internship.
- **Sofa**: The player can rest.
- **Distributor**: The player can buy food.

## Technical

- **Maven**: The project is managed with Maven.
- **JavaFX**: The project uses JavaFX for the GUI.
- **Junit**: The project uses Junit for the tests.
- **Jacoco**: The project uses Jacoco for the coverage.
- **JavaDoc**: The project uses JavaDoc for the documentation.
