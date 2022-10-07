import java.util.HashMap;

public class InputSystem {
    //10 USD + 5 EUR + 100 USD in USD
    public void inputProcessor(String str) throws Exception {//метод занимается входными данными
        String[] arrOfWord;
        HashMap<String,Double> parseValut = new HashMap<>();
        String[] alphabet ={"USD","EUR","RUB"};
        Parser mainParser = new Parser();
        parseValut =mainParser.takeADictionaryOfCurrency(alphabet);//создаёт словарь с валютой и её значением в рублях
        str = str.replaceAll("\s+", " ");//можно ставить хоть сколько пробелов
        arrOfWord = str.split(" ");//разбить на массив
        Convert mainConvert = new Convert(parseValut);//создание класса для подсчёта
        InputProcessing worker = new InputProcessing(mainConvert);
        worker.process(arrOfWord, arrOfWord.length);//основной подсчёт
    }
}
