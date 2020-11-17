package com.quince.salatnotifier.model;

public class SurahModel {
    private int number, noOfAyahs;
    private String enName, arName, revelationType;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNoOfAyahs() {
        return noOfAyahs;
    }

    public void setNoOfAyahs(int noOfAyahs) {
        this.noOfAyahs = noOfAyahs;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getRevelationType() {
        return revelationType;
    }

    public void setRevelationType(String revelationType) {
        this.revelationType = revelationType;
    }
}
