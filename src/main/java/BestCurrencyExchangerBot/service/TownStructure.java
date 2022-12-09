package BestCurrencyExchangerBot.service;

import Parsers.BanksParser;
import Parsers.YandexParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class TownStructure {
    static final String[] alphabet = {"Евро", "Доллар", "Рубль"};

    public TownStructure() throws IOException {
    }

    private Set<String> banks;
    private HashMap<String, HashMap<String, HashMap<String, Double>>>  banks_with_currencies;

    public static TownStructure getStructure(String town) throws IOException, ParseException {
        YandexParser yaParser = new YandexParser();
        BanksParser bankParser = new BanksParser(alphabet);
        yaParser.setText("Город" + town + "Банки");
        Set<String> banks = yaParser.ParseTheYandexName(false);
        HashMap<String, HashMap<String, HashMap<String, Double>>>  banks_with_currencies =
                bankParser.banks_with_their_exchange_rates(banks);
        TownStructure townStructure = new TownStructure();
        townStructure.banks = banks;
        townStructure.banks_with_currencies = banks_with_currencies;
        return townStructure;
    }

    public static Set<String> getCoordinates(String mainBank, String town) throws IOException, ParseException {
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

    public HashMap<String, HashMap<String, HashMap<String, Double>>> getBanks_with_currencies() {
        return banks_with_currencies;
    }

    public void setBanks_with_currencies(HashMap<String, HashMap<String, HashMap<String, Double>>> banks_with_currencies) {
        this.banks_with_currencies = banks_with_currencies;
    }
}
