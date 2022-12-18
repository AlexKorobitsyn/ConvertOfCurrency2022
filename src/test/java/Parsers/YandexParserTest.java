package Parsers;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class YandexParserTest {

    @Test
    void parseTheYandexName() {
        YandexParserForTest yaParser = new YandexParserForTest();

        Set<String> expectedResult1 = new HashSet<>();
        expectedResult1.add("ВТБ");
        expectedResult1.add("СберБанк");
        assertEquals(expectedResult1, yaParser.ParseTheYandexName(false));

        Set<String> expectedResult2 = new HashSet<>();
        expectedResult2.add("60.4345");
        expectedResult2.add("59.5930");
        assertEquals(expectedResult2, yaParser.ParseTheYandexName(true));
    }

    @Test
    void testTakeTheTown() {
        YandexParserForTest yaParser = new YandexParserForTest();
        String expectedResult = "TestTown";
        assertEquals(expectedResult, yaParser.takeTheTown());
    }
}