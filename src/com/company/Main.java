package com.company;

public class Main {

    public static void main(String[] args) {
        PriceList _pricelist = new PriceList("AMZN.csv");
        EMATradingSystem e = new EMATradingSystem(_pricelist, 8, 88);
        e.calculateResults();
        e.plottingEMAT();

    }
}
