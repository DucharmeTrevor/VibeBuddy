package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers;

import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;

import java.util.ArrayList;
//Super broken gotta fix
/*
TODO:
	- Fix Output not being the correct size
	- Fix Backward
 */
public class RecurrentLayer implements Layer {


	Layer layer;
	ArrayList<Double> LastOutput;

	Integer Inputs;
	Integer Outputs;

	public Integer GetInputs(){
		return Inputs;
	}
	public Integer GetOutputs(){
		return Outputs;
	}
	public RecurrentLayer(Integer Inputs, Integer Outputs, Class<? extends Activation> activation){
		layer = new FullyConnectedLayer(Inputs+Outputs,Outputs,activation);
		this.Inputs=Inputs;
		this.Outputs=Outputs;
		LastOutput=new ArrayList<>();
		for (int i = 0; i < Outputs; i++) {
			LastOutput.add(0.0);
		}
	}

	public RecurrentLayer(){
	}
	@Override
	public void Resize(Integer Inputs,Integer Outputs){
		Inputs=Inputs;
		Outputs=Outputs;
		layer.Resize(Inputs+Outputs,Outputs);
	}
	@Override
	public void SetActivation(Class<? extends Activation> activation){
		layer.SetActivation(activation);
	}
	@Override
	public ArrayList<Double> Forward(ArrayList<Double> x){
		x.addAll(LastOutput);
		LastOutput=layer.Forward(x);
		return LastOutput;
	}
	@Override
	public ArrayList<Double> Backward(ArrayList<Double> x, ArrayList<Double> fg, Double lr){
		x.addAll(LastOutput);
		ArrayList<Double> d = layer.Backward(x,fg,lr);
		LastOutput= new ArrayList<>(d.subList(Inputs,Inputs+Outputs));
		return new ArrayList<>(d.subList(0,Inputs));
	}
	@Override
	public ArrayList<ArrayList<Double>> GetWeights() {
		return layer.GetWeights();
	}

	@Override
	public void SetWeights(ArrayList<ArrayList<Double>> weights) {
		layer.SetWeights(weights);
	}
}