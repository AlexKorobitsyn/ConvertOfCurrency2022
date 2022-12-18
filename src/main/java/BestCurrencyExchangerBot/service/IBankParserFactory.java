package BestCurrencyExchangerBot.service;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public interface IBankParserFactory {
    Document getBankPage(String bankName) throws IOException;
    HashMap<String, HashMap<String, HashMap<String, Double>>> banksWithTheirExchangeRates(Set<String> banks) throws IOException;
    public HashMap<String, HashMap<String, Double>> parse(Document page);
}