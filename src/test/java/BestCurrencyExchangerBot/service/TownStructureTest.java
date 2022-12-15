package BestCurrencyExchangerBot.service;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TownStructureTest {

    @org.junit.jupiter.api.Test
    void testGetStructure() throws IOException, ParseException {
        TownStructure expectedTownStructure = new TownStructure();
        expectedTownStructure.setBanks(new HashSet<>(List.of(new String[]{"Sberbank", "VTB"})));
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

        expectedTownStructure.setBanksWithCurrencies(banksWithCurrencies);

        assertEquals(expectedTownStructure.getBanks(), TownStructure.getStructure("London").getBanks());
        assertEquals(expectedTownStructure.getBanksWithCurrencies(), TownStructure.getStructure("London").getBanksWithCurrencies());
    }

    @Test
    void testGetCoordinates() throws IOException, ParseException {
        Set<String> ExpectedCoordinates = new HashSet<>();
        ExpectedCoordinates.add("63.340774,61.314464");
        ExpectedCoordinates.add("63.323504,61.312341");
        ExpectedCoordinates.add("63.343912,61.31457");
        assertEquals(ExpectedCoordinates, TownStructure.getCoordinates("Сбербанк", "London"));
    }
}