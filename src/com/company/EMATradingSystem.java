package com.company;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.style.lines.SeriesLines;




public class EMATradingSystem {

    public int window_fast, window_slow;
    public double[] ema_window_fast;
    public double[] ema_window_slow;
    public int[] buysignals;
    public int[] sellsignals;
    public double[] results;
    public PriceList priceList;

    public EMATradingSystem(PriceList priceList, int window_fast, int window_slow){
        this.priceList = priceList;
        this.window_fast = window_fast;
        this.window_slow = window_slow;
        trade();

    }
    public void trade(){
        this.ema_window_fast = calculateEMA(window_fast);
        this.ema_window_slow = calculateEMA(window_slow);
        this.buysignals = calculateBuySignals();
        this.sellsignals = calculateSellSignals();
        this.results = calculateResults();
    }

    public int getWindowFast(){
        return window_fast;
    }

    public int getWindowSlow(){
        return window_slow;
    }

    public double[] calculateEMA(int window){
        double[] ema = new double[priceList.priselist1.size()];
        ema[0] = priceList.priselist1.get(0).open;

        for (int i = 1; i<priceList.priselist1.size(); i++){
            ema[i] = ema[i-1] + (2*(priceList.priselist1.get(i).open - ema[i-1])/(window+1));
        }

        return ema;
    }


    public int[] calculateBuySignals(){
        int k=0, j=0;

        for (int i = 1; i<priceList.priselist1.size(); i++){
            if(ema_window_fast[i-1] < ema_window_slow[i-1] && ema_window_fast[i] > ema_window_slow[i]){
                k++;
            }
        }

        int[] buysignals1 = new int [k];
        for (int i = 1; i<priceList.priselist1.size(); i++){
            if(ema_window_fast[i-1] < ema_window_slow[i-1] && ema_window_fast[i] > ema_window_slow[i]){
                buysignals1[j] = i;
                j++;
            }
        }

        return buysignals1;
    }

/*
    public int[] calculateSellSignals(){
        int sum=0, y=0, jj=0;
        for (int i = 1; i<priceList.priselist1.size()-1; i++){
            if(i>buysignals[y] && priceList.priselist1.get(i-1).close < ema_window_slow[i-1]){
                sum++;
                y++;
            }
            if (y==buysignals.length){
                break;
            }
        }

        y=0;

        int[] sellsignals1 = new int [sum];
        for (int i = 1; i<priceList.priselist1.size()-1; i++){
            if(i>buysignals[y] && priceList.priselist1.get(i-1).close < ema_window_slow[i-1]){
                if(y!=0 && buysignals[y]==buysignals[y-1]){
                    sellsignals1[jj] = i-1;
                }
                else{
                    sellsignals1[jj] = i;
                }
                y++;
                jj++;
            }
            if (y==buysignals.length){
                break;
            }
        }
        return sellsignals1;
    }
*/


    public int[] calculateSellSignals(){
        int sumsellsig=0, buysigindex=0,buysigindex1=0, sellsigindex=0;
        for (int i = 1; i<priceList.priselist1.size(); i++){
            if(i>buysignals[buysigindex] && priceList.priselist1.get(i-1).close < ema_window_slow[i-1]){
                sumsellsig++;
                buysigindex++;
            }
            if (buysigindex==buysignals.length){
                break;
            }
        }
        buysigindex=0;

        int[] sellsignals1 = new int [sumsellsig];
        int tosellsigcount = 0, manysellsigindex, start=buysignals[buysigindex];

        for (int i = start; i<priceList.priselist1.size(); i++){
            if(buysigindex!=buysignals.length){
            if(i==buysignals[buysigindex]){
                buysigindex++;
            }}
            if(i>buysignals[buysigindex1] && priceList.priselist1.get(i-1).close < ema_window_slow[i-1]){
                sellsignals1[sellsigindex] = i;
                sellsigindex++;
                buysigindex1++;
                if(buysigindex>sellsigindex && sellsignals1[sellsigindex-1]!=buysignals[buysigindex1]){
                    tosellsigcount = buysigindex-sellsigindex;
                    manysellsigindex = i;
                    for(int k =0; k<tosellsigcount; k++){
                        sellsignals1[sellsigindex] = manysellsigindex;
                        sellsigindex++;
                        buysigindex1++;
                    }
                }
            }

            if (/*buysigindex==buysignals.length && */ sellsigindex ==sellsignals1.length){
                break;
            }

        }
        return sellsignals1;
    }


    public double[] calculateResults(){
        double[] results1 = new double[sellsignals.length];

        for (int i = 0; i<sellsignals.length; i++){
            results1[i] = priceList.priselist1.get(sellsignals[i]).open - priceList.priselist1.get(buysignals[i]).open;
        }

        return results1;
    }


    public double[] getOpenPrices(){
        double[] openPrices = new double[priceList.priselist1.size()];

        for(int i = 0; i<priceList.priselist1.size(); i++){
            openPrices[i]=priceList.priselist1.get(i).open;
        }
        return openPrices;
    }

    public void plottingEMAT(){
        XYChart chart = new XYChart (1600, 900);
        chart.setTitle("Preisverlauf");
        chart.setXAxisTitle("Tag");

        XYSeries seriesOpenPrice = chart.addSeries("Preis", getOpenPrices());
        seriesOpenPrice.setMarker(SeriesMarkers.NONE);
        XYSeries seriesEMAFast = chart.addSeries("EMA (window=" + window_fast + ")", ema_window_fast);
        seriesEMAFast.setMarker(SeriesMarkers.NONE);
        XYSeries seriesEMASlow = chart.addSeries("EMA (window=" + window_slow + ")", ema_window_slow);
        seriesEMASlow.setMarker(SeriesMarkers.NONE);

        XYSeries seriesBuySignals = chart.addSeries("buy", buysignals, new int[buysignals.length]);
        seriesBuySignals.setLineStyle(SeriesLines.NONE);
        XYSeries seriesSellSignals = chart.addSeries("sell", sellsignals, new int[sellsignals.length]);
        seriesSellSignals.setLineStyle(SeriesLines.NONE);

        try{
            BitmapEncoder.saveBitmap(chart, "trading_AMZN.PNG", BitmapFormat.PNG);
        }
        catch (Exception e){
            System.out.println("Fehler beim Speichern der Bilddatei");
        }
    }
}


