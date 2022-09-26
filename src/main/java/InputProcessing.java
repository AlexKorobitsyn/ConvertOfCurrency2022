import java.util.HashMap;

public class InputProcessing {

    private static String[] rawArray;
    private static int rawArraySize;
    private static HashMap<String, Double> ParseValut;

    public InputProcessing(String[] rawArray, int rawArraySize, HashMap<String,Double> ParseValut) {
        this.rawArray = rawArray;
        this.rawArraySize = rawArraySize;
        this.ParseValut = ParseValut;
    }

    public static Double DivideValut(String InValut, String OutValut, HashMap<String, Double> ParseValut) {
        return ParseValut.get(InValut) / ParseValut.get(OutValut);
    }

    public static Double ActWithSelectSign (Double a, Double b, String sign) {
        if (sign == "+"){
            return (a + b);
        }
        else  {
            return (a - b);
        }
    }
    public static String Process() throws Exception {



        HashMap<String,Double> FinalDic = new HashMap<>(); //создание словаря
        String FinalValut = rawArray[rawArraySize - 1]; //Итоговая валюта
        for (int i = 1; i < rawArraySize - 1; i+=3){ // идем только по валютам
            String key = rawArray[i]; //валюта
            Double value = Double.parseDouble((rawArray[i-1])); // значение
            String sign = "+"; //знак по умолчанию +
            if (i != 1){ // узнаем знак для каждого значения
                sign = rawArray[i - 2];
            }
            if (FinalDic.containsKey(rawArray[i])){ //если валюта уже есть в словаре
                FinalDic.put(key, ActWithSelectSign(FinalDic.get(key), value, sign));
            }
            else {
                FinalDic.put(key, ActWithSelectSign(0.0, value, sign));
            }
        }
        double counter = 0.0; //переменная для подсчета итоговой суммы
        String result = ""; //строка для формирования результата
        for (String key : FinalDic.keySet()){
            counter += FinalDic.get(key) * DivideValut(key, FinalValut, ParseValut);
        }
        result += String.format("%.3f", counter) + " " + FinalValut;
        System.out.println(result);
        return result;
    }
}
