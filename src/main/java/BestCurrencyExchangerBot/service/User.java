package BestCurrencyExchangerBot.service;

import Parsers.YandexParser;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.IOException;
import java.util.HashMap;

public class User {
    private String firstName;
    private long chatId;
    private Location coordinatesOfUser = new Location();
    private double[] coordinatesOfBank = new double[2];
    private boolean haveCoordinatesOfBank = false;
    private boolean haveCoordinates = false;
    private String town = "";
    private TownStructure townStructure;
    private HashMap<String, Double> bankCurrenciesForCalculate = new HashMap<>();
    private YandexParser yandexParser = new YandexParser();
    private String mainBank = "";
    private String step = "Start";
    private String firstCurrency = "";
    private Integer numberOfFirstCurrency = 0;
    private String secondCurrency = "";

    public User() throws IOException {
    }

    public String getTown()
    {
        return town;
    }

    public void setTown(String town)
    {
        this.town = town;
    }

    public String getMainBank()
    {
        return mainBank;
    }

    public void setMainBank(String mainBank)
    {
        this.mainBank = mainBank;
    }

    public String getStep()
    {
        return step;
    }

    public void setStep(String step)
    {
        this.step = step;
    }

    public String getFirstCurrency()
    {
        return firstCurrency;
    }

    public void setFirstCurrency(String firstCurrency)
    {
        this.firstCurrency = firstCurrency;
    }

    public String getSecondCurrency()
    {
        return secondCurrency;
    }

    public void setSecondCurrency(String secondCurrency)
    {
        this.secondCurrency = secondCurrency;
    }

    public Integer getNumberOfFirstCurrency()
    {
        return numberOfFirstCurrency;
    }

    public void setNumberOfFirstCurrency(Integer numberOfFirstCurrency)
    {
        this.numberOfFirstCurrency = numberOfFirstCurrency;
    }

    public TownStructure getTownStructure()
    {
        return townStructure;
    }

    public void setTownStructure(TownStructure townStructure)
    {
        this.townStructure = townStructure;
    }

    public HashMap<String, Double> getBankCurrenciesForCalculate()
    {
        return bankCurrenciesForCalculate;
    }

    public void setBankCurrenciesForCalculate(HashMap<String, Double> bankCurrenciesForCalculate)
    {
        this.bankCurrenciesForCalculate = bankCurrenciesForCalculate;
    }

    public Location getCoordinatesOfUser()
    {
        return coordinatesOfUser;
    }

    public void setCoordinatesOfUser(Location coordinatesOfUser) throws IOException
    {
        this.coordinatesOfUser = coordinatesOfUser;
        String coordinates = coordinatesOfUser.getLongitude()+","+coordinatesOfUser.getLatitude();
        yandexParser.setGeocode(coordinates);
        this.setTown(yandexParser.takeTheTown());
        this.setHaveCoordinates(true);
    }

    public boolean isHaveCoordinates()
    {
        return haveCoordinates;
    }

    public void setHaveCoordinates(boolean haveCoordinates)
    {
        this.haveCoordinates = haveCoordinates;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public double[] getCoordinatesOfBank() {
        return coordinatesOfBank;
    }

    public void setCoordinatesOfBank(double[] coordinatesOfBank) {
        this.coordinatesOfBank = coordinatesOfBank;
    }

    public boolean isHaveCoordinatesOfBank() {
        return haveCoordinatesOfBank;
    }

    public void setHaveCoordinatesOfBank(boolean haveCoordinatesOfBank) {
        this.haveCoordinatesOfBank = haveCoordinatesOfBank;
    }
}
