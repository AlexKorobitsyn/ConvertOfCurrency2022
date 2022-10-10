import java.util.HashMap;

public class InputSystem {
    //10 USD + 5 EUR + 100 USD in USD
    private String[] makeArrFromString(String str)
    {
        String[] arrOfWord;
        str = str.replaceAll("\s+", " ");//можно ставить хоть сколько пробелов
        arrOfWord = str.split(" ");//разбить на массив
        return arrOfWord;
    }
    public void inputProcessor(String str) throws Exception {//метод занимается входными данными
        String[] arrOfWord = makeArrFromString(str);
        String[] alphabet = {"USD","EUR","RUB"};
        HashMap<String,Double> parseValut = new HashMap<>();
        Parser mainParser = new Parser();
        parseValut = mainParser.takeADictionaryOfCurrency(alphabet);//создаёт словарь с валютой и её значением в рублях
        Convert mainConvert = new Convert(parseValut);//создание класса для подсчёта
        InputProcessing worker = new InputProcessing(mainConvert);
        double tempValue = worker.process(arrOfWord, arrOfWord.length);//основной подсчёт
        Interface out = new Interface();
        out.output(tempValue, worker.getFinalCurrency());
    }
}
