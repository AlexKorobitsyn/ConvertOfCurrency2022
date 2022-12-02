package Calculate_Classes;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Interface {
    public String input(String expression, HashMap<String, Double> currencies_values) throws Exception {
        Scanner inStr = new Scanner(expression);
        String resOfRegex = "";//с помощью регулярки отсеивает ошибочные строки
        resOfRegex = inStr.findInLine("(((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)([+|-]))+(\\s+)\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))|((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))");

        if (!Objects.equals(resOfRegex, expression)) { //Если строка неправильного формата - ошибка
            return "Wrong input, check /help";
        }
        else
        {
            InputSystem process = new InputSystem();
            return process.inputProcessor(resOfRegex, currencies_values);
        }
    }

    public String output(double resultDouble, String finalCurrency)
    {
        String result = ""; //строка для формирования результата
        result += String.format("%.3f", resultDouble) + " " + finalCurrency;
        return result;
    }
}
