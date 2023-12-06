package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations;

public class LeakyReLU implements Activation
{

    @Override
    public double Activation(double x) {
        if (x > 0) {
            return x;
        } else {
            return 0.01 * x;
        }
    }

    @Override
    public double ActivationPrime(double x) {
        if (x > 0) {
            return 1;
        } else {
            return 0.01;
        }
    }
}
