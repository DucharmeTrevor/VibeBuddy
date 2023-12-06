package com.vibebuddy.vibebuddy.Buddy;

import android.util.Log;

import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Sigmoid;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.FullyConnectedLayer;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.Layer;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.BinaryCrossentropy;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.Loss;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.MeanAbsoluteError;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.MeanSquaredError;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.SparseCategoricalCrossentropy;
import com.vibebuddy.vibebuddy.Lovense.InputState;
import com.vibebuddy.vibebuddy.Lovense.Toy;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Buddy {
    NeuralNetwork neuralNetwork;
    Class<? extends Activation> activation;
    Class<? extends Loss> loss;
    Integer Inputs;
    Integer Outputs;
    ArrayList<Integer> BrainShape;
    ArrayList<Class<? extends Layer>> LayerTypes;
    Double LearningRate;

    String Name;
    Integer ID;

    Integer MemorySize;

    Integer Age;
    Double BestLoss;
    Double CurrentLoss;

    public static void LogBuddy(Buddy buddy){
        Log.d("Buddy",buddy.Name);
        Log.d("Buddy",buddy.BrainShape.toString());
        Log.d("Buddy",buddy.LayerTypes.toString());
        Log.d("Buddy",buddy.LearningRate.toString());
        Log.d("Buddy",buddy.MemorySize.toString());
        Log.d("Buddy",buddy.activation.toString());
        Log.d("Buddy",buddy.loss.toString());
        Log.d("Buddy",buddy.ID.toString());
        Log.d("Buddy",buddy.Age.toString());
        Log.d("Buddy",buddy.Inputs.toString());
        Log.d("Buddy",buddy.Outputs.toString());
        Log.d("Buddy",buddy.neuralNetwork.GetWeights().toString());
    }

    static String DefaultName() {
        return "Default";
    }

    static ArrayList<Integer> DefaultBrainShape() {
        ArrayList<Integer> BrainShape = new ArrayList<Integer>();
        BrainShape.add(10);
        BrainShape.add(10);
        BrainShape.add(10);
        BrainShape.add(10);
        BrainShape.add(10);
        BrainShape.add(10);
        BrainShape.add(10);
        BrainShape.add(10);
        return BrainShape;
    }
    static Double DefaultLearningRate() {
        return 0.01;
    }
    static Integer DefaultMemorySize() {
        return 100;
    }
    static Class<? extends Activation> DefaultActivation() {
        return Sigmoid.class;
    }
    static Class<? extends Loss> DefaultLoss() {
        return MeanAbsoluteError.class;
    }
    static ArrayList<Class<? extends Layer>> DefaultLayerTypes() {
        ArrayList<Class<? extends Layer>> LayerTypes = new ArrayList<>();
        for (int i = 0; i < DefaultBrainShape().size()+2; i++) {
            LayerTypes.add(FullyConnectedLayer.class);
        }
        return LayerTypes;
    }

    public static Buddy DefaultBuddy(){
        return new Buddy(DefaultName(), DefaultBrainShape(), DefaultLayerTypes(),DefaultLearningRate(),DefaultMemorySize(), DefaultActivation(), DefaultLoss());
    }
    public Buddy(){
        this(DefaultName(), DefaultBrainShape(), DefaultLayerTypes(),DefaultLearningRate(),DefaultMemorySize(), DefaultActivation(), DefaultLoss());
    }

    public static ArrayList<Double> OneHot(Integer Index,Integer Size){
        ArrayList<Double> onehot = new ArrayList<Double>();
        for (int i = 0; i < Size; i++) {
            if(i==Index){
                onehot.add(1.0);
            }else {
                onehot.add(0.0);
            }
        }
        return onehot;
    }

    public Buddy(String Name, ArrayList<Integer> BrainShape, ArrayList<Class<? extends Layer>> LayerTypes, Double LearningRate, Integer MemorySize, Class<? extends Activation> activation, Class<? extends Loss> loss){
        this.Age=0;
        this.Name=Name;
        this.LearningRate = LearningRate;
        this.MemorySize=MemorySize;
        Inputs=(MemorySize*InputState.GetOneHotEncodeSize())+Toy.GetOneHotEncodeSize()+20;
        Outputs=InputState.GetOneHotEncodeSize();
        this.BrainShape=BrainShape;
        this.LayerTypes=LayerTypes;
        this.activation = activation;
        this.loss = loss;

        ArrayList<Integer> Fixed_BrainShape = new ArrayList<Integer>();
        Fixed_BrainShape.add(Inputs);
        for (Integer i: BrainShape) {
            Fixed_BrainShape.add(i);
        }
        Fixed_BrainShape.add(Outputs);
        neuralNetwork = new NeuralNetwork(Fixed_BrainShape,LayerTypes,activation,loss);
    }

    public void TrainBuddy(Toy toy, Integer DesiredPleasureLevel, ArrayList<InputState> PreviousInputStates, InputState expected, Integer Reward){
        //combine previous input state and toy state
        ArrayList<Double> input = new ArrayList<>();
        for (int i = 0; i < MemorySize; i++) {
            if(PreviousInputStates.size()>i) {
                input.addAll(InputState.OneHotEncode(PreviousInputStates.get(i)));
            }else{
                input.addAll(InputState.OneHotEncode(new InputState()));
            }
        }
        input.addAll(Toy.OneHotEncode(toy));
        input.addAll(OneHot(DesiredPleasureLevel,20));
        neuralNetwork.Train(input,InputState.OneHotEncode(expected),LearningRate);
        //log the entire loss array to see if it is decreasing
        CurrentLoss = neuralNetwork.GetLoss(input,InputState.OneHotEncode(expected));
        if(BestLoss==null||CurrentLoss<BestLoss){
            BestLoss=CurrentLoss;
        }
        Log.d("Loss",CurrentLoss.toString());
        Age++;
    }

    public InputState BuddyGuess(Toy toy,Integer DesiredPleasureLevel,ArrayList<InputState> PreviousInputStates){
        //combine previous input state and toy state
        ArrayList<Double> input = new ArrayList<Double>();
        for (int i = 0; i < MemorySize; i++) {
            if(PreviousInputStates.size()>i) {
                input.addAll(InputState.OneHotEncode(PreviousInputStates.get(i)));
            }else{
                input.addAll(InputState.OneHotEncode(new InputState()));
            }
        }
        input.addAll(Toy.OneHotEncode(toy));
        input.addAll(OneHot(DesiredPleasureLevel,20));
        ArrayList<Double> output = neuralNetwork.Forward(input);
        return InputState.FromOneHotEncode(output);
    }

    public Integer GetMemorySize() {
        return MemorySize;
    }

    public void SetName(String toString) {
        Name=toString;
    }

    public String GetName() {
        return Name;
    }

    public ArrayList<Integer> GetBrainShape() {
        return BrainShape;
    }

    public Double GetLearningRate() {
        return LearningRate;
    }

    public void CreateNeuralNetwork() {
        ArrayList<Integer> Fixed_BrainShape = new ArrayList<Integer>();
        Fixed_BrainShape.add(Inputs);
        for (Integer i: BrainShape) {
            Fixed_BrainShape.add(i);
        }
        Fixed_BrainShape.add(Outputs);
        neuralNetwork = new NeuralNetwork(Fixed_BrainShape,LayerTypes,activation,loss);
    }

    public Class<? extends Activation> GetActivation() {
        return activation;
    }

    public Class<? extends Loss> GetLoss() {
        return loss;
    }

    public void SetLayerType(int position, Class<? extends Layer> type) {
        LayerTypes.set(position,type);
    }

    public void SetActivation(Class<? extends Activation> item) {
        activation = item;
        neuralNetwork.SetActivation(activation);
    }

    public void SetLoss(Class<? extends Loss> item) {
        loss = item;
        neuralNetwork.SetLoss(loss);
    }

    public Class<? extends Layer> GetLayerType(int i) {
        try {
            return LayerTypes.get(i).asSubclass(Layer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void SetWeights(ArrayList<ArrayList<ArrayList<Double>>> weights) {
        neuralNetwork.SetWeights(weights);
    }

    public void SetInputs(Integer Inputs) {
        this.Inputs = Inputs;
    }

    public void SetOutputs(Integer Outputs) {
        this.Outputs = Outputs;
    }

    public void SetBrainShape(ArrayList<Integer> BrainShape) {
        this.BrainShape = BrainShape;
    }

    public void SetLayerTypes(ArrayList<Class<? extends Layer>> layerTypes) {
        LayerTypes = layerTypes;
    }

    public void SetLearningRate(Double learningRate) {
        LearningRate = learningRate;
    }

    public void SetMemorySize(Integer memorySize) {
        MemorySize = memorySize;
    }

    public Double GetBestLoss() {
        return BestLoss;
    }
    public Double GetCurrentLoss() {
        return CurrentLoss;
    }
}
