package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import java.util.ArrayList;

public class MeanSquaredError implements Loss{
    public Double Loss(ArrayList<Double> x, ArrayList<Double> y){
        Double sum = 0.0;
        for(int i = 0;i<x.size();i++){
            sum += Math.pow(x.get(i)-y.get(i),2);
        }
        return sum/x.size();
    }
    public ArrayList<Double> LossPrime(ArrayList<Double> x,ArrayList<Double> y){
        ArrayList<Double> result = new ArrayList<>();
        for(int i = 0;i<x.size();i++){
            result.add(2*(x.get(i)-y.get(i)));
        }
        return result;
    }
}
