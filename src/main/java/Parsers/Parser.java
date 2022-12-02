package Parsers;

import java.io.IOException;
import java.util.HashMap;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
    private String parseTheTable() throws IOException{//разбивает сайт на одну строку со всеми значениями валют
        HashMap<String, Double> allCurrency = new HashMap<>();
        String url ="https://www.cbr.ru/currency_base/daily/";
        Document page = Jsoup.parse(new URL(url), 3000);//вытаскиваем всю страницу
        Element tableFirst = page.select("table[class=data]").first();//первая выборка
        Elements names = tableFirst.select("td");//вторая выборка
        String value = names.text();
        return value;
    }

    private String findInBigStr(String value, String currency) throws Exception {//Ищет определённую валюсту во всей строке
        Pattern pattern2 = Pattern.compile("\\d+,\\d+");//ищет значение в рублях
        Pattern pattern1 = Pattern.compile(currency+"\\s+\\d+\\D+\\d+,\\d+");//паттерн - для поиска инфы в тексте шаблон всё об одной валюте
        Matcher matcher = pattern1.matcher(value);
        if (matcher.find()) { //Первое сравнение с регуляркой
            String tmp = matcher.group();
            Matcher matcher1 = pattern2.matcher(tmp);

            if (matcher1.find()) { //Второе сравнение с регуляркой
                return matcher1.group();
            }
        }
        throw new Exception("Can't find this currency");
    }
    public HashMap<String, Double> takeADictionaryOfCurrency(String[] allKey)//создание Dictionary с курсом валют
            throws Exception {
        Parser parserObject1 = new Parser();
        String value = parserObject1.parseTheTable();
        HashMap<String, Double> dict = new HashMap();
        for(int i =0;i< allKey.length;i++) {
            if(allKey[i]=="RUB")dict.put("RUB", 1.0); // добавили только rub
            else { //добавление и остальных валют
                dict.put(allKey[i], Double.parseDouble(parserObject1.findInBigStr(value, allKey[i]).replaceAll(",",".")));
            }
        }
        return dict;
    }
}
