package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import java.util.ArrayList;

public class MeanAbsolutePercentageError implements Loss {
	public Double Loss(ArrayList<Double> x, ArrayList<Double> y){
		double sum = 0;
		for(int i = 0; i < x.size(); i++){
			sum += Math.abs((x.get(i) - y.get(i)) / y.get(i));
		}
		return sum / x.size();
	}
	public ArrayList<Double> LossPrime(ArrayList<Double> x,ArrayList<Double> y){
		ArrayList<Double> ret = new ArrayList<Double>();
		for(int i = 0; i < x.size(); i++){
			ret.add((x.get(i) - y.get(i)) / y.get(i));
		}
		return ret;
	}
}
