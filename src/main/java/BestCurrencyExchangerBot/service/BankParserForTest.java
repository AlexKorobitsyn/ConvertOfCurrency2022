package BestCurrencyExchangerBot.service;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class BankParserForTest implements IBankParserFactory {
    @Override
    public Document getBankPage(String bankName) {
        return new Document("test");
    }

    @Override
    public HashMap<String, HashMap<String, HashMap<String, Double>>> banksWithTheirExchangeRates(Set<String> banks) throws IOException {
        HashMap<String, HashMap<String, HashMap<String, Double>>> resultDict = new HashMap<>();
        for (String bank: banks) {
            if (bank.startsWith("testBank")) { // заглушка
                HashMap<String, HashMap<String, Double>> testBankWithCurrencies = new HashMap<>();
                HashMap<String, Double> sellBuyUSD = new HashMap<>();
                sellBuyUSD.put("sell", 69.0);
                sellBuyUSD.put("buy", 73.0);
                HashMap<String, Double> sellBuyEUR = new HashMap<>();
                sellBuyEUR.put("sell", 70.0);
                sellBuyEUR.put("buy", 85.0);
                HashMap<String, Double> sellBuyRUB = new HashMap<>();
                sellBuyRUB.put("sell", 1.0);
                sellBuyRUB.put("buy", 1.0);
                testBankWithCurrencies.put("EUR", sellBuyEUR);
                testBankWithCurrencies.put("USD", sellBuyUSD);
                testBankWithCurrencies.put("RUB", sellBuyRUB);
                resultDict.put(bank, testBankWithCurrencies);
            }
        }
        return resultDict;
    }

    @Override
    public HashMap<String, HashMap<String, Double>> parse(Document page) {
        HashMap<String, HashMap<String, Double>> bankWithCurrencies = new HashMap<>();
        HashMap<String, Double> sellBuyForUSD = new HashMap<>();
        sellBuyForUSD.put("sell", 69.0);
        sellBuyForUSD.put("buy", 73.0);
        HashMap<String, Double> sellBuyForEUR = new HashMap<>();
        sellBuyForEUR.put("sell", 70.0);
        sellBuyForEUR.put("buy", 85.0);
        HashMap<String, Double> sellBuyForRUB = new HashMap<>();
        sellBuyForRUB.put("sell", 1.0);
        sellBuyForRUB.put("buy", 1.0);
        bankWithCurrencies.put("EUR", sellBuyForEUR);
        bankWithCurrencies.put("USD", sellBuyForUSD);
        bankWithCurrencies.put("RUB", sellBuyForRUB);

        return bankWithCurrencies;
    }
}
