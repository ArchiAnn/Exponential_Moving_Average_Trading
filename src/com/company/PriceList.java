package com.company;

import java.util.ArrayList;
import java.io.BufferedReader ;
import java.io.File;
import java.io.FileReader;


public class PriceList{

    public ArrayList<StockPrice> priselist1 = new ArrayList<StockPrice>();

    public PriceList(String fileName){

        File infile = new File(fileName);
        FileReader filereader = null;

        try {
            filereader = new FileReader(infile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        BufferedReader reader = new BufferedReader(filereader);
        try {
            while (reader.ready()){
                String line1 = reader.readLine();
                try {
                    StockPrice newsp = StockPrice.parseCSVLine(line1);
                    priselist1.add(newsp);
                }catch (Exception e) {
                    continue;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }


    public int getNumPrices(){
        return priselist1.size();
    }

    public StockPrice getPriceAt(int pos){
        return priselist1.get(pos);
    }

}
