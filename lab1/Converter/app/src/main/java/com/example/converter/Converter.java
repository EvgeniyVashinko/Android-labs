package com.example.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Converter {
    public Map<String, Double> time = new HashMap<String, Double>();
    public Map<String, Double> distance = new HashMap<String, Double>();
    public Map<String, Double> weight = new HashMap<String, Double>();

    public Converter() {
        time.put("milliseconds", 1000.);
        time.put("seconds", 1.);
        time.put("hours", 1./3600);
        time.put("days", 1./86400);
        time.put("years", 1./31536000);

        distance.put("micrometer", 1000000.);
        distance.put("millimeter", 1000.);
        distance.put("centimeter", 100.);
        distance.put("decimeter", 10.);
        distance.put("meter", 1.);
        distance.put("kilometer", 1./1000);

        weight.put("tons",1./1000);
        weight.put("centners",1./100);
        weight.put("kilograms",1.);
        weight.put("grams",1000.);
        weight.put("milligrams",1000000.);
    }

    public double getCoeffs(String catName, String name){
        double res = 1.;
        switch (catName){
            case "time":
                res = time.get(name);
                break;
            case "distance":
                res = distance.get(name);
                break;
            case "weight":
                res = weight.get(name);
                break;
        }
        return res;
    }

    public ArrayList<String> getCategoriesNames(){
        ArrayList<String> al = new ArrayList<String>();
        al.add("time");
        al.add("distance");
        al.add("weight");
        return al;
    }

    public ArrayList<String> getTimeNames(){
        return new ArrayList<String>(time.keySet());
    }

    public ArrayList<String> getDistanceNames(){
        return new ArrayList<String>(distance.keySet());
    }

    public ArrayList<String> getWeightNames(){
        return new ArrayList<String>(weight.keySet());
    }

    public String Convert(String data, double firstCoeff, double secCoeff) {
        {
            double num;
            if (data.equals("")){ return "0"; }
            try {
                num = Double.parseDouble(data);
            }
            catch(Exception exp){
                return "Ошибка";
            }
            num = num * secCoeff / firstCoeff;
            return ""+num;
        }
    }
}
