package MineSweeperGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MineSweeperGame extends JFrame {
    private int numRows = 10;
    private int numColumns = 10;
    private int numMines = 20;
    private JButton[][] grid;
    private boolean[][] mineLocations;
    private boolean[][] revealed;
    private boolean gameOver = false;

    public MineSweeperGame() {
        grid = new JButton[numRows][numColumns];
        mineLocations = new boolean[numRows][numColumns];
        revealed = new boolean[numRows][numColumns];

        initializeBoard();
        placeMines();
        setLayout(new GridLayout(numRows, numColumns));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("지뢰찾기");
        pack();
    }

    private void initializeBoard() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                grid[row][col] = new JButton();
                grid[row][col].setPreferredSize(new Dimension(40, 40));
                add(grid[row][col]);
                int finalRow = row;
                int finalCol = col;
                grid[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!gameOver) {
                            handleButtonClick(finalRow, finalCol);
                        }
                    }
                });
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < numMines) {
            int row = random.nextInt(numRows);
            int col = random.nextInt(numColumns);

            if (!mineLocations[row][col]) {
                mineLocations[row][col] = true;
                minesPlaced++;
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        if (revealed[row][col]) {
            return;
        }

        revealed[row][col] = true;

        if (mineLocations[row][col]) {
            gameOver = true;
            showMines();
            JOptionPane.showMessageDialog(this, "지뢰를 밟았습니다. 게임 종료!");
        } else {
            int adjacentMines = countAdjacentMines(row, col);

            if (adjacentMines == 0) {
                revealEmptyTiles(row, col);
            } else {
                grid[row][col].setText(String.valueOf(adjacentMines));
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numColumns) {
                    if (mineLocations[newRow][newCol]) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private void revealEmptyTiles(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;

                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numColumns) {
                    if (!revealed[newRow][newCol]) {
                        handleButtonClick(newRow, newCol);
                        if (countAdjacentMines(newRow, newCol) == 0) {
                            revealEmptyTiles(newRow, newCol);
                        }
                    }
                }
            }
        }
    }


    private void showMines() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                if (mineLocations[row][col]) {
                    grid[row][col].setText("X");
                }
            }
        }
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MineSweeperGame game = new MineSweeperGame();
            game.setVisible(true);
        });
    }
}

