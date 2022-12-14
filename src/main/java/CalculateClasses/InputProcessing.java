package CalculateClasses;

import java.util.HashMap;
import java.util.Objects;

public class InputProcessing {
    private final Convert convert;
    private String finalCurrency;

    public InputProcessing(Convert convert) {
        this.convert = convert;
    }

    public String getFinalCurrency()
    {
        return finalCurrency;
    }
    private Double actWithSelectSign (Double a, Double b, String sign) {
        if (Objects.equals(sign, "+")){
            return (a + b);
        }
        else  {
            return (a - b);
        }
    }
    public double process(String[] rawArray, int rawArraySize) throws Exception {

        HashMap<String,Double> finalDic = new HashMap<>(); //создание словаря
        finalCurrency = rawArray[rawArraySize - 1]; //Итоговая валюта
        for (int i = 1; i < rawArraySize - 1; i+=3){ // идем только по валютам
            String key = rawArray[i]; //валюта
            Double value = Double.parseDouble((rawArray[i-1])); // значение
            String sign = "+"; //знак по умолчанию +
            if (i != 1){ // узнаем знак для каждого значения
                sign = rawArray[i - 2];
            }
            if (finalDic.containsKey(rawArray[i])){ //если валюта уже есть в словаре
                finalDic.put(key, actWithSelectSign(finalDic.get(key), value, sign));
            }
            else {
                finalDic.put(key, actWithSelectSign(0.0, value, sign));
            }
        }
        double counter = 0.0; //переменная для подсчета итоговой суммы
        for (String key : finalDic.keySet()){
            counter += convert.convertation(key, finalCurrency, finalDic.get(key));
        }
        return counter;
    }
}
