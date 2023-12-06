package Omok;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OmokGame extends JFrame {
    private static final int SIZE = 15;
    private static final int CELL_SIZE = 30;
    private static final char EMPTY = '.';
    private static final char PLAYER1 = 'X';
    private static final char PLAYER2 = 'O';

    private char[][] board;
    private boolean isPlayer1Turn;

    public OmokGame() {
        setTitle("Omok Game");
        setSize(SIZE * CELL_SIZE, SIZE * CELL_SIZE); // 내부 컴포넌트와 테두리 크기 고려
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치

        board = new char[SIZE][SIZE];
        initializeBoard();
        isPlayer1Turn = true;

        if (!isPlayer1Turn) {
            computerMove(-1, -1); // 초기화 시 사용자가 아직 돌을 두지 않았으므로 임의의 값 전달
            isPlayer1Turn = true;
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;

                if (isPlayer1Turn && makeMove(row, col, PLAYER1)) {
                    if (checkWin(row, col, PLAYER1)) {
                        handleGameEnd("플레이어 1 (X)가 이겼습니다! 재시작하시겠습니까?");
                    } else {
                        isPlayer1Turn = false;
                        computerMove(row, col); // 사용자가 둔 위치의 좌표 전달
                        if (checkWin(row, col, PLAYER2)) {
                            handleGameEnd("플레이어 2 (O)가 이겼습니다! 재시작하시겠습니까?");
                        }
                        isPlayer1Turn = true;
                    }
                    repaint();
                }
            }
        });

        setVisible(true);
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private boolean makeMove(int row, int col, char player) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != EMPTY) {
            return false; // 유효하지 않은 수
        }

        board[row][col] = player;
        return true;
    }

    private boolean checkWin(int row, int col, char player) {
        return checkLine(row, col, player, 0, 1) ||
                checkLine(row, col, player, 1, 0) ||
                checkLine(row, col, player, 1, 1) ||
                checkLine(row, col, player, 1, -1);
    }

    private boolean checkLine(int row, int col, char player, int rowDirection, int colDirection) {
        int count = 1;

        for (int i = 1; i <= 4; i++) {
            int r = row + i * rowDirection;
            int c = col + i * colDirection;

            if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
                count++;
            } else {
                break; // 끊기면 루프 종료
            }
        }

        for (int i = 1; i <= 4; i++) {
            int r = row - i * rowDirection;
            int c = col - i * colDirection;

            if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
                count++;
            } else {
                break; // 끊기면 루프 종료
            }
        }

        return count == 5;
    }

    private void computerMove(int userRow, int userCol) {
        int range = 2; // 사용자가 둔 돌 주변으로 놓을 수 있는 범위

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                int newRow = userRow + i;
                int newCol = userCol + j;

                if (isValidMove(newRow, newCol) && makeMove(newRow, newCol, PLAYER2)) {
                    return; // 주변에 둘 수 있는 곳을 찾으면 둔다.
                }
            }
        }

        // 주변에 둘 수 있는 곳이 없다면 랜덤 위치에 둠
        computerRandomMove();
    }

    
    private int countConsecutiveStones(int row, int col, char player) {
        int count = 0;

        // 오른쪽 방향으로 검사
        for (int i = col + 1; i < SIZE && board[row][i] == player; i++) {
            count++;
        }

        // 왼쪽 방향으로 검사
        for (int i = col - 1; i >= 0 && board[row][i] == player; i--) {
            count++;
        }

        return count;
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY;
    }

    private void computerRandomMove() {
        int row, col;
        do {
            row = (int) (Math.random() * SIZE);
            col = (int) (Math.random() * SIZE);
        } while (!makeMove(row, col, PLAYER2));
    }

    private void handleGameEnd(String message) {
        int choice = JOptionPane.showConfirmDialog(this, message, "게임 종료", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        initializeBoard();
        if (!isPlayer1Turn) {
            computerMove(-1,-1);
            isPlayer1Turn = true;
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 보드 그리기
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                g.setColor(Color.BLACK);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                if (board[i][j] == PLAYER1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(j * CELL_SIZE + 5, i * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                } else if (board[i][j] == PLAYER2) {
                    g.setColor(Color.WHITE);
                    g.fillOval(j * CELL_SIZE + 5, i * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OmokGame::new);
    }
}
