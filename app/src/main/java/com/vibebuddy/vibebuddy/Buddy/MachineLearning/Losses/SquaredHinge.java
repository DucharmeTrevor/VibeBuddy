package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import java.util.ArrayList;

public class SquaredHinge implements Loss {
	public Double Loss(ArrayList<Double> x, ArrayList<Double> y){
		Double sum = 0.0;
		for(int i=0;i<x.size();i++){
			sum += Math.pow(Math.max(0,1-x.get(i)*y.get(i)),2);
		}
		return sum;
	}
	public ArrayList<Double> LossPrime(ArrayList<Double> x,ArrayList<Double> y){
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i=0;i<x.size();i++){
			if(x.get(i)*y.get(i)<1){
				result.add(-2*Math.pow(Math.max(0,1-x.get(i)*y.get(i)),2)*y.get(i));
			}
			else{
				result.add(0.0);
			}
		}
		return result;
	}
}
