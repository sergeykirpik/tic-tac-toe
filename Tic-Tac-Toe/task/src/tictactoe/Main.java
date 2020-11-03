package tictactoe;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Game game = new Game();
        game.draw();

        boolean gameFinished = false;
        while (!gameFinished) {
            int[] coordinates = readCoordinates();
            if (coordinates == null) {
                continue;
            }
            int col = coordinates[0] - 1;
            int row = 3 - coordinates[1];

            switch (game.makeMove(row, col)) {
                case ERR_NOT_IN_RANGE:
                    System.out.println("Coordinates should be from 1 to 3!");
                    break;
                case ERR_OCCUPIED:
                    System.out.println("This cell is occupied! Choose another one!");
                    break;
                case X_WINS:
                    game.draw();
                    System.out.println("X wins");
                    gameFinished = true;
                    break;
                case O_WINS:
                    game.draw();
                    System.out.println("O wins");
                    gameFinished = true;
                    break;
                case DRAW:
                    game.draw();
                    System.out.println("Draw");
                    gameFinished = true;
                    break;
                case MOVE_OK:
                    game.draw();
                    break;
                default:
                    break;
            }
        }
    }

    static int[] readCoordinates() {
        System.out.print("Enter the coordinates: ");
        String[] rawCoordinates = scanner.nextLine().split("\\s+");
        boolean isValid =
            rawCoordinates.length >= 2 &&
            rawCoordinates[0].matches("\\d+") &&
            rawCoordinates[1].matches("\\d+");

        if (!isValid) {
            System.out.println("You should enter numbers!");
            return null;
        }
        int x = Integer.parseInt(rawCoordinates[0]);
        int y = Integer.parseInt(rawCoordinates[1]);

        return new int[] { x, y };
    }
}

enum MoveResult {
    MOVE_OK,
    ERR_OCCUPIED,
    ERR_NOT_IN_RANGE,
    DRAW,
    X_WINS,
    O_WINS
}

class Game {
    static final char EMPTY_CELL = ' ';
    static final char PLAYER_X = 'X';
    static final char PLAYER_O = 'O';

    String cells;
    char player;

    Game() {
        this(String.valueOf(EMPTY_CELL).repeat(9));
    }

    Game(String cells) {
        this.cells = cells;
        player = PLAYER_X;
    }

    void setState(String cells) {
        this.cells = cells;
    }

    void setPlayer(char player) {
        this.player = player;
    }

    MoveResult makeMove(int row, int col) {
        boolean notInRange = row < 0 || row > 2 || col < 0 || col > 2;
        if (notInRange) {
            return MoveResult.ERR_NOT_IN_RANGE;
        }
        if (cellAt(row, col) != EMPTY_CELL) {
            return MoveResult.ERR_OCCUPIED;
        }
        int insertAt = convertToCharIndex(row, col);
        setState(cells.substring(0, insertAt) + player + cells.substring(insertAt + 1));

        if (isWinner(player)) {
            if (player == PLAYER_X) {
                return MoveResult.X_WINS;
            } else {
                return MoveResult.O_WINS;
            }
        }

        if (count(EMPTY_CELL) == 0) {
            return MoveResult.DRAW;
        }

        switchPlayer();
        return MoveResult.MOVE_OK;
    }

    void switchPlayer() {
        setPlayer(player == PLAYER_X ? PLAYER_O : PLAYER_X);
    }

    void draw() {
        System.out.println("---------");
        for (int row = 0; row < 3; row++) {
            System.out.print("|");
            for (int col = 0; col < 3; col++) {
                System.out.printf(" %s", cellAt(row, col));
            }
            System.out.println(" |");
        }
        System.out.println("---------");
    }

    String checkCurrentState() {
        boolean isWinnerX = isWinner(PLAYER_X);
        boolean isWinnerO = isWinner(PLAYER_O);
        int countX = count(PLAYER_X);
        int countO = count(PLAYER_O);
        boolean noMoreMoves = count(EMPTY_CELL) == 0;

        if (Math.abs(countO - countX) >= 2 ) {
            return "Impossible";
        }
        if (isWinnerX && isWinnerO) {
            return "Impossible";
        }
        if (isWinnerX) {
            return "X wins";
        }
        if (isWinnerO) {
            return "O wins";
        }
        if (noMoreMoves) {
            return "Draw";
        }

        return "Game not finished";
    }

    char cellAt(int row, int col) {
        return cells.charAt(row * 3 + col);
    }

    int convertToCharIndex(int row, int col) {
        return row * 3 + col;
    }

    boolean areEqual(char sample, char c1, char c2, char c3) {
        return c1 == sample && c2 == sample && c3 == sample;
    }

    boolean isWinner(char candidate) {
        boolean found = false;
        for (int i = 0; i < 3; i++) {
            // check rows
            found |= areEqual(candidate,
                cellAt(i, 0),
                cellAt(i, 1),
                cellAt(i, 2)
            );
            // check columns
            found |= areEqual(candidate,
                cellAt(0, i),
                cellAt(1, i),
                cellAt(2, i)
            );
        }
        // check diagonal1
        found |= areEqual(candidate,
            cellAt(0, 0),
            cellAt(1, 1),
            cellAt(2, 2)
        );
        // check diagonal2
        found |= areEqual(candidate,
            cellAt(0, 2),
            cellAt(1, 1),
            cellAt(2, 0)
        );
        return found;
    }

    int count(char symbol) {
        int n = 0;
        for (int i = 0; i < cells.length(); i++) {
            if (cells.charAt(i) == symbol) {
                n++;
            }
        }
        return n;
    }
}
