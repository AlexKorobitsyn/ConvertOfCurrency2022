package Parsers;

import BestCurrencyExchangerBot.service.BankParserForTest;
import BestCurrencyExchangerBot.service.IBankParserFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BanksParserTest {

    @Test
    void testGetBankPage() throws IOException {
        IBankParserFactory bankParser = new BankParserForTest();
        Document expectedResult = new Document("test");
        assertEquals(expectedResult.baseUri(), bankParser.getBankPage("BankForTest").baseUri());
    }

    @Test
    void testBanksWithTheirExchangeRates() throws IOException {
        IBankParserFactory banksParser = new BankParserForTest();
        Set<String> banks = new HashSet<>();
        banks.add("testBank1");
        banks.add("testBank2");

        HashMap<String, HashMap<String, HashMap<String, Double>>> expectedResult = new HashMap<>();

        for (String bank: banks) {
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
            expectedResult.put(bank, testBankWithCurrencies);
        }

        assertEquals(expectedResult, banksParser.banksWithTheirExchangeRates(banks));
    }

    @Test
    void testMakeOutputStr() {
        HashMap<String, HashMap<String, HashMap<String, Double>>> banksWithCurrencies = new HashMap<>();

        HashMap<String, HashMap<String, Double>> sberbankWithCurrencies = new HashMap<>();
        HashMap<String, Double> sellBuyUSDSber = new HashMap<>();
        sellBuyUSDSber.put("sell", 69.0);
        sellBuyUSDSber.put("buy", 73.0);
        HashMap<String, Double> sellBuyEURSber = new HashMap<>();
        sellBuyEURSber.put("sell", 70.0);
        sellBuyEURSber.put("buy", 85.0);
        HashMap<String, Double> sellBuyRUBSber = new HashMap<>();
        sellBuyRUBSber.put("sell", 1.0);
        sellBuyRUBSber.put("buy", 1.0);
        sberbankWithCurrencies.put("EUR", sellBuyEURSber);
        sberbankWithCurrencies.put("USD", sellBuyUSDSber);
        sberbankWithCurrencies.put("RUB", sellBuyRUBSber);
        banksWithCurrencies.put("Sberbank", sberbankWithCurrencies);

        HashMap<String, HashMap<String, Double>> vtbWithCurrencies = new HashMap<>();
        HashMap<String, Double> sellBuyUSDVtb = new HashMap<>();
        sellBuyUSDVtb.put("sell", 50.0);
        sellBuyUSDVtb.put("buy", 60.0);

        HashMap<String, Double> sellBuyEURVtb = new HashMap<>();
        sellBuyEURVtb.put("sell", 70.0);
        sellBuyEURVtb.put("buy", 80.0);
        HashMap<String, Double> sellBuyRUBVtb = new HashMap<>();
        sellBuyRUBVtb.put("sell", 1.0);
        sellBuyRUBVtb.put("buy", 1.0);
        vtbWithCurrencies.put("EUR", sellBuyEURVtb);
        vtbWithCurrencies.put("USD", sellBuyUSDVtb);
        vtbWithCurrencies.put("RUB", sellBuyRUBVtb);
        banksWithCurrencies.put("VTB", vtbWithCurrencies);

        String expectedResult = """
                VTB
                EUR: sell: 70.0 buy: 80.0\s
                USD: sell: 50.0 buy: 60.0\s
                RUB: sell: 1.0 buy: 1.0\s


                Sberbank
                EUR: sell: 70.0 buy: 85.0\s
                USD: sell: 69.0 buy: 73.0\s
                RUB: sell: 1.0 buy: 1.0\s


                """;

        assertEquals(expectedResult, BanksParser.makeOutputStr(banksWithCurrencies));
    }

    @Test
    void testMakeDictForCalculate() {
        String type = "sell";
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

        HashMap<String, Double> expectedResult = new HashMap<>();
        expectedResult.put("USD", 69.0);
        expectedResult.put("EUR", 70.0);
        expectedResult.put("RUB", 1.0);

        assertEquals(expectedResult, BanksParser.makeDictForCalculate(bankWithCurrencies, type));
    }

    @Test
    void testParse() {
        String[] alphabet = {"Евро", "Доллар", "Рубль"};
        IBankParserFactory banksParser = new BankParserForTest();
        Document testPage = new Document("test");

        HashMap<String, HashMap<String, Double>> expectedResult = new HashMap<>();
        HashMap<String, Double> sellBuyForUSD = new HashMap<>();
        sellBuyForUSD.put("sell", 69.0);
        sellBuyForUSD.put("buy", 73.0);
        HashMap<String, Double> sellBuyForEUR = new HashMap<>();
        sellBuyForEUR.put("sell", 70.0);
        sellBuyForEUR.put("buy", 85.0);
        HashMap<String, Double> sellBuyForRUB = new HashMap<>();
        sellBuyForRUB.put("sell", 1.0);
        sellBuyForRUB.put("buy", 1.0);
        expectedResult.put("EUR", sellBuyForEUR);
        expectedResult.put("USD", sellBuyForUSD);
        expectedResult.put("RUB", sellBuyForRUB);

        assertEquals(expectedResult, banksParser.parse(testPage));
    }
}