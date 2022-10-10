import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InputProcessingTest {
    @org.junit.jupiter.api.Test
    void testProcessMethod() throws Exception {
        HashMap<String,Double> testDict1 = new HashMap<>();
        testDict1.put("RUB", 1.0);
        testDict1.put("EUR", 43.0);
        testDict1.put("USD", 55.0);
        Convert convert1 = new Convert(testDict1);
        String[] array1 = {"10", "EUR", "+", "4", "USD", "-", "240", "RUB", "in", "RUB"};
        InputProcessing test1 = new InputProcessing(convert1);

        assertEquals(410.0, test1.process(array1,array1.length));

        HashMap<String,Double> testDict2 = new HashMap<>();
        testDict2.put("RUB", 1.0);
        testDict2.put("EUR", 30.0);
        testDict2.put("USD", 25.0);
        Convert convert2 = new Convert(testDict2);
        String[] array2 = {"10", "EUR", "in", "RUB"};
        InputProcessing test2 = new InputProcessing(convert2);

        assertEquals( 300.0, test2.process(array2,array2.length));

        HashMap<String,Double> testDict3 = new HashMap<>();
        testDict3.put("RUB", 1.0);
        testDict3.put("EUR", 100.0);
        testDict3.put("USD", 95.0);
        Convert convert3 = new Convert(testDict3);
        String[] array3 = {"10", "EUR", "-", "10", "EUR", "in", "USD"};
        InputProcessing test3 = new InputProcessing(convert3);

        assertEquals(0.0, test3.process(array3,array3.length));
    }
}
