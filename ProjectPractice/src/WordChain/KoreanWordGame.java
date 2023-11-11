package WordChain;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class KoreanWordGame extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private String apiKey = "API-KEY"; // 여기에 실제 API 키로 교체해주세요
    private String currentWord;

    public KoreanWordGame() {
        setTitle("한글 끝말잇기 게임");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 위치하도록 설정

        inputField = new JTextField();
        JButton submitButton = new JButton("제출");
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("단어 입력: "), BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.EAST);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitWord();
            }
        });

        // Enter 키로 단어 제출할 수 있도록 처리
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitWord();
                }
            }
        });

        // 게임 시작 시 랜덤한 단어로 시작
        currentWord = getRandomWord();
        outputArea.append("시작 단어: " + currentWord + "\n");

        // 시작 시 입력 필드에 포커스 주기
        inputField.requestFocusInWindow();
    }

    private void submitWord() {
        String inputWord = inputField.getText().trim();
        if (!inputWord.isEmpty()) {
            if (isValidInput(inputWord)) {
                if (currentWord == null || inputWord.startsWith(currentWord.substring(currentWord.length() - 1))) {
                    currentWord = inputWord;
                    String nextWord = findNextWord();
                    if (nextWord != null) {
                        outputArea.append("다음 단어: " + nextWord + "\n");
                        inputField.setText("");
                    } else {
                        outputArea.append("다음 단어를 찾을 수 없습니다.\n");
                    }
                } else {
                    outputArea.append("올바른 끝말을 이어야 합니다.\n");
                }
            } else {
                outputArea.append("올바른 한글 단어를 입력하세요.\n");
            }
        }
    }

    // 입력한 단어가 유효한지 확인하는 메서드
    private boolean isValidInput(String word) {
        // 여기에 단어의 유효성을 체크하는 로직 추가
        // 예를 들어, 한글 단어인지 확인하는 등의 로직을 추가할 수 있습니다.
        // 유효한 경우 true를 반환, 그렇지 않은 경우 false를 반환하도록 구현
    	return word.matches("^[가-힣]*$");
    }

 // 다음 단어를 찾아오는 메서드
    private String findNextWord() {
        try {
            String apiUrl = "https://stdict.korean.go.kr/api/search.do?key=" + apiKey + "&q=" + currentWord + "&req_type=json";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";

            List<String> words = parseApiResponse(response);

            // 유효한 다음 단어를 찾을 때까지 반복
            String nextWord;
            do {
                nextWord = getRandomWordFromList(words);
            } while (nextWord != null && !nextWord.startsWith(currentWord.substring(currentWord.length() - 1)));

            inputStream.close();
            connection.disconnect();

            return nextWord;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // API 응답을 파싱하여 단어 목록을 얻는 메서드
    private List<String> parseApiResponse(String response) {
    	return List.of("강아지", "기린", "날개", "에어컨", "냉장고");
    }

    // 단어 목록에서 랜덤한 단어를 얻는 메서드
    private String getRandomWordFromList(List<String> words) {
        if (words != null && !words.isEmpty()) {
            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        }
        return null;
    }

    // 미리 정의된 단어 중에서 랜덤한 단어를 얻는 메서드
    private String getRandomWord() {
    	return List.of("사과", "바나나", "딸기", "수박", "포도").get(new Random().nextInt(5));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KoreanWordGame().setVisible(true);
            }
        });
    }
}
