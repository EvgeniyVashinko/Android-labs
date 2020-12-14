package com.example.converter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ConverterViewModel extends ViewModel {
    private MutableLiveData<String> dataIn = new MutableLiveData<String>();
    private MutableLiveData<String> dataOut = new MutableLiveData<String>();
    private Converter converter;

    private MutableLiveData<String> currentCategory = new MutableLiveData<String>();
    private MutableLiveData<String> currentConverterFrom = new MutableLiveData<String>();
    private MutableLiveData<String> currentConverterTo = new MutableLiveData<String>();

    public ConverterViewModel(){
        converter = new Converter();
    }

    public LiveData<String> getCurrentCategory() {
        if (currentCategory == null) {
            currentCategory = new MutableLiveData<String>();
        }
        return currentCategory;
    }

    public void setCurrentCategory(String str) {
        if (currentCategory == null) {
            currentCategory = new MutableLiveData<String>();
        }
        currentCategory.setValue(str);
//        setDataIn("");
    }

    public LiveData<String> getCurrentConverterFrom() {
        if (currentConverterFrom == null) {
            currentConverterFrom = new MutableLiveData<String>();
        }
        return currentConverterFrom;
    }

    public void setCurrentConverterFrom(String str) {
        if (currentConverterFrom == null) {
            currentConverterFrom = new MutableLiveData<String>();
        }
        currentConverterFrom.setValue(str);
        if (getDataIn().getValue() != null){
            CalculateResult(getDataIn().getValue());
        }
    }

    public LiveData<String> getCurrentConverterTo() {
        if (currentConverterTo == null) {
            currentConverterTo = new MutableLiveData<String>();
        }
        return currentConverterTo;
    }

    public void setCurrentConverterTo(String str) {
        if (currentConverterTo == null) {
            currentConverterTo = new MutableLiveData<String>();
        }
        currentConverterTo.setValue(str);
        if (getDataIn().getValue() != null){
            CalculateResult(getDataIn().getValue());
        }
    }

//    public
    public ArrayList<String> getCategoriesNames(){
        return converter.getCategoriesNames();
    }

    public ArrayList<String> getCategoriesByName(String name){
        ArrayList<String> cat = converter.getCategoriesNames();
        if (name.equals(cat.get(0))){
            return converter.getTimeNames();
        }
        else if(name.equals(cat.get(1))){
            return converter.getDistanceNames();
        }
        else if(name.equals(cat.get(2))){
            return converter.getWeightNames();
        }
        else return new ArrayList<String>();
    }

    public LiveData<String> getDataIn() {
        if (dataIn == null) {
            dataIn = new MutableLiveData<String>();
        }
        return dataIn;
    }
    public void setDataIn(String str) {
        if (dataIn == null) {
            dataIn = new MutableLiveData<String>();
        }
        str = InfoProcessing(str);
        dataIn.setValue(str);
        CalculateResult(str);
    }
    public LiveData<String> getDataOut() {
        if (dataOut == null) {
            dataOut = new MutableLiveData<String>();
        }
        return dataOut;
    }
    public void setDataOut(String str) {
        if (dataOut == null) {
            dataOut = new MutableLiveData<String>();
        }
        dataOut.setValue(str);
    }
    private String InfoProcessing(String str){
        if (str.equals("")){
            return "";
        }
        if (str.charAt(str.length()-1) == '⌫'){
            if (str.length() > 1){
                return str.substring(0, str.length()-2);
            }
            return "";
        }
        return str;
    }

    private String Calculate(String str){
        double firstCoeff = 1., secCoeff=1.;
        try {
            firstCoeff = converter.getCoeffs(getCurrentCategory().getValue(), getCurrentConverterFrom().getValue());
            secCoeff = converter.getCoeffs(getCurrentCategory().getValue(), getCurrentConverterTo().getValue());
        }
        catch (Exception exp){}
        return converter.Convert(str,firstCoeff,secCoeff);
    }

    private void CalculateResult(String str){
        setDataOut(Calculate(str));
    }
}
