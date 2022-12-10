import CalculateClasses.Convert;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ConvertTest {
    @org.junit.jupiter.api.Test
    void testConvertationMethod(){
        HashMap<String, Double> dict = new HashMap<>();
        dict.put("USD", 50.0);
        dict.put("EUR", 70.0);
        dict.put("RUB", 1.0);
        Convert convert = new Convert(dict);
        assertEquals(139930.0, convert.convertation("EUR","RUB", 1999.0));
        assertEquals(2000.0, convert.convertation("RUB","RUB", 2000.0));
        assertEquals(22500, convert.convertation("USD","RUB", 450.0));
    }
}
