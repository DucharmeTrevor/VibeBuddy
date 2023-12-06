package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers;

import android.util.Log;

import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.Loss;

import java.util.ArrayList;

public class FullyConnectedLayer implements Layer {

    Activation activation;
    ArrayList<ArrayList<Double>> w;
    ArrayList<Double> b;
    ArrayList<Double> z;
    ArrayList<Double> a;

    Integer Inputs;
    Integer Outputs;

    public Integer GetInputs(){
        return Inputs;
    }
    public Integer GetOutputs(){
        return Outputs;
    }
    public FullyConnectedLayer() {

    }
    @Override
    public void Resize(Integer Inputs,Integer Outputs){
        w = new ArrayList<ArrayList<Double>>();
        b = new ArrayList<Double>();
        z = new ArrayList<Double>();
        a = new ArrayList<Double>();
        for (int i = 0; i < Outputs; i++) {
            w.add(new ArrayList<Double>(Inputs));
            for (int j = 0; j < Inputs; j++) {
                w.get(i).add(Math.random()*2-1);
            }
            b.add(Math.random()*2-1);
            z.add(0.0);
            a.add(0.0);
        }
        this.Inputs=Inputs;
        this.Outputs=Outputs;
    }
    @Override
    public void SetActivation(Class<? extends Activation> activation){
        try {
            this.activation = activation.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
    public FullyConnectedLayer(Integer Inputs, Integer Outputs, Class<? extends Activation> activation) {
        Resize(Inputs,Outputs);
        SetActivation(activation);
    }

    static {
        System.loadLibrary("vibebuddy");
    }

    //native forward pass c++/JNI
    public native ArrayList<Double> ForwardNative(ArrayList<Double> x, ArrayList<ArrayList<Double>> w,ArrayList<Double> z, ArrayList<Double> b, Integer Inputs, Integer Outputs,Integer Threads,Activation activation);
    public native ArrayList<Double> BackwardNative(ArrayList<Double> x, ArrayList<ArrayList<Double>> w, ArrayList<Double> z, ArrayList<Double> b, ArrayList<Double> fg, Double lr, Integer Inputs, Integer Outputs, Integer Threads, Activation activation);

                                                   @Override
    public ArrayList<Double> Forward(ArrayList<Double> x){
        if(x.size()!=Inputs){
            Log.d("FullyConnectedLayer","Inputs: "+ Inputs +" x.size(): "+x.size());
            throw new RuntimeException("Inputs: "+Inputs.toString()+" x.size(): "+x.size());
        }

        Integer Threads=24;

        z = new ArrayList<>();
        a = new ArrayList<>();
        for (int i = 0; i < Outputs; i++) {
            z.add(0.0);
            a.add(0.0);
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < Threads; i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                Integer Outputs_Start = Outputs/Threads* finalI;
                Integer Outputs_End = Outputs/Threads*(finalI +1);

                for (int j = Outputs_Start; j < Outputs_End; j++) {
                    Double sum = 0.0;
                    for (int k = 0; k < Inputs; k++) {
                        if(Inputs!=w.get(j).size()||Outputs!=w.size()||Inputs!=x.size()){
                            Log.d("FullyConnectedLayer","Inputs: "+Inputs.toString()+" Outputs: "+Outputs.toString()+" w.get(j).size(): "+w.get(j).size()+" x.size(): "+x.size());
                        }
                        sum += w.get(j).get(k) * x.get(k);
                    }
                    z.set(j, sum + b.get(j));
                    a.set(j, activation.Activation(sum + b.get(j)));
                }
            }));
        }


        for (int i = 0; i < Threads; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < Threads; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //ForwardNative(x,w,z,b,Inputs,Outputs,Threads,activation);

        if(a.size()!=Outputs){
            Log.d("FullyConnectedLayer","Outputs: "+Outputs.toString()+" a.size(): "+a.size());
            throw new RuntimeException("Outputs: "+Outputs.toString()+" a.size(): "+a.size());
        }

        return a;
    }

    @Override
    public ArrayList<Double> Backward(ArrayList<Double> x, ArrayList<Double> fg, Double lr){
        Integer Threads=24;

        ArrayList<Double> dA_dX = new ArrayList<>();
        for (int i = 0; i < Inputs; i++) {
            dA_dX.add(0.0);
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < Threads; i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                Integer Outputs_Start = Outputs/Threads* finalI;
                Integer Outputs_End = Outputs/Threads*(finalI +1);

                for (int j = Outputs_Start; j < Outputs_End; j++) {
                    b.set(j, b.get(j) - fg.get(j) * activation.ActivationPrime(z.get(j)) * lr);
                    for (int k = 0; k < Inputs; k++) {
                        w.get(j).set(k, w.get(j).get(k) - fg.get(j) * activation.ActivationPrime(z.get(j)) * x.get(k) * lr);
                        dA_dX.set(k, dA_dX.get(k) + fg.get(j) * activation.ActivationPrime(z.get(j)) * w.get(j).get(k));
                    }
                }
            }));
        }

        for (int i = 0; i < Threads; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < Threads; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //BackwardNative(x,w,z,b,fg,lr,Inputs,Outputs,Threads,activation);

        return dA_dX;
    }
    @Override
	public ArrayList<ArrayList<Double>> GetWeights() {
        return w;
    }

    @Override
    public void SetWeights(ArrayList<ArrayList<Double>> weights) {
        w = weights;
    }
}
