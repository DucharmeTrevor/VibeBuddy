package com.vibebuddy.vibebuddy.Lovense;

import android.util.Log;

import com.lovense.sdklibrary.Lovense;
import com.lovense.sdklibrary.LovenseToy;
import com.lovense.sdklibrary.callBack.LovenseError;
import com.lovense.sdklibrary.callBack.OnCallBackAidLightStatusListener;
import com.lovense.sdklibrary.callBack.OnCallBackBatteryListener;
import com.lovense.sdklibrary.callBack.OnCallBackDeviceTypListener;
import com.lovense.sdklibrary.callBack.OnCallBackLightStatusListener;
import com.lovense.sdklibrary.callBack.OnCallBackMoveListener;
import com.lovense.sdklibrary.callBack.OnCommandErrorListener;
import com.lovense.sdklibrary.callBack.OnCommandSuccessListener;
import com.lovense.sdklibrary.callBack.OnConnectListener;
import com.lovense.sdklibrary.callBack.OnErrorListener;
import com.lovense.sdklibrary.callBack.OnSendCommandErrorListener;

import java.util.ArrayList;

public class Toy implements OnCallBackBatteryListener, OnErrorListener, OnCallBackAidLightStatusListener, OnCallBackLightStatusListener,
		OnCallBackDeviceTypListener, OnCallBackMoveListener, OnCommandErrorListener, OnCommandSuccessListener, OnConnectListener, OnSendCommandErrorListener
{
	LovenseToy toy;
	ToyAliasHelper toyAliasHelper;
	Lovense lovense;
	Boolean connected;
	String name;
	Integer DeviceType;
	Integer battery;
	Boolean AidLightStatus;
	Boolean LightStatus;
	Integer VibratorGeneralSpeed;
	Integer VibratorOneSpeed;
	Integer VibratorTwoSpeed;
	Integer VibratorThreeSpeed;
	Integer RotationSpeed;
	Boolean RotationClockwise;


	ArrayList<ToyDisconnectListener> toyDisconnectListeners = new ArrayList<>();
	public void AddToyDisconnectListener(ToyDisconnectListener toyDisconnectListener){
		toyDisconnectListeners.add(toyDisconnectListener);
	}
	public void RemoveToyDisconnectListener(ToyDisconnectListener toyDisconnectListener){
		toyDisconnectListeners.remove(toyDisconnectListener);
	}
	public void RemoveAllToyDisconnectListeners(){
		toyDisconnectListeners.clear();
	}

	ArrayList<ToyMoveWaggleListener> toyMoveWaggleListeners = new ArrayList<>();
	public void AddToyMoveWaggleListener(ToyMoveWaggleListener toyMoveWaggleListener){
		toyMoveWaggleListeners.add(toyMoveWaggleListener);
	}
	public void RemoveToyMoveWaggleListener(ToyMoveWaggleListener toyMoveWaggleListener){
		toyMoveWaggleListeners.remove(toyMoveWaggleListener);
	}
	public void RemoveAllToyMoveWaggleListeners(){
		toyMoveWaggleListeners.clear();
	}

	public Toy(Lovense lovense, LovenseToy lovenseToy){
		toyAliasHelper= ToyAliasHelper.getInstance();
		this.lovense = lovense;
		toy = lovenseToy;
		battery = toy.getBattery();
		name = toyAliasHelper.AliasToToyName(toy.getDeviceName());
		DeviceType = toyAliasHelper.AliasToToyType(toy.getDeviceName());
		connected = false;
		AidLightStatus = false;
		LightStatus = false;
		VibratorGeneralSpeed = 0;
		VibratorOneSpeed = 0;
		VibratorTwoSpeed = 0;
		VibratorThreeSpeed = 0;
		RotationSpeed = 0;
		RotationClockwise = false;

		lovense.addListener(toy.getToyId(), this);
	}


	public Toy(){
		toyAliasHelper= ToyAliasHelper.getInstance();
		toy = new LovenseToy();
		battery = toy.getBattery();
		name = toyAliasHelper.AliasToToyName(toy.getDeviceName());
		connected = false;
		AidLightStatus = false;
		LightStatus = false;
		VibratorGeneralSpeed = 0;
		VibratorOneSpeed = 0;
		VibratorTwoSpeed = 0;
		VibratorThreeSpeed = 0;
		RotationSpeed = 0;
		RotationClockwise = false;

	}

	public Boolean IsConnected(){
		return connected;
	}

	public String GetName(){
		return name;
	}

	public String GetID(){
		return toy.getToyId();
	}

	public Integer GetBattery(){
		return battery;
	}

	public void SendCommand(Integer command, Integer value){
		lovense.sendCommand(toy.getToyId(),command,value);
	}

	public void SetAidLightStatus(Boolean aidLightStatus) {
		AidLightStatus = aidLightStatus;
		if (aidLightStatus) {
			SendCommand(LovenseToy.COMMAND_ALIGHT_ON, 0);
		} else {
			SendCommand(LovenseToy.COMMAND_ALIGHT_OFF, 0);
		}
	}

	public void SetLightStatus(Boolean lightStatus) {
		LightStatus = lightStatus;
		if (lightStatus) {
			SendCommand(LovenseToy.COMMAND_LIGHT_ON, 0);
		} else {
			SendCommand(LovenseToy.COMMAND_LIGHT_OFF, 0);
		}
	}

	public void VibrateGeneral(Integer value){
		VibratorGeneralSpeed = value;
		SendCommand(LovenseToy.COMMAND_VIBRATE,value);
	}

	public void VibrateMotorOne(Integer value){
		VibratorOneSpeed = value;
		SendCommand(LovenseToy.COMMAND_VIBRATE1,value);
	}

	public void VibrateMotorTwo(Integer value){
		VibratorTwoSpeed = value;
		SendCommand(LovenseToy.COMMAND_VIBRATE2,value);
	}

	public void VibrateMotorThree(Integer value){
		VibratorThreeSpeed = value;
		//SendCommand(LovenseToy.COMMAND_VIBRATE3,value);
	}

	public void Rotate(Integer value, Boolean clockwise){
		RotationSpeed = value;
		if(clockwise){
			RotationClockwise = true;
			SendCommand(LovenseToy.COMMAND_ROTATE_CLOCKWISE,value);
		}else{
			RotationClockwise = false;
			SendCommand(LovenseToy.COMMAND_ROTATE_ANTI_CLOCKWISE,value);
		}
	}
	@Override
	public void aidLightStatus(String s, Integer integer) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " AidLightStatus: " + integer.toString());
		AidLightStatus = integer == 1;
	}

	@Override
	public void battery(String s, int i) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " Battery: " + i);
		battery = i;
	}

	@Override
	public void deviceType(String s, LovenseToy lovenseToy) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " DeviceType: " + lovenseToy.getDeviceName());
		toy = lovenseToy;
		name = toyAliasHelper.AliasToToyName(toy.getDeviceName());
	}

	@Override
	public void lightStatus(String s, Integer integer) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " LightStatus: " + integer.toString());
		LightStatus = integer == 1;
	}

	@Override
	public void moveWaggle(String s) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " MoveWaggle"+ s);
	}

	@Override
	public void commandError(String s) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " CommandError: " + s);
	}

	@Override
	public void commandSuccess(String s) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " CommandSuccess: " + s);
	}

	@Override
	public void onConnect(String s, String s1) {
		connected = true;
		battery = toy.getBattery();
		name = toyAliasHelper.AliasToToyName(toy.getDeviceName());
	}

	@Override
	public void onError(LovenseError lovenseError) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " Error: " + lovenseError.getMessage());
	}

	@Override
	public void sendCommandError(String s, LovenseError lovenseError) {
		Log.d("Toy", "Toy ID: " + toy.getToyId() + " SendCommandError: " + lovenseError.getMessage());
	}

	public void Connect() {
		lovense.connectToy(toy.getToyId(),this);
	}

	public void Disconnect() {
		lovense.disconnect(toy.getToyId());
	}

	public void Apply(InputState inputState){
		if(inputState.AidLightStatus!=this.AidLightStatus){
			SetAidLightStatus(inputState.AidLightStatus);
		}
		if(inputState.LightStatus!=this.LightStatus){
			SetLightStatus(inputState.LightStatus);
		}
		if(inputState.VibratorGeneralSpeed!=this.VibratorGeneralSpeed){
			VibrateGeneral(inputState.VibratorGeneralSpeed);
		}
		if(inputState.VibratorOneSpeed!=this.VibratorOneSpeed){
			VibrateMotorOne(inputState.VibratorOneSpeed);
		}
		if(inputState.VibratorTwoSpeed!=this.VibratorTwoSpeed){
			VibrateMotorTwo(inputState.VibratorTwoSpeed);
		}
		if(inputState.VibratorThreeSpeed!=this.VibratorThreeSpeed){
			VibrateMotorThree(inputState.VibratorThreeSpeed);
		}
		if(inputState.RotationSpeed!=this.RotationSpeed || inputState.RotationClockwise!=this.RotationClockwise){
			Rotate(inputState.RotationSpeed,inputState.RotationClockwise);
		}
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
	public static ArrayList<Double> OneHotEncode(Toy toy) {
		ArrayList<Double> onehot = new ArrayList<Double>();
		onehot.addAll(OneHot(toy.AidLightStatus?1:0,2));
		onehot.addAll(OneHot(toy.LightStatus?1:0,2));
		onehot.addAll(OneHot(toy.VibratorGeneralSpeed,20));
		onehot.addAll(OneHot(toy.VibratorOneSpeed,20));
		onehot.addAll(OneHot(toy.VibratorTwoSpeed,20));
		onehot.addAll(OneHot(toy.VibratorThreeSpeed,20));
		onehot.addAll(OneHot(toy.RotationSpeed,20));
		onehot.addAll(OneHot(toy.RotationClockwise?1:0,2));
		return onehot;
	}

	public static Integer GetOneHotEncodeSize() {
		return OneHotEncode(new Toy()).size();
	}

	public Integer GetVibratorOneLevel() {
		return VibratorOneSpeed;
	}

	public Integer GetVibratorTwoLevel() {
		return VibratorTwoSpeed;
	}

	public Integer GetVibratorThreeLevel() {
		return VibratorThreeSpeed;
	}

	public Integer GetVibratorGeneralLevel() {
		return VibratorGeneralSpeed;
	}

	public Integer GetRotationLevel() {
		return RotationSpeed;
	}

	public Boolean GetRotationDirection() {
		return RotationClockwise;
	}

	public Boolean GetAidLightStatus() {
		return AidLightStatus;
	}

	public Boolean GetLightStatus() {
		return LightStatus;
	}

	public Integer GetDeviceType() {
		return DeviceType;
	}

	public void Thrust(int progress) {
		//SendCommand(LovenseToy.COMMAND_THRUST,progress);
	}
}
