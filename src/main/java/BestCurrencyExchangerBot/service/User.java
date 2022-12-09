package BestCurrencyExchangerBot.service;

public class User {
    private String town = "";
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
}
