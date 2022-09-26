/*

import static org.junit.jupiter.api.Assertions.*;

class InputProcessingTest {

    @org.junit.jupiter.api.Test
    void TestProcessMethod() {
        String[] arr1 = new String[] {"10" , "USD" , "+" , "5" , "EUR", "in", "RUB"};
        InputProcessing test1 = new InputProcessing(arr1,7);
        assertEquals("820,620 RUB", test1.Process());

        String[] arr2 = new String[] {"10" , "USD" , "+" , "5" , "USD", "in", "USD"};
        InputProcessing test2 = new InputProcessing(arr2,7);
        assertEquals("15,000 USD", test2.Process());

        String[] arr3 = new String[] {"0" , "USD" , "+" , "0" , "EUR", "+", "0", "RUB", "in", "RUB"};
        InputProcessing test3 = new InputProcessing(arr3,10);
        assertEquals("0,000 RUB", test3.Process());
    }
}*/
