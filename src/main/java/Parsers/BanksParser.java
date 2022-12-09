package Parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class BanksParser {
    private final HashMap<String, String> bank_and_related_url; // Словарь {Банк: URL}
    private final String[] alphabet; // Массив валют
    public BanksParser(String[] alphabet) {
        bank_and_related_url = new HashMap<>();
        bank_and_related_url.put("СберБанк", "https://ru.myfin.by/bank/sberbank/currency");
        bank_and_related_url.put("ВТБ", "https://ru.myfin.by/bank/vtb/currency");
        bank_and_related_url.put("Альфа-Банк", "https://ru.myfin.by/bank/alfabank/currency");
        bank_and_related_url.put("Газпромбанк", "https://ru.myfin.by/bank/gazprombank/currency");
        bank_and_related_url.put("Россельхозбанк", "https://ru.myfin.by/bank/rshb/currency");
        bank_and_related_url.put("Московский кредитный банк", "https://ru.myfin.by/bank/mkb/currency");
        bank_and_related_url.put("Банк Открытие", "https://ru.myfin.by/bank/otkritie/currency");
        bank_and_related_url.put("МТС банк", "https://ru.myfin.by/bank/mts-bank/currency");
        this.alphabet = alphabet;
    }

    public Document get_bank_page(String bank_name) throws IOException {
        String url = bank_and_related_url.get(bank_name);
        return Jsoup.parse(new URL(url), 3000);
    }

    public HashMap<String, HashMap<String, HashMap<String, Double>>> banks_with_their_exchange_rates(Set<String> banks) throws IOException {
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

    public static String makeOutputStr(HashMap<String, HashMap<String, HashMap<String, Double>>> banks_with_currencies){
        String result = "";
        Set<String> banks = banks_with_currencies.keySet();
        for (String bank : banks){
            result += bank + '\n';
            Set<String> currencies = banks_with_currencies.get(bank).keySet();
            for (String currency : currencies) {
                if (currency.equals("Рубль")) {
                    continue;
                }
                result += currency + ": ";
                HashMap<String, Double> types_with_values = banks_with_currencies.get(bank).get(currency);
                for (String type : types_with_values.keySet()){
                    result += type + ": " + types_with_values.get(type) + " ";
                }
                result += "\n";
            }
            result += "\n\n";
        }

        return result;
    }
    public static HashMap<String, Double> make_dict_for_calculate( // получение словаря валюта - значение для вычислений
            HashMap<String, HashMap<String, Double>> bank_with_currencies, String type
    )
    {
        HashMap<String, Double> currencies_values = new HashMap<>();
        for (String currency : bank_with_currencies.keySet()){
            String new_currency_form = switch (currency) {
                case "Евро" -> "EUR";
                case "Доллар" -> "USD";
                case "Рубль" -> "RUB";
                default -> "";
            };
            currencies_values.put(new_currency_form, bank_with_currencies.get(currency).get(type));
        }
        return currencies_values;
    }
}