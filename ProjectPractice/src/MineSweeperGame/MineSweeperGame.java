package MineSweeperGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;

public class MineSweeperGame extends JFrame {
    private int numRows;
    private int numColumns;
    private int numMines;
    private JButton[][] grid;
    private boolean[][] mineLocations;
    private boolean[][] revealed;
    private boolean gameOver;

    public MineSweeperGame(int numRows, int numColumns, int numMines) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.numMines = numMines;

        grid = new JButton[numRows][numColumns];
        mineLocations = new boolean[numRows][numColumns];
        revealed = new boolean[numRows][numColumns];

        initializeBoard();
        placeMines();
        setLayout(new GridLayout(numRows, numColumns));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("지뢰찾기");
        pack();

        setLocationRelativeTo(null);
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
            int choice = JOptionPane.showConfirmDialog(this, "지뢰를 밟았습니다. 게임 종료! 다시 시작하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        } else {
            int adjacentMines = countAdjacentMines(row, col);

            if (adjacentMines == 0) {
                revealEmptyTiles(row, col);
            } else {
                grid[row][col].setText(String.valueOf(adjacentMines));
            }

            if (checkGameWin()) {
                gameOver = true;
                showMines();
                int choice = JOptionPane.showConfirmDialog(this, "축하합니다! 모든 지뢰를 찾았습니다. 게임 종료! 다시 시작하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    restartGame();
                } else {
                    System.exit(0);
                }
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

    private void restartGame() {
        // Reset all game variables
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                grid[row][col].setText("");
                mineLocations[row][col] = false;
                revealed[row][col] = false;
            }
        }

        placeMines();
        gameOver = false;
    }

    private boolean checkGameWin() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                if (!mineLocations[row][col] && !revealed[row][col]) {
                    return false; // 아직 모든 빈 칸이 열리지 않았으면 게임 승리 조건 충족하지 않음
                }
            }
        }
        return true; // 모든 빈 칸이 열렸으면 게임 승리
    }
    
    private static MineSweeperGame initializeGame() {
        String[] options = {"초급", "중급", "고급"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "난이도를 선택하세요",
                "난이도 선택",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        MineSweeperGame game;

        switch (choice) {
            case 0: // 초급
                game = new MineSweeperGame(9, 9, 10);
                break;
            case 1: // 중급
                game = new MineSweeperGame(16, 16, 40);
                break;
            case 2: // 고급
                game = new MineSweeperGame(16, 30, 99);
                break;
            default:
                throw new IllegalStateException("예상치 못한 값: " + choice);
        }

        return game;
    }

    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {	
            try {
                MineSweeperGame game = initializeGame();
                game.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
