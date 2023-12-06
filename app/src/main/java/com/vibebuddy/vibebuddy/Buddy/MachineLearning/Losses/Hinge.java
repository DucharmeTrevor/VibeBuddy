package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import java.util.ArrayList;

public class Hinge implements Loss {
	public Double Loss(ArrayList<Double> x, ArrayList<Double> y){
		Double sum = 0.0;
		for(int i=0;i<x.size();i++){
			sum += Math.max(0,1-x.get(i)*y.get(i));
		}
		return sum;
	}
	public ArrayList<Double> LossPrime(ArrayList<Double> x,ArrayList<Double> y){
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i=0;i<x.size();i++){
			if(x.get(i)*y.get(i)<1){
				result.add(-y.get(i));
			}
			else{
				result.add(0.0);
			}
		}
		return result;
	}
}
