package com.example.billify;

public class CustomSpinnerItem {
    private String spinnerText;
    private int spinnerImg;

    public CustomSpinnerItem(String spinnerText, int spinnerImg) {
        this.spinnerText = spinnerText;
        this.spinnerImg = spinnerImg;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public int getSpinnerImg() {
        return spinnerImg;
    }
}
