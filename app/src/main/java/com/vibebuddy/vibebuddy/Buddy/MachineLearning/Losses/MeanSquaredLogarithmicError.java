package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import java.util.ArrayList;

public class MeanSquaredLogarithmicError implements Loss {
	public Double Loss(ArrayList<Double> x, ArrayList<Double> y){
		Double sum = 0.0;
		for(int i=0;i<x.size();i++){
			sum += Math.pow(Math.log(x.get(i)+1)-Math.log(y.get(i)+1),2);
		}
		return sum;
	}
	public ArrayList<Double> LossPrime(ArrayList<Double> x,ArrayList<Double> y){
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i=0;i<x.size();i++){
			result.add(2*(Math.log(x.get(i)+1)-Math.log(y.get(i)+1))/(x.get(i)+1));
		}
		return result;
	}
}
