# MineSweeper Game

The MineSweeper Game is a Java Swing-based application that allows users to play the classic game of MineSweeper. Below is an overview of the project's structure and components.

## Project Components

### Classes

#### `MineSweeperGame`

- The main class extending `JFrame`, responsible for creating the game window and handling user interactions.
- Includes methods for initializing the game board, placing mines, and managing button clicks.
- Features dynamic grid generation and mine placement based on user-selected difficulty (beginner, intermediate, advanced).

### Game Logic

#### `handleButtonClick(int row, int col)`

- Handles the click event when a button on the game board is pressed.
- Reveals tiles, checks for mines, and updates the game state accordingly.

#### `placeMines()`

- Randomly places mines on the game board based on the selected difficulty.

#### `countAdjacentMines(int row, int col)`

- Counts the number of mines adjacent to a given tile.

#### `revealEmptyTiles(int row, int col)`

- Reveals all adjacent empty tiles when an empty tile is clicked.

#### `showMines()`

- Reveals the locations of all mines on the board.

### UI Components

#### `initializeBoard()`

- Sets up the initial game board with buttons and adds action listeners for button clicks.

### Game Flow

#### `restartGame()`

- Resets the game state for a new round.

#### `checkGameWin()`

- Checks whether the game has been won by revealing all non-mine tiles.

### UI Initialization

#### `initializeGame()`

- Displays a dialog for selecting the game difficulty (beginner, intermediate, advanced).
- Initializes and returns a new instance of the `MineSweeperGame` based on the chosen difficulty.

### Main Method

#### `main(String[] args)`

- Entry point for the application, invoking the creation of a new `MineSweeperGame` instance.
