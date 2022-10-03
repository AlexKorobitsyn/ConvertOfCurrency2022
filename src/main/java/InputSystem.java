import java.util.Scanner;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class InputSystem {
    //10 USD + 5 EUR + 100 USD in USD
    public static void mainInput() throws Exception {
        Scanner inStr = new Scanner(System.in);
        String[] arrOfWord ;
        String resOfRegex = "";
        HashMap<String,Double> parseValut = new HashMap<>();
        String[] alphabet ={"USD","EUR","RUB"};
        parseValut =Parser.takeADictionaryOfCurrency(alphabet);
        String cancelStr;
        while(true) {
            // обработаем пробел и как?
            resOfRegex = inStr.findInLine("(((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)([+|-]))+(\\s+)\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))|((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))");
            cancelStr = inStr.nextLine();
            if(cancelStr.compareTo("\\exit")==0){
                System.out.println("Exit.");
                break;
            }
            if(cancelStr.compareTo("\\help")==0){
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
            if(resOfRegex == null) {
                System.out.println("Thats Error!!!");
                break;
            }
            else {
                resOfRegex = resOfRegex.replaceAll("\s+", " ");
                arrOfWord = resOfRegex.split(" ");
                Convert mainConvert = new Convert(parseValut);
                InputProcessing worker = new InputProcessing(mainConvert);
                worker.process(arrOfWord, arrOfWord.length);
            }
        }
    }
}
