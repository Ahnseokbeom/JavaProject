# Omok Game

The Omok Game is a Java Swing-based implementation of the classic board game "Gomoku" (also known as Five in a Row). Players take turns placing their stones on the board with the goal of getting five of their stones in a row, horizontally, vertically, or diagonally. Below is an overview of the project's structure and components.

## Project Components

### Classes

#### `OmokGame`

- The main class extending `JFrame`, responsible for creating the game window and handling user interactions.
- Includes methods for initializing the game board, managing player moves, and checking for a win.
- Features a simple AI for the computer player.

### Game Board

#### `board`

- Represents the game board, a 2D array of characters.
- `EMPTY` indicates an empty cell, `PLAYER1` represents the player's stone, and `PLAYER2` represents the computer's stone.

### User Interaction

#### Mouse Click Events

- The game responds to mouse clicks on the game board.
- Players can make moves by clicking on an empty cell.

### Game Logic

#### `makeMove(int row, int col, char player)`

- Checks if a move is valid and updates the game board.

#### `checkWin(int row, int col, char player)`

- Checks for a win in all directions (horizontal, vertical, diagonal).

#### `computerMove(int userRow, int userCol)`

- Handles the computer's move after the player makes a move.

### AI Logic

#### `countConsecutiveStones(int row, int col, char player)`

- Counts consecutive stones in a given direction.

#### `isValidMove(int row, int col)`

- Checks if a move is valid (within the board and the cell is empty).

#### `computerRandomMove()`

- If no strategic move is available, the computer makes a random move.

### Game Flow

#### `handleGameEnd(String message)`

- Displays a dialog when the game ends, prompting the player to restart or exit.

#### `resetGame()`

- Resets the game board for a new round.

### UI Rendering

#### `paint(Graphics g)`

- Renders the game board, player stones, and computer stones.

### Main Method

#### `main(String[] args)`

- Entry point for the application, invoking the creation of a new `OmokGame` instance.

