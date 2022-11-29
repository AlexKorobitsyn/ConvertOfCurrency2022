import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class YandexParser {

    public YandexParser() throws IOException {
    }
    static private final Path path = Paths.get("src\\main\\resources\\keys.txt");

    private String text;
    private final String APIKey = Files.readString(path);// Безопасность ключа.
    private int results = 500;

    private String GiveSecretKey()//придётся сохранять готовую модель ссылки с ключом
    {
        String link = "https://search-maps.yandex.ru/v1/?type=biz&lang=ru_RU";
        link += "&apikey=" + APIKey ;
        return link;
    }

    private String MakeALink(String model)
    {
        String URL = model +"&text=" + text + "&results=" + results;
        return URL;
    }
//name - 0(false), coordinate - 1(true)
    public Set<String> ParseTheYandexName(boolean key) throws IOException, ParseException {//Вторая функция Desicion
        String URL1 = MakeALink(GiveSecretKey());
        Document page  = Jsoup.connect(URL1).ignoreContentType(true).get();
        String str = page.text();
        Scanner scan = new Scanner(str);
        Set<String> result = new HashSet<>();
        int i = 0;
        if (key) {
            setResults(50);
            while (true) {
                String goodString = scan.findInLine("(?<=\"coordinates\":.).+?(?=\\])");
                if (goodString == null)
                    break;
                result.add(goodString.toString());
                i++;
            }
        }
        else
        {
            while (true) {
                setResults(500);
                String goodString = scan.findInLine("(?<=\"properties\":\\{\"name\":\").+?(?=\")");
                if (goodString == null)
                    break;
                result.add(goodString.toString());
                i++;
            }
        }
        return result;
    }


    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
