package BestCurrencyExchangerBot.service;

import Parsers.BanksParser;
import Parsers.YandexParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class TownStructure implements ITownStructureFactory {
    static final String[] alphabet = {"Евро", "Доллар", "Рубль"};

    public TownStructure() {
    }

    private Set<String> banks;
    private HashMap<String, HashMap<String, HashMap<String, Double>>>  banksWithCurrencies;
    @Override
    public TownStructure buildTownStructure(String town) throws IOException {
        YandexParser yaParser = new YandexParser();
        IBankParserFactory bankParser = new BanksParser(alphabet);
        yaParser.setText("Город" + town + "Банки");
        Set<String> banks = yaParser.ParseTheYandexName(false);
        HashMap<String, HashMap<String, HashMap<String, Double>>>  banksWithCurrencies =
                bankParser.banksWithTheirExchangeRates(banks);

        TownStructure townStructure = new TownStructure();
        townStructure.banks = banks;
        townStructure.banksWithCurrencies = banksWithCurrencies;
        return townStructure;
    }

    public Set<String> getCoordinates(String mainBank, String town) throws IOException {
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
