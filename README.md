**Reversi Game**

This is a Java implementation of the classic Reversi (also known as Othello) game, featuring a graphical user interface (GUI) for two players. The game allows players to make moves by clicking on the board squares and provides options for automated moves using a simple AI algorithm.

![Game Boards](image/game.png)

**How to Run the Game:**

1. Ensure you have Java installed on your system.
2. Compile the Java files using a Java compiler.
3. Run the `ReversiMain` class file.

**Game Rules:**

1. The game is played on an 8x8 board (it can be changed by `model.initialise(8, 8, view, controller);` at main).
2. Players take turns to place their pieces on the board.
3. A player's piece must be placed adjacent to an opponent's piece and must capture at least one opponent's piece by enclosing it between the placed piece and another piece of the player's color.
4. Captured opponent's pieces are flipped to the capturing player's color.
5. The game ends when neither player can make a valid move.
6. The player with the most pieces of their color on the board wins.

**Components:**

1. `ReversiMain`: The main class that initializes the game components (model, view, controller) and starts the game.
2. `ReversiController`: Implements the game logic, handles player moves, and controls the flow of the game.
3. `GUIView`: Provides the graphical user interface for the game, allowing players to interact with the board and displaying game status.
4. `IModel`, `IView`, `IController`: Interfaces defining the model-view-controller (MVC) architecture for the game.

**Game Features:**

1. Graphical user interface with separate frames for each player.
2. Board squares are clickable for making moves.
3. Options for automated moves using a simple AI algorithm.
4. Feedback messages for players displayed at the top of each frame.
5. Restart button to reset the game board.
