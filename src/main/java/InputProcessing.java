import java.util.HashMap;

public class InputProcessing {
    private static Convert convert;

    public InputProcessing(Convert convert) {
        this.convert = convert;
    }

    public static Double divideValut(String inValut, String outValut, HashMap<String, Double> parseValut) {
        return parseValut.get(inValut) / parseValut.get(outValut);
    }

    public static Double actWithSelectSign (Double a, Double b, String sign) {
        if (sign == "+"){
            return (a + b);
        }
        else  {
            return (a - b);
        }
    }
    public static String process(String[] rawArray, int rawArraySize) throws Exception {

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
        String result = ""; //строка для формирования результата
        for (String key : finalDic.keySet()){
            counter += convert.convertation(key, finalCurrency, finalDic.get(key));
        }
        result += String.format("%.3f", counter) + " " + finalCurrency;
        System.out.println(result);
        return result;
    }
}
