package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations;

public class Linear implements Activation
{

	@Override
	public double Activation(double x) {
		return x;
	}

	@Override
	public double ActivationPrime(double x) {
		return 1;
	}
}
