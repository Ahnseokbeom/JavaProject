package WordChain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KoreanDictionary {
    private static final String API_URL = "https://seokbeom.korean.go.kr/api/search";
    private static final String API_KEY = "F03BB3FC03DF5BD3ADABD9A015FCC196";

    public static String getRandomWordStartingWith(char lastChar) {
        try {
            String apiUrl = API_URL + "?key=" + API_KEY + "&type_search=search1&method1=TARGET_CODE&type1_code=2&query=";
            char startChar = lastChar == 'ㅎ' ? 'ㄱ' : (char) (lastChar + 1);
            Document doc = Jsoup.connect(apiUrl + startChar).get();

            Elements items = doc.select("item");
            List<String> words = new ArrayList<>();

            for (Element item : items) {
                String word = item.selectFirst("word").text();
                words.add(word);
            }

            Random random = new Random();
            return words.get(random.nextInt(words.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
