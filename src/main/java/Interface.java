import java.util.Scanner;

public class Interface {
    public void input() throws Exception {
        Scanner inStr = new Scanner(System.in);
        String resOfRegex = "";//с помощью регулярки отсеивает ошибочные строки
        String cancelStr;
        InputSystem process = new InputSystem();
        while (true) {
            resOfRegex = inStr.findInLine("(((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)([+|-]))+(\\s+)\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))|((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))");
            cancelStr = inStr.nextLine();
            if (cancelStr.compareTo("\\exit") == 0) { //если написали выход
                System.out.println("Exit.");
                break;
            }
            if (cancelStr.compareTo("\\help") == 0) {//help
                System.out.println("help:\n" +
                        "Description:\n" +
                        "\tCurrency conversion. The simplest calculations in different currencies.\n" +
                        "Options:\n" +
                        "\tQuantityOfCurrency = QOC\n" +
                        "\tNameOfCurrency = NOC\n" +
                        "\tAnswer - Double variable\n" +
                        "\n" +
                        "\tQOC <= 2147483647\n" +
                        "\t-1.7 * 10^308 <= Answer <= 1.7 * 10^308\t\n" +
                        "\tNOC: USD, RUB, EUR\n" +
                        "\t\n" +
                        "Examples:\n" +
                        "\tIN: QOC NOC + QOC NOC + ... + QOC NOC IN NOC\n" +
                        "\tOUT: Answer\n" +
                        "\t\n" +
                        "\tIN: 10 USD + 5 EUR in RUB\n" +
                        "\tOUT: 820.62 RUB");

                continue;
            }
            if (resOfRegex == null) { //Если строка неправильного формата - ошибка
                System.out.println("Thats Error!!!");
                break;
            }
            else
            {
                process.inputProcessor(resOfRegex);
            }
        }
    }
    public void output(double resultDouble, String finalCurrency)
    {
        String result = ""; //строка для формирования результата
        result += String.format("%.3f", resultDouble) + " " + finalCurrency;
        System.out.println(result);
    }
}