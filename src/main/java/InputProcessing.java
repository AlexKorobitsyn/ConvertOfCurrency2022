import java.util.HashMap;

public class InputProcessing {

    private static String[] rawArray;
    private static int rawArraySize;

    public InputProcessing(String[] rawArray, int rawArraySize) {
        this.rawArray = rawArray;
        this.rawArraySize = rawArraySize;
    }

    public static Double DivideValut(String InValut, String OutValut, HashMap<String, Double> ParseValut) {
        return ParseValut.get(InValut) / ParseValut.get(OutValut);
    }

    public static String Process() {

        HashMap<String,Double> ParseValut = new HashMap<>();
        ParseValut.put("USD", 55.50);
        ParseValut.put("EUR", 53.124);
        ParseValut.put("RUB", 1.0);

        HashMap<String,Double> FinalDic = new HashMap<>(); //создание словаря
        String FinalValut = rawArray[rawArraySize - 1]; //Итоговая валюта
        for (int i = 1; i < rawArraySize - 1; i+=3){ // идем только по валютам
            String key = rawArray[i]; //валюта
            Double value = Double.parseDouble((rawArray[i-1])); // значение

            if (FinalDic.containsKey(rawArray[i])){ //если валюта уже есть в словаре
                FinalDic.put(key, FinalDic.get(key) + value);
            }
            else {
                FinalDic.put(key, value);
            }
        }
        double counter = 0.0;
        String result = "";
        for (String key : FinalDic.keySet()){
            counter += FinalDic.get(key) * DivideValut(key, FinalValut, ParseValut);
        }
        result += String.format("%.3f", counter) + " " + FinalValut;
        System.out.println(result);
        return result;
    }
}
