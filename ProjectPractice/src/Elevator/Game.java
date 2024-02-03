package Elevator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame {
    private JButton[] floorButtons;
    private JButton[] elevatorButtons;
    private JTextArea logArea;

    private boolean[] elevatorStatus;  // 엘리베이터 상태 배열 (true: 동작 중, false: 멈춤)
    private int[] elevatorFloors;      // 엘리베이터가 위치한 층 배열

    public Game() {
        setTitle("Elevator Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        elevatorStatus = new boolean[2];
        elevatorFloors = new int[2];

        floorButtons = new JButton[7];
        elevatorButtons = new JButton[2];
        logArea = new JTextArea();

        initializeUI();
        startGame();  // 게임 시작 메소드 호출
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel floorPanel = new JPanel(new GridLayout(7, 1));
        for (int i = 0; i < 7; i++) {
            floorButtons[i] = new JButton("Floor " + (i + 1));
            final int floor = i + 1;
            floorButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    requestElevator(floor);
                }
            });
            floorPanel.add(floorButtons[i]);
        }

        JPanel elevatorPanel = new JPanel(new GridLayout(1, 2));
        for (int i = 0; i < 2; i++) {
            elevatorButtons[i] = new JButton("Elevator " + (i + 1));
            final int elevatorIndex = i;
            elevatorButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveElevator(elevatorIndex);
                    checkAnswer(elevatorIndex);
                }
            });
            elevatorPanel.add(elevatorButtons[i]);
        }

        logArea.setEditable(false);

        add(floorPanel, BorderLayout.WEST);
        add(elevatorPanel, BorderLayout.CENTER);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);
    }

    private void startGame() {
        for (int i = 0; i < 2; i++) {
            elevatorFloors[i] = (int) (Math.random() * 7) + 1;  // 1부터 7까지 랜덤 층수 할당
        }
        log("Game started! Elevator 1 is on Floor " + elevatorFloors[0] + ", Elevator 2 is on Floor " + elevatorFloors[1]);
    }

    private void requestElevator(int floor) {
        int closestElevator = findClosestElevator(floor);
        moveElevatorTo(closestElevator, floor);
        log("Elevator " + (closestElevator + 1) + " is moving to Floor " + floor);
    }

    private int findClosestElevator(int floor) {
        int distanceElevator1 = Math.abs(elevatorFloors[0] - floor);
        int distanceElevator2 = Math.abs(elevatorFloors[1] - floor);
        return (distanceElevator1 <= distanceElevator2) ? 0 : 1;
    }

    private void moveElevator(int elevatorIndex) {
        int currentFloor = elevatorFloors[elevatorIndex];
        log("Elevator " + (elevatorIndex + 1) + " is on Floor " + currentFloor);
    }

    private void moveElevatorTo(int elevatorIndex, int targetFloor) {
        int[] currentFloor = {elevatorFloors[elevatorIndex]};  // 배열로 변경

        elevatorStatus[elevatorIndex] = true;

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFloor[0] < targetFloor) {
                    currentFloor[0]++;
                } else if (currentFloor[0] > targetFloor) {
                    currentFloor[0]--;
                } else {
                    elevatorStatus[elevatorIndex] = false;
                    ((Timer) e.getSource()).stop();

                    // 엘리베이터가 도착했을 때 새로운 문제 생성
                    startGame();
                }
                elevatorFloors[elevatorIndex] = currentFloor[0];
                log("Elevator " + (elevatorIndex + 1) + " is on Floor " + currentFloor[0]);
            }
        });
        timer.start();
    }

    private void checkAnswer(int elevatorIndex) {
        // 사용자가 선택한 엘리베이터가 정답인지 확인
        if (elevatorFloors[0] <= elevatorFloors[1]) {
            if (elevatorIndex == 0) {
                log("Correct! You guessed the right elevator!");
            } else {
                log("Incorrect! Try again.");
            }
        } else {
            if (elevatorIndex == 1) {
                log("Correct! You guessed the right elevator!");
            } else {
                log("Incorrect! Try again.");
            }
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game().setVisible(true);
            }
        });
    }
}
