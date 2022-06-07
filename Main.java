package minesweeper;


import java.util.Objects;
import java.util.Random;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        // write your code here

        Minefield game = new Minefield();
        while (!game.hasLost || !game.hasWon) {
            game.nextTurn();
        }

    }
}

class Minefield {
    int[][] board = new int[9][9];
    boolean[][] mineLocations = new boolean[9][9];
    boolean[][] marked = new boolean[9][9];

    boolean[][] explored = new boolean[9][9];

    boolean hasWon;
    boolean hasLost;

    boolean boardSet;
    static Scanner scanner = new Scanner(System.in);
    private int mines;


    public Minefield(){
        System.out.print("How many mines do you want on the field? ");
        this.mines = scanner.nextInt();
        this.showMinefield();
    }

    public void nextTurn() {
        System.out.print("Set/unset mines marks or claim a cell as free: ");
        int col = scanner.nextInt() - 1;
        int row = scanner.nextInt() - 1;
        String command = scanner.next();
        if (this.isExplored(row, col)) {
            System.out.println("This cell is already explored!");
        } else {
            executeTurn(row, col, command);
        }
    }

    private void executeTurn(int row, int col, String command) {
        switch (command) {
            case "free":
                if (!boardSet) {
                    boardSet = true;
                    this.newBoard(row, col);
                    explore(row, col);
                }
                if (this.hasMineAt(row, col)) {
                    this.revealMinefield();
                    System.out.println("You stepped on a mine and failed!");
                    this.hasLost = true;
                } else {
                    explore(row, col);
                }
                break;
            case "mine":
                this.marked[row][col] = !this.marked[row][col];
        }
        this.checkWin();
        this.showMinefield();
    }

    private void newBoard(int initialRow, int initialCol){


        for (int i = Math.max(0, initialRow - 1); i <= Math.min(8, initialRow + 1); i++) {
            for (int j = Math.max(0, initialCol - 1); j <= Math.min(8, initialCol + 1); j++) {
                this.board[i][j] = -1;
            }
        }

        Random random = new Random();
        for (int i = 0; i < this.mines; i++) {
            int mineRow , mineCol;
            do {
                mineRow = random.nextInt(9);
                mineCol = random.nextInt(9);
            } while (this.board[mineRow][mineCol] == -1);
            this.board[mineRow][mineCol] = -1;
            this.mineLocations[mineRow][mineCol] = true;
        }

        for (int i = Math.max(0, initialRow - 1); i <= Math.min(8, initialRow + 1); i++) {
            for (int j = Math.max(0, initialCol - 1); j <= Math.min(8, initialCol + 1); j++) {
                this.board[i][j] = 0;
            }
        }

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (this.hasMineAt(i,j)) {
                    for (int k = Math.max(0, i - 1); k <= Math.min(8, i + 1) ; k++) {
                        for (int l = Math.max(0, j - 1); l <= Math.min(8, j + 1); l++) {
                            if (!(k == i && l == j)) {
                                this.board[k][l]++;
                            }
                        }
                    }
                }
            }
        }
        if (board[initialRow][initialCol] != 0) {
            System.out.println("Error with mine spawning");
        }
    }

    private void showMinefield() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < this.board.length; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < this.board[0].length; j++) {
                if (this.isMarked(i, j)) {
                    System.out.print("*");
                } else if (this.board[i][j] == 0 && this.isExplored(i, j)) {
                    System.out.print("/");
                } else if (this.isExplored(i, j)){
                    System.out.print(this.board[i][j]);
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
    }

    private void revealMinefield() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        for (int i = 0; i < this.board.length; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < this.board[0].length; j++) {
                if (this.hasMineAt(i, j)) {
                    System.out.print("X");
                } else if (this.board[i][j] == 0 && this.isExplored(i, j)) {
                    System.out.print("/");
                } else if (this.isExplored(i, j)){
                    System.out.print(this.board[i][j]);
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
    }


    private boolean hasMineAt(int row, int col) {
        return this.mineLocations[row][col];
    }

    private boolean isMarked(int row, int col) {
        return this.marked[row][col];
    }

    private boolean isExplored(int row, int col) {
        return this.explored[row][col];
    }

    private void explore(int row, int col) {

        this.explored[row][col] = true;
        this.marked[row][col] = false;

        if (this.board[row][col] == 0) {
            for (int i = Math.max(0, row - 1); i <= Math.min(8, row + 1) ; i++) {
                for (int j = Math.max(0, col - 1); j <= Math.min(8, col + 1) ; j++) {
                    if (!this.isExplored(i, j)) {
                        this.explore(i, j);
                    }
                }
            }
        }
    }
    private void checkWin(){
        if (this.marked == this.mineLocations) {
            this.hasWon = true;
            return;
        }
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (this.hasMineAt(i, j) && !this.isMarked(i,j)) {
                    return;
                }
            }
        }
        this.hasWon =  true;
    }


}

