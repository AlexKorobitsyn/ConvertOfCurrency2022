import java.util.HashMap;

public class InputProcessing {

    private static String[] rawArray;
    private static int rawArraySize;
    private static HashMap<String, Double> parseValut;

    public InputProcessing(String[] rawArray, int rawArraySize, HashMap<String,Double> parseValut) {
        this.rawArray = rawArray;
        this.rawArraySize = rawArraySize;
        this.parseValut = parseValut;
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
    public static String process() throws Exception {



        HashMap<String,Double> finalDic = new HashMap<>(); //создание словаря
        String finalValut = rawArray[rawArraySize - 1]; //Итоговая валюта
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
            counter += finalDic.get(key) * divideValut(key, finalValut, parseValut);
        }
        result += String.format("%.3f", counter) + " " + finalValut;
        System.out.println(result);
        return result;
    }
}
