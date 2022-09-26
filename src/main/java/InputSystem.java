import java.util.Scanner;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class InputSystem {
    //10 USD + 5 EUR + 100 USD in USD
    public static HashMap<String, Double> TakeADictionaryOfCurrency(String[] allKey)//создание Dictionary с курсом валют
            throws Exception {
        Parser parserObject1 = new Parser();
        String value = parserObject1.ParseTheTable();
        HashMap<String, Double> dict = new HashMap();
        for(int i =0;i< allKey.length;i++) {
            if(allKey[i]=="RUB")dict.put("RUB", 1.0);
            else {
                dict.put(allKey[i], Double.parseDouble(parserObject1.findInBigStr(value, allKey[i]).replaceAll(",",".")));
            }
        }
        return dict;
    }
    public static void MainInput() throws Exception {
        Scanner inStr = new Scanner(System.in);
        String[] arrOfWord ;
        String audi = "";
        HashMap<String,Double> ParseValut = new HashMap<>();
        String[] alphabet ={"USD","EUR","RUB"};
        ParseValut =InputSystem.TakeADictionaryOfCurrency(alphabet);
        String cancelStr;
        while(true) {
            // обработаем пробел и как?
            audi = inStr.findInLine("(((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)([+|-]))+(\\s+)\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))|((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB))");
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
            if(audi == null) {
                System.out.println("Thats Error!!!");
                break;
            }
            else {
                audi = audi.replaceAll("\s+", " ");
                arrOfWord = audi.split(" ");
                InputProcessing worker = new InputProcessing(arrOfWord, arrOfWord.length, ParseValut);
                worker.Process();
            }

        }
    }
}
