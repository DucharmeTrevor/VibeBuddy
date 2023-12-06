package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations;

public class Tanh implements Activation
{

	@Override
	public double Activation(double x) {
		return Math.tanh(x);
	}

	@Override
	public double ActivationPrime(double x) {
		return 1 - Math.pow(Math.tanh(x),2);
	}
}
