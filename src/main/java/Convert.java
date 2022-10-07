import java.util.HashMap;

public class Convert {

    private HashMap<String,Double> dictOfCurrencyAndRate;

    public Convert(HashMap<String,Double> dictOfCurrencyAndRate){
        this.dictOfCurrencyAndRate = dictOfCurrencyAndRate;
    }

    public Double convertation(String inCurrency, String outCurrency, Double numberOfInCurrency){
        return (dictOfCurrencyAndRate.get(inCurrency) / dictOfCurrencyAndRate.get(outCurrency)) * numberOfInCurrency;
    }
}
