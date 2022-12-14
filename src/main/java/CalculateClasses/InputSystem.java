package CalculateClasses;

import java.util.HashMap;

public class InputSystem {
    private String[] makeArrFromString(String str)
    {
        String[] arrOfWord;
        str = str.replaceAll("\s+", " ");//можно ставить хоть сколько пробелов
        arrOfWord = str.split(" ");//разбить на массив
        return arrOfWord;
    }
    public String inputProcessor(String str, HashMap<String,Double> currenciesValues) throws Exception {//метод занимается входными данными
        String[] arrOfWord = makeArrFromString(str);
        Convert mainConvert = new Convert(currenciesValues);//создание класса для подсчёта
        InputProcessing worker = new InputProcessing(mainConvert);
        double tempValue = worker.process(arrOfWord, arrOfWord.length);//основной подсчёт
        Interface out = new Interface();
        return out.output(tempValue, worker.getFinalCurrency());
    }
}
