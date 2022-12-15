package CalculateClasses;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest {

    @Test
    void testInput() throws Exception {
        Interface testInterface = new Interface();
        String expression1 = "10 USD + 20 EUR in RUB";
        HashMap<String, Double> currenciesValues1 = new HashMap<>();
        currenciesValues1.put("USD", 10.0);
        currenciesValues1.put("RUB", 1.0);
        currenciesValues1.put("EUR", 20.0);
        String expectedResult1 = "500,000 RUB";
        assertEquals(expectedResult1, testInterface.input(expression1, currenciesValues1));

        String expression2 = "10 USDDDD + 20 EUR in RUB";
        String expectedResult2 = "Wrong input, check /help";
        assertEquals(expectedResult2, testInterface.input(expression2,currenciesValues1));

        String expression3 = "5 USD - 100 RUB in EUR";
        HashMap<String, Double> currenciesValues2 = new HashMap<>();
        currenciesValues2.put("USD", 50.0);
        currenciesValues2.put("RUB", 1.0);
        currenciesValues2.put("EUR", 100.0);
        String expectedResult3 = "1,500 EUR";
        assertEquals(expectedResult3, testInterface.input(expression3, currenciesValues2));
    }

    @Test
    void testOutput(){
        Interface testInterface = new Interface();
        double resultValue1 = 100.500;
        String finalCurrency1 = "USD";
        String expectedResult1 = "100,500 USD";
        assertEquals(expectedResult1, testInterface.output(resultValue1, finalCurrency1));

        double resultValue2 = 20.012;
        String finalCurrency2 = "RUB";
        String expectedResult2 = "20,012 RUB";
        assertEquals(expectedResult2, testInterface.output(resultValue2, finalCurrency2));
    }

}