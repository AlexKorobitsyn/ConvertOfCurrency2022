import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class BanksParser {
    private final HashMap<String, String> bank_and_related_url; // Словарь {Банк: URL}
    private final String[] alphabet; // Массив валют
    public BanksParser(HashMap<String, String> bank_and_related_url, String[] alphabet) {
        this.bank_and_related_url = bank_and_related_url;
        this.alphabet = alphabet;
    }

    public Document get_bank_page(String bank_name) throws IOException {
        String url = bank_and_related_url.get(bank_name);
        return Jsoup.parse(new URL(url), 3000);
    }

    public HashMap<String, HashMap<String, HashMap<String, Double>>> banks_with_their_exchange_rates(String[] banks) throws IOException {
        HashMap<String, HashMap<String, HashMap<String, Double>>> result_dict = new HashMap<>();
        for (String bank: banks) {
            if (bank_and_related_url.containsKey(bank)){
                Document page = get_bank_page(bank);
                result_dict.put(bank,parse(page));
            }
        }
        return result_dict;
    }
    public HashMap<String, HashMap<String, Double>> parse(Document page){
        Element tableFirst = page.select("table[class=table-best white_bg]").first(); //выборка
        String content = tableFirst.text();
        HashMap<String, HashMap<String, Double>> dict_with_currency_and_values = new HashMap<>();
        for (String currency : alphabet) { // бежим по валютам
            HashMap<String, Double> dict_for_sell_and_buy_value = new HashMap<>();
            if (Objects.equals(currency, "Рубль")){ // обрабатываем рубль, т.к. его курса нет
                dict_for_sell_and_buy_value.put("sell", 1.0);
                dict_for_sell_and_buy_value.put("buy", 1.0);
            }
            else { // обрабатываем остальные валюты
                Pattern pattern_for_values = Pattern.compile(currency + " \\d*\\.?\\d* \\d*\\.?\\d*");
                Matcher matcher = pattern_for_values.matcher(content);
                if (matcher.find()){
                    String[] parts = matcher.group().split(" ");
                    dict_for_sell_and_buy_value.put("sell", Double.parseDouble(parts[parts.length - 2]));
                    dict_for_sell_and_buy_value.put("buy", Double.parseDouble(parts[parts.length - 1]));
                }
            }
            dict_with_currency_and_values.put(currency, dict_for_sell_and_buy_value);
        }
        return dict_with_currency_and_values;
    }
}