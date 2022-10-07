import java.util.HashMap;
import java.util.Objects;

public class InputProcessing {
    private Convert convert;

    public InputProcessing(Convert convert) {
        this.convert = convert;
    }

    private Double actWithSelectSign (Double a, Double b, String sign) {
        if (Objects.equals(sign, "+")){
            return (a + b);
        }
        else  {
            return (a - b);
        }
    }
    public void process(String[] rawArray, int rawArraySize) throws Exception {

        HashMap<String,Double> finalDic = new HashMap<>(); //создание словаря
        String finalCurrency = rawArray[rawArraySize - 1]; //Итоговая валюта
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
        Interface out = new Interface();
        out.output(counter, finalCurrency);
    }
}
