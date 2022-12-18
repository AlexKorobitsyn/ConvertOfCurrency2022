package BestCurrencyExchangerBot.service;

import java.io.IOException;
import java.util.Set;

public interface ITownStructureFactory {
    TownStructure buildTownStructure(String town) throws IOException;

    Set<String> getCoordinates(String mainBank, String town) throws IOException;
}
