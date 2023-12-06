package com.vibebuddy.vibebuddy.Lovense;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
import com.lovense.sdklibrary.callBack.OnSearchToyListener;
import com.lovense.sdklibrary.callBack.OnSendCommandErrorListener;
import com.vibebuddy.vibebuddy.R;

import java.util.ArrayList;

public class LovenseManager extends ArrayAdapter<Toy> implements OnSearchToyListener {

	static Lovense lovense;
	static ArrayList<Toy> toys= new ArrayList<>();

	public LovenseManager(Context context,Lovense lovense) {
		super(context, android.R.layout.simple_spinner_item,toys);
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.lovense = lovense;
	}

	//get view for spinner
	@Override
	public View getView(int position, View convertView, android.view.ViewGroup parent) {
		//create a view containing the name of the toy and its ID, Battery, and Connection Status
		View row = convertView;
		if ( row == null )
		{
			final LayoutInflater inflater =  (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, null);

			TextView tv = (TextView) row.findViewById(android.R.id.text1);
			tv.setText(toys.get(position).GetName() + " " + toys.get(position).GetID() + " " + String.valueOf(toys.get(position).GetBattery()) + " " + (toys.get(position).IsConnected()? "Connected":"Disconnected"));
			return (row);
		}
		return row;
	}

	//get view for dropdown
	@Override
	public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
		//create a view containing the name of the toy and its ID, Battery, and Connection Status
		View row = convertView;
		if ( row == null )
		{
			final LayoutInflater inflater =  (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, null);

			TextView tv = (TextView) row.findViewById(android.R.id.text1);
			tv.setText(toys.get(position).GetName() + " " + toys.get(position).GetID() + " " + String.valueOf(toys.get(position).GetBattery()) + " " + (toys.get(position).IsConnected()? "Connected":"Disconnected"));
			return (row);
		}
		return row;
	}

	public void SendCommandToAll(Integer command, Integer value){
		for (Toy toy: toys) {
			toy.SendCommand(command,value);
		}
	}

	public void SendCommandByIndex(Integer index, Integer command, Integer value){
		toys.get(index).SendCommand(command,value);
	}

	public void SendCommandByID(String id, Integer command, Integer value){
		for (Toy toy: toys) {
			if(toy.toy.getToyId().equals(id)){
				lovense.sendCommand(id,command,value);
			}
		}
	}
	public void ConnectToyByIndex(Integer index){
		toys.get(index).Connect();
	}

	public void SearchForToys(){
		lovense.searchToys(this);
	}

	@Override
	public void onSearchToy(LovenseToy lovenseToy) {
		for (Toy toy: toys) {
			if(toy.toy.getToyId().equals(lovenseToy.getToyId())){
				return;
			}
		}

		toys.add(new Toy(lovense,lovenseToy));
	}

	@Override
	public void finishSearch() {
		notifyDataSetChanged();
	}

	@Override
	public void onError(LovenseError lovenseError) {

	}
}
