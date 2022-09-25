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
    public static void MainInput(){
        Scanner inStr = new Scanner(System.in);
        String[] arrOfWord ;
        String audi = "";
        String cancelStr;
        while(true) {
            // обработаем пробел и как?
            audi = inStr.findInLine("((\\s+)?\\d+(\\s+)(USD|EUR|RUB)(\\s+)([+|-]))+(\\s+)\\d+(\\s+)(USD|EUR|RUB)(\\s+)in(\\s+)(USD|EUR|RUB)");
            cancelStr = inStr.nextLine();
            if(cancelStr.compareTo("\\exit")==0){
                System.out.println("Exit.");
                break;
            }
            if(cancelStr.compareTo("\\help")==0){
                System.out.println("There will be \"help\"!");
                continue;
            }
            if(audi == null) {
                System.out.println("Thats Error!!!");
                break;
            }
            else {
                audi = audi.replaceAll("\s+", " ");
                arrOfWord = audi.split(" ");
                for (int i = 0; i < arrOfWord.length; i++) {
                      System.out.println(arrOfWord[i]);
                  }
            }

        }
    }
    public static void main(String[] args) throws Exception {
            //String encoding = System.getProperty("console.encoding", "utf-8");??
        String[] alphabet ={"USD","EUR","RUB"};
        HashMap<String,Double> aezak = new HashMap<>();
        aezak =TakeADictionaryOfCurrency(alphabet);
        System.out.println(aezak.keySet());
        System.out.println(aezak.values());
        MainInput();
    }
}
