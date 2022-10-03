import java.util.HashMap;

public class Convert {

    public static HashMap<String,Double> dictOfCurrencyAndRate;

    public Convert(HashMap<String,Double> dictOfCurrencyAndRate){
        Convert.dictOfCurrencyAndRate = dictOfCurrencyAndRate;
    }

    public static Double convertation(String inCurrency, String outCurrency, Double numberOfInCurrency){
        return (dictOfCurrencyAndRate.get(inCurrency) / dictOfCurrencyAndRate.get(outCurrency)) * numberOfInCurrency;
    }
}
