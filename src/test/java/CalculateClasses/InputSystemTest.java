package CalculateClasses;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InputSystemTest {

    @Test
    void testIinputProcessor() throws Exception {
        InputSystem inputSystem = new InputSystem();

        String expression1 = "10     USD - 9 USD in   RUB";
        HashMap<String, Double> currenciesValues1 = new HashMap<>();
        currenciesValues1.put("USD", 10.0);
        currenciesValues1.put("RUB", 1.0);
        currenciesValues1.put("EUR", 20.0);
        String expectedResult1 = "10,000 RUB";
        assertEquals(expectedResult1, inputSystem.inputProcessor(expression1, currenciesValues1));

        String expression2 = "5       EUR   in   RUB";
        HashMap<String, Double> currenciesValues2 = new HashMap<>();
        currenciesValues2.put("USD", 70.0);
        currenciesValues2.put("RUB", 1.0);
        currenciesValues2.put("EUR", 100.0);
        String expectedResult2 = "500,000 RUB";
        assertEquals(expectedResult2, inputSystem.inputProcessor(expression2, currenciesValues2));
    }
}