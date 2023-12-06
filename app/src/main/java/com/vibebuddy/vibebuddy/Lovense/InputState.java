package com.vibebuddy.vibebuddy.Lovense;

import android.util.Log;

import java.util.ArrayList;

public class InputState {
	Integer VibratorGeneralSpeed;
	Integer VibratorOneSpeed;
	Integer VibratorTwoSpeed;
	Integer VibratorThreeSpeed;
	Integer RotationSpeed;
	Boolean RotationClockwise;
	Boolean AidLightStatus;
	Boolean LightStatus;

	public InputState(){
		VibratorGeneralSpeed = 0;
		VibratorOneSpeed = 0;
		VibratorTwoSpeed = 0;
		VibratorThreeSpeed = 0;
		RotationSpeed = 0;
		RotationClockwise = false;
		AidLightStatus = false;
		LightStatus = false;
	}
	public InputState(Boolean AidLightStatus, Boolean LightStatus, Integer VibratorGeneralSpeed, Integer VibratorOneSpeed, Integer VibratorTwoSpeed, Integer VibratorThreeSpeed, Integer RotationSpeed, Boolean RotationClockwise){
		this.AidLightStatus = AidLightStatus;
		this.LightStatus = LightStatus;
		this.VibratorGeneralSpeed = VibratorGeneralSpeed;
		this.VibratorOneSpeed = VibratorOneSpeed;
		this.VibratorTwoSpeed = VibratorTwoSpeed;
		this.VibratorThreeSpeed = VibratorThreeSpeed;
		this.RotationSpeed = RotationSpeed;
		this.RotationClockwise = RotationClockwise;
	}

	public void Set(Boolean AidLightStatus, Boolean LightStatus, Integer VibratorGeneralSpeed, Integer VibratorOneSpeed, Integer VibratorTwoSpeed, Integer VibratorThreeSpeed, Integer RotationSpeed, Boolean RotationClockwise){
		this.AidLightStatus = AidLightStatus;
		this.LightStatus = LightStatus;
		this.VibratorGeneralSpeed = VibratorGeneralSpeed;
		this.VibratorOneSpeed = VibratorOneSpeed;
		this.VibratorTwoSpeed = VibratorTwoSpeed;
		this.VibratorThreeSpeed = VibratorThreeSpeed;
		this.RotationSpeed = RotationSpeed;
		this.RotationClockwise = RotationClockwise;
	}
	static ArrayList<Double> OneHot(int pos,int size){
		ArrayList<Double> onehot = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			if(i==pos){
				onehot.add(1.0);
			}else {
				onehot.add(0.0);
			}
		}
		return onehot;
	}
	static Integer OneHotDecode(ArrayList<Double> onehot){
		int max_index = 0;
		for (int i = 0; i < onehot.size(); i++) {
			if(onehot.get(i)>onehot.get(max_index)){
				max_index=i;
			}
		}
		return max_index;
	}
	public static ArrayList<Double> OneHotEncode(InputState state) {
		ArrayList<Double> onehot = new ArrayList<Double>();
		onehot.addAll(OneHot(state.AidLightStatus?1:0,2));
		onehot.addAll(OneHot(state.LightStatus?1:0,2));
		onehot.addAll(OneHot(state.VibratorGeneralSpeed,20));
		onehot.addAll(OneHot(state.VibratorOneSpeed,20));
		onehot.addAll(OneHot(state.VibratorTwoSpeed,20));
		onehot.addAll(OneHot(state.VibratorThreeSpeed,20));
		onehot.addAll(OneHot(state.RotationSpeed,20));
		onehot.addAll(OneHot(state.RotationClockwise?1:0,2));
		return onehot;
	}
	public static Integer GetOneHotEncodeSize() {
		return OneHotEncode(new InputState()).size();
	}

	public static InputState FromOneHotEncode(ArrayList<Double> encoded) {
		if(encoded.size()!=GetOneHotEncodeSize()){
			Log.d("InputState","Encoded size does not match");
			Log.d("InputState","Encoded size: "+encoded.size());
			Log.d("InputState","Expected size: "+GetOneHotEncodeSize());
			return new InputState();
		}
		InputState inputState = new InputState();
		if(OneHotDecode(new ArrayList<Double>(encoded.subList(0,2)))==1){
			inputState.AidLightStatus = true;
		}
		if(OneHotDecode(new ArrayList<Double>(encoded.subList(0,2)))==0){
			inputState.AidLightStatus = false;
		}
		if(OneHotDecode(new ArrayList<Double>(encoded.subList(2,4)))==1){
			inputState.LightStatus = true;
		}
		if(OneHotDecode(new ArrayList<Double>(encoded.subList(2,4)))==0){
			inputState.LightStatus = false;
		}
		inputState.VibratorGeneralSpeed = OneHotDecode(new ArrayList<Double>(encoded.subList(4,24)));
		inputState.VibratorOneSpeed = OneHotDecode(new ArrayList<Double>(encoded.subList(24,44)));
		inputState.VibratorTwoSpeed = OneHotDecode(new ArrayList<Double>(encoded.subList(44,64)));
		inputState.VibratorThreeSpeed = OneHotDecode(new ArrayList<Double>(encoded.subList(64,84)));
		inputState.RotationSpeed = OneHotDecode(new ArrayList<Double>(encoded.subList(84,104)));
		if(OneHotDecode(new ArrayList<Double>(encoded.subList(104,106)))==1){
			inputState.RotationClockwise = true;
		}
		if(OneHotDecode(new ArrayList<Double>(encoded.subList(104,106)))==0){
			inputState.RotationClockwise = false;
		}
		return inputState;
	}
}
