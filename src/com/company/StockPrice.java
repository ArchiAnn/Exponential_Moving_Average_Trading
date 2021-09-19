package com.company;

public class StockPrice {

    public double open, high, low, close;

    public StockPrice(double open, double high, double low, double close){
        this.open  =  open;
        this.high  =  high;
        this.low   =  low ;
        this.close =  close;
    }

    public double getOpen(){
        return open;
    }

    public double getHigh(){
        return high;
    }

    public double getLow(){
        return low;
    }

    public double getClose(){
        return close;
    }

    public static StockPrice parseCSVLine(String line){

        double o, h, l, c;

        String[] values = line.split(",");

        o= Double.parseDouble(values[1]);
        h= Double.parseDouble(values[2]);
        l= Double.parseDouble(values[3]);
        c= Double.parseDouble(values[4]);

        StockPrice newsp1 = new StockPrice(o, h, l, c);


        return newsp1;
    }

}
