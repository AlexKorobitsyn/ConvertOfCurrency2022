package BestCurrencyExchangerBot.service;

import Parsers.BanksParser;
import Parsers.YandexParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class TownStructure {
    static final String[] alphabet = {"Евро", "Доллар", "Рубль"};

    public TownStructure() throws IOException {
    }

    private Set<String> banks;
    private HashMap<String, HashMap<String, HashMap<String, Double>>>  banksWithCurrencies;

    public static TownStructure getStructure(String town) throws IOException, ParseException {

        if (Objects.equals(town, "London")) {   // заглушка
            TownStructure townStructure = new TownStructure();
            townStructure.banks = new HashSet<>(List.of(new String[]{"Sberbank", "VTB"}));
            HashMap<String, HashMap<String, HashMap<String, Double>>> banksWithCurrencies = new HashMap<>();

            HashMap<String, Double> sellAndBuyForSberbankUSD = new HashMap<>();
            sellAndBuyForSberbankUSD.put("sell", 60.5);
            sellAndBuyForSberbankUSD.put("buy", 68.0);
            HashMap<String, Double> sellAndBuyForSberbankEUR = new HashMap<>();
            sellAndBuyForSberbankEUR.put("sell", 70.5);
            sellAndBuyForSberbankEUR.put("buy", 78.0);
            HashMap<String, Double> sellAndBuyForSberbankRUB = new HashMap<>();
            sellAndBuyForSberbankRUB.put("sell", 1.0);
            sellAndBuyForSberbankRUB.put("buy", 1.0);
            HashMap<String, HashMap<String, Double>> currenciesAndValuesForSberbank = new HashMap<>();
            currenciesAndValuesForSberbank.put("USD", sellAndBuyForSberbankUSD);
            currenciesAndValuesForSberbank.put("EUR", sellAndBuyForSberbankEUR);
            currenciesAndValuesForSberbank.put("RUB", sellAndBuyForSberbankRUB);

            HashMap<String, Double> sellAndBuyForVTBUSD = new HashMap<>();
            sellAndBuyForVTBUSD.put("sell", 59.5);
            sellAndBuyForVTBUSD.put("buy", 66.0);
            HashMap<String, Double> sellAndBuyForVTBEUR = new HashMap<>();
            sellAndBuyForVTBEUR.put("sell", 69.5);
            sellAndBuyForVTBEUR.put("buy", 75.0);
            HashMap<String, Double> sellAndBuyForVTBRUB = new HashMap<>();
            sellAndBuyForVTBRUB.put("sell", 1.0);
            sellAndBuyForVTBRUB.put("buy", 1.0);
            HashMap<String, HashMap<String, Double>> currenciesAndValuesForVTB = new HashMap<>();
            currenciesAndValuesForVTB.put("USD", sellAndBuyForVTBUSD);
            currenciesAndValuesForVTB.put("EUR", sellAndBuyForVTBEUR);
            currenciesAndValuesForVTB.put("RUB", sellAndBuyForVTBRUB);

            banksWithCurrencies.put("Sberbank", currenciesAndValuesForSberbank);
            banksWithCurrencies.put("VTB", currenciesAndValuesForVTB);

            townStructure.banksWithCurrencies = banksWithCurrencies;

            return townStructure;
        }

        YandexParser yaParser = new YandexParser();
        BanksParser bankParser = new BanksParser(alphabet);
        yaParser.setText("Город" + town + "Банки");
        Set<String> banks = yaParser.ParseTheYandexName(false);
        HashMap<String, HashMap<String, HashMap<String, Double>>>  banksWithCurrencies =
                bankParser.banksWithTheirExchangeRates(banks);

        TownStructure townStructure = new TownStructure();
        townStructure.banks = banks;
        townStructure.banksWithCurrencies = banksWithCurrencies;
        return townStructure;
    }

    public static Set<String> getCoordinates(String mainBank, String town) throws IOException, ParseException {
        if (Objects.equals(mainBank, "Сбербанк") && Objects.equals(town, "London")){ // заглушка
            Set<String> coordinates = new HashSet<>();
            coordinates.add("63.340774,61.314464");
            coordinates.add("63.323504,61.312341");
            coordinates.add("63.343912,61.31457");
            return coordinates;
        }
        YandexParser yaParser = new YandexParser();
        yaParser.setText("Город" + town + mainBank);
        return yaParser.ParseTheYandexName(true);
    }

    public Set<String> getBanks() {
        return banks;
    }

    public void setBanks(Set<String> banks) {
        this.banks = banks;
    }

    public HashMap<String, HashMap<String, HashMap<String, Double>>> getBanksWithCurrencies() {
        return banksWithCurrencies;
    }

    public void setBanksWithCurrencies(HashMap<String, HashMap<String, HashMap<String, Double>>> banksWithCurrencies) {
        this.banksWithCurrencies = banksWithCurrencies;
    }
}
