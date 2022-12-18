package Parsers;

import java.util.HashSet;
import java.util.Set;

public class YandexParserForTest {

    public Set<String> ParseTheYandexName(boolean key) {
        Set<String> result = new HashSet<>();
        if (!key){
            result.add("СберБанк");
            result.add("ВТБ");
        }
        else{
            result.add("60.4345");
            result.add("59.5930");
        }
        return result;
        }

    public String takeTheTown() {
        return "TestTown";
    }
}
