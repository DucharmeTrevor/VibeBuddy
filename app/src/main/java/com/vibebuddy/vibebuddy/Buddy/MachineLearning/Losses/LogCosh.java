package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import java.util.ArrayList;

public class LogCosh implements Loss {
	public Double Loss(ArrayList<Double> x, ArrayList<Double> y){
		Double sum = 0.0;
		for(int i=0;i<x.size();i++){
			sum += Math.log(Math.cosh(x.get(i) - y.get(i)));
		}
		return sum;
	}
	public ArrayList<Double> LossPrime(ArrayList<Double> x,ArrayList<Double> y){
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i=0;i<x.size();i++){
			result.add(Math.tanh(x.get(i) - y.get(i)));
		}
		return result;
	}
}
