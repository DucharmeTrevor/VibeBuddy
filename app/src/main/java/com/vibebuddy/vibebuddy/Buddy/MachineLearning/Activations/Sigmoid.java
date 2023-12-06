package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations;

public class Sigmoid implements Activation
{

    @Override
    public double Activation(double x) {
        return 1/(1+Math.exp(-x));
    }

    @Override
    public double ActivationPrime(double x) {
        return Activation(x)*(1-Activation(x));
    }

}
