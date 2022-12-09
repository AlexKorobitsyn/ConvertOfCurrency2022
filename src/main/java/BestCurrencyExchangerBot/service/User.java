package BestCurrencyExchangerBot.service;

import java.util.HashMap;

public class User {
    private String town = "";
    private TownStructure townStructure;
    private HashMap<String, Double> bank_currencies_for_calculate = new HashMap<>();

    private String mainBank = "";
    private String step = "Start";
    private String firstCurrency = "";
    private Integer numberOfFirstCurrency = 0;
    private String secondСurrency = "";

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getMainBank() {
        return mainBank;
    }

    public void setMainBank(String mainBank) {
        this.mainBank = mainBank;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getFirstCurrency() {
        return firstCurrency;
    }

    public void setFirstCurrency(String firstCurrency) {
        this.firstCurrency = firstCurrency;
    }

    public String getSecondСurrency() {
        return secondСurrency;
    }

    public void setSecondСurrency(String secondСurrency) {
        this.secondСurrency = secondСurrency;
    }

    public Integer getNumberOfFirstCurrency() {
        return numberOfFirstCurrency;
    }

    public void setNumberOfFirstCurrency(Integer numberOfFirstCurrency) {
        this.numberOfFirstCurrency = numberOfFirstCurrency;
    }

    public TownStructure getTownStructure() {
        return townStructure;
    }

    public void setTownStructure(TownStructure townStructure) {
        this.townStructure = townStructure;
    }

    public HashMap<String, Double> getBank_currencies_for_calculate() {
        return bank_currencies_for_calculate;
    }

    public void setBank_currencies_for_calculate(HashMap<String, Double> bank_currencies_for_calculate) {
        this.bank_currencies_for_calculate = bank_currencies_for_calculate;
    }
}
