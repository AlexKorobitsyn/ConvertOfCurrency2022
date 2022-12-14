package Parsers;

import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class YandexParser {

    public YandexParser() throws IOException {
    }
    private String geocode;
    private String text;
    private final String[] tmp = {"СберБанк", "ВТБ", "Газпромбанк", "Альфа-Банк",
            "Россельхозбанк", "Московский кредитный банк", "МТС банк", "Банк Открытие"};
    private final String APIKeyForSearch = System.getProperty("apiKeyForSearch");// Безопасность ключа.

    private final String apiKeyForGeocoder = System.getProperty("apiKeyForGeocoder");
    private int results = 500;


    private String makeALinkGeocoder()
    {
        String link = "https://geocode-maps.yandex.ru/1.x/?apikey=";
        link += apiKeyForGeocoder + "&geocode=" + geocode + "&results=" + 1;
        return link;
    }

    private String makeALink()
    {
        return ("https://search-maps.yandex.ru/v1/?type=biz&lang=ru_RU"+ "&apikey=" + APIKeyForSearch  )
                +("&text=" + text + "&results=" + results);
    }
    //name - 0(false), coordinate - 1(true)
    public Set<String> ParseTheYandexName(boolean key) throws IOException, ParseException {//Вторая функция Desicion
        if (key)
            setResults(50);
        else
            setResults(500);
        String url1 = makeALink();
        Document page  = Jsoup.connect(url1).ignoreContentType(true).get();
        String str = page.text();
        Scanner scan = new Scanner(str);
        Set<String> unfilteredResult = new HashSet<>();
        int i = 0;
        if (key) {

            while (true) {
                String goodString = scan.findInLine("(?<=\"coordinates\":.).+?(?=\\])");
                if (goodString == null)
                    break;
                unfilteredResult.add(goodString);
                i++;
            }
           return unfilteredResult;
        }
        else
        {
            YandexParser ss;
            try {
                ss = new YandexParser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Set<String> result = new HashSet<>();

            while (true) {
                String goodString = scan.findInLine("(?<=\"properties\":\\{\"name\":\").+?(?=\")");
                if (goodString == null)
                    break;
                unfilteredResult.add(goodString);
                i++;
            }
            for (int k = 0; k < ss.tmp.length; k++)
            {
                if(unfilteredResult.contains(ss.tmp[k]))
                {
                    result.add(ss.tmp[k]);
                }
            }
            return result;
        }
    }
    public String takeTheTown() throws IOException {
        String url1 = makeALinkGeocoder();
        Document page  = Jsoup.connect(url1).ignoreContentType(true).get();
        String str = page.text();
        Scanner scan = new Scanner(str);
        String goodString = scan.findInLine("(?<=locality).+?(?=[A-Za-z])");
        return goodString;
    }

    public static void main(String[] args) throws IOException {

        YandexParser yy = new YandexParser();
        yy.setGeocode("60.607436,56.774992");
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

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }
}
