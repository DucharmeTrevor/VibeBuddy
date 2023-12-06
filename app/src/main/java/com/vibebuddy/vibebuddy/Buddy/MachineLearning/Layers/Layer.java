package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers;

import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;

import java.util.ArrayList;

public interface Layer {
	public void Resize(Integer Inputs,Integer Outputs);
	public void SetActivation(Class<? extends Activation> activation);
	public ArrayList<Double> Forward(ArrayList<Double> x);
	public ArrayList<Double> Backward(ArrayList<Double> x, ArrayList<Double> fg, Double lr);
	public ArrayList<ArrayList<Double>> GetWeights();
	public void SetWeights(ArrayList<ArrayList<Double>> weights);

	public Integer GetInputs();
	public Integer GetOutputs();
}
