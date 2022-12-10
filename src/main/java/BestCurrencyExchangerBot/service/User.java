package BestCurrencyExchangerBot.service;

import java.util.HashMap;

public class User {
    private String town = "";
    private TownStructure townStructure;
    private HashMap<String, Double> bankCurrenciesForCalculate = new HashMap<>();

    private String mainBank = "";
    private String step = "Start";
    private String firstCurrency = "";
    private Integer numberOfFirstCurrency = 0;
    private String secondCurrency = "";

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

    public String getSecondCurrency() {
        return secondCurrency;
    }

    public void setSecondCurrency(String secondCurrency) {
        this.secondCurrency = secondCurrency;
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

    public HashMap<String, Double> getBankCurrenciesForCalculate() {
        return bankCurrenciesForCalculate;
    }

    public void setBankCurrenciesForCalculate(HashMap<String, Double> bankCurrenciesForCalculate) {
        this.bankCurrenciesForCalculate = bankCurrenciesForCalculate;
    }
}
