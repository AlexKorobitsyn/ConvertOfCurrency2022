package Parsers;

import BestCurrencyExchangerBot.service.IBankParserFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class BanksParser implements IBankParserFactory {
    private final HashMap<String, String> bankAndRelatedUrl; // Словарь {Банк: URL}
    private final String[] alphabet; // Массив валют
    public BanksParser(String[] alphabet) {
        bankAndRelatedUrl = new HashMap<>();
        bankAndRelatedUrl.put("СберБанк", "https://ru.myfin.by/bank/sberbank/currency");
        bankAndRelatedUrl.put("ВТБ", "https://ru.myfin.by/bank/vtb/currency");
        bankAndRelatedUrl.put("Альфа-Банк", "https://ru.myfin.by/bank/alfabank/currency");
        bankAndRelatedUrl.put("Газпромбанк", "https://ru.myfin.by/bank/gazprombank/currency");
        bankAndRelatedUrl.put("Россельхозбанк", "https://ru.myfin.by/bank/rshb/currency");
        bankAndRelatedUrl.put("Московский кредитный банк", "https://ru.myfin.by/bank/mkb/currency");
        bankAndRelatedUrl.put("Банк Открытие", "https://ru.myfin.by/bank/otkritie/currency");
        bankAndRelatedUrl.put("МТС банк", "https://ru.myfin.by/bank/mts-bank/currency");
        this.alphabet = alphabet;
    }
    @Override
    public Document getBankPage(String bankName) throws IOException {
        String url = bankAndRelatedUrl.get(bankName);
        return Jsoup.parse(new URL(url), 3000);
    }

    @Override
    public HashMap<String, HashMap<String, HashMap<String, Double>>> banksWithTheirExchangeRates(Set<String> banks) throws IOException {
        HashMap<String, HashMap<String, HashMap<String, Double>>> resultDict = new HashMap<>();
        for (String bank: banks) {
            if (bankAndRelatedUrl.containsKey(bank)){
                Document page = getBankPage(bank);
                resultDict.put(bank,parse(page));
            }
        }
        return resultDict;
    }
    @Override
    public HashMap<String, HashMap<String, Double>> parse(Document page){
        HashMap<String, HashMap<String, Double>> dictWithCurrencyAndValues = new HashMap<>();

        Element tableFirst = page.select("table[class=table-best white_bg]").first(); //выборка
        String content = tableFirst.text();

        for (String currency : alphabet) { // бежим по валютам
            HashMap<String, Double> dictForSellAndBuyValue = new HashMap<>();
            if (Objects.equals(currency, "Рубль")){ // обрабатываем рубль, т.к. его курса нет
                dictForSellAndBuyValue.put("sell", 1.0);
                dictForSellAndBuyValue.put("buy", 1.0);
            }
            else { // обрабатываем остальные валюты
                Pattern patternForValues = Pattern.compile(currency + " \\d*\\.?\\d* \\d*\\.?\\d*");
                Matcher matcher = patternForValues.matcher(content);
                if (matcher.find()){
                    String[] parts = matcher.group().split(" ");
                    dictForSellAndBuyValue.put("sell", Double.parseDouble(parts[parts.length - 2]));
                    dictForSellAndBuyValue.put("buy", Double.parseDouble(parts[parts.length - 1]));
                }
            }
            dictWithCurrencyAndValues.put(currency, dictForSellAndBuyValue);
        }
        return dictWithCurrencyAndValues;
    }

    public static String makeOutputStr(HashMap<String, HashMap<String, HashMap<String, Double>>> banksWithCurrencies){
        StringBuilder result = new StringBuilder();
        Set<String> banks = banksWithCurrencies.keySet();
        for (String bank : banks){
            result.append(bank).append('\n');
            Set<String> currencies = banksWithCurrencies.get(bank).keySet();
            for (String currency : currencies) {
                if (currency.equals("Рубль")) {
                    continue;
                }
                result.append(currency).append(": ");
                HashMap<String, Double> typesWithValues = banksWithCurrencies.get(bank).get(currency);
                for (String type : typesWithValues.keySet()){
                    result.append(type).append(": ").append(typesWithValues.get(type)).append(" ");
                }
                result.append("\n");
            }
            result.append("\n\n");
        }

        return result.toString();
    }
    public static HashMap<String, Double> makeDictForCalculate( // получение словаря {валюта : значение} для вычислений
            HashMap<String, HashMap<String, Double>> bankWithCurrencies, String type
    )
    {
        HashMap<String, Double> currenciesValues = new HashMap<>();
        for (String currency : bankWithCurrencies.keySet()){
            String newCurrencyForm = switch (currency) {
                case "Евро" -> "EUR";
                case "Доллар" -> "USD";
                case "Рубль" -> "RUB";
                default -> currency;
            };
            currenciesValues.put(newCurrencyForm, bankWithCurrencies.get(currency).get(type));
        }
        return currenciesValues;
    }
}