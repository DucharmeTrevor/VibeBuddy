package com.vibebuddy.vibebuddy.Buddy.MachineLearning;

import androidx.annotation.Nullable;

import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.FullyConnectedLayer;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.Layer;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.Loss;

import java.util.ArrayList;

public class NeuralNetwork {

    Class<? extends Activation> activation;
    Loss loss;
    ArrayList<Layer> layers;
    ArrayList<ArrayList<Double>> A;

    public NeuralNetwork() {
    }

    public void Resize(ArrayList<Integer> LayerSizes, @Nullable Class<? extends Activation> activation){
        layers = new ArrayList<>();
        for (int i = 0; i < LayerSizes.size()-1; i++) {
            if(activation==null){
                layers.add(new FullyConnectedLayer(LayerSizes.get(i),LayerSizes.get(i+1),this.activation));
            }else{
                layers.add(new FullyConnectedLayer(LayerSizes.get(i),LayerSizes.get(i+1),activation));
            }
        }
    }
    public NeuralNetwork(ArrayList<Integer> LayerSizes,ArrayList<Class<? extends Layer>> LayerTypes,Class<? extends Activation> activation,Class<? extends Loss> loss) {
        SetLoss(loss);
        Resize(LayerSizes,activation);
    }

    public void SetActivation(Class<? extends Activation> activation){
        this.activation = activation;
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).SetActivation(activation);
        }
    }

    public void SetLoss(Class<? extends Loss> loss){
        try {
            this.loss = loss.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Double> Forward(ArrayList<Double> x){
        if(x.size()!=layers.get(0).GetInputs()){
            throw new RuntimeException("Inputs: "+layers.get(0).GetInputs().toString()+" x.size(): "+x.size());
        }
        A= new ArrayList<>();
        A.add(x);
        for (int i = 0; i < layers.size(); i++) {
            x = layers.get(i).Forward(x);
            A.add(x);
        }
        if(x.size()!=layers.get(layers.size()-1).GetOutputs()){
            throw new RuntimeException("Outputs: "+layers.get(layers.size()-1).GetOutputs().toString()+" x.size(): "+x.size());
        }
        return x;
    }

    public ArrayList<Double> Backward(ArrayList<Double> fg,Double lr){
        ArrayList<Double> da_dx = fg;
        for (int i = layers.size()-1; i >= 0; i--) {
            da_dx = layers.get(i).Backward(A.get(i),da_dx,lr);
        }
        return fg;
    }
    public void Train(ArrayList<Double> Input,ArrayList<Double> Expected,Double lr){
        ArrayList<Double> Output = Forward(Input);
        ArrayList<Double> Error = loss.LossPrime(Output,Expected);
        Backward(Error,lr);
    }

    public Double GetLoss(ArrayList<Double> Input,ArrayList<Double> Expected){
        ArrayList<Double> Output = Forward(Input);
        return loss.Loss(Output,Expected);
    }

    public ArrayList<ArrayList<ArrayList<Double>>> GetWeights(){
        ArrayList<ArrayList<ArrayList<Double>>> Weights = new ArrayList<ArrayList<ArrayList<Double>>>();
        for (int i = 0; i < layers.size(); i++) {
            Weights.add(layers.get(i).GetWeights());
        }
        return Weights;
    }

    public void SetWeights(ArrayList<ArrayList<ArrayList<Double>>> Weights){
        for (int i = 0; i < layers.size(); i++) {
            layers.get(i).SetWeights(Weights.get(i));
        }
    }
}
