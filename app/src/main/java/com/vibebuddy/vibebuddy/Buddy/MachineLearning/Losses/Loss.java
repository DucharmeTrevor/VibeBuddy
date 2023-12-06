package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import java.util.ArrayList;

public interface Loss {
    public Double Loss(ArrayList<Double> x,ArrayList<Double> y);
    public ArrayList<Double> LossPrime(ArrayList<Double> x,ArrayList<Double> y);
}
