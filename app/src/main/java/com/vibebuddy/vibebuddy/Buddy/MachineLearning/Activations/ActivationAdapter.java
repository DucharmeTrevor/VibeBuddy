package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivationAdapter  extends ArrayAdapter<Class<? extends Activation>>{
	public static ArrayList<Class<? extends Activation>> activations = new ArrayList<>();
	public ActivationAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item, activations);
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(activations.size()==0) {
			activations.add(Sigmoid.class);
			activations.add(Tanh.class);
			activations.add(ReLU.class);
			activations.add(LeakyReLU.class);
			activations.add(Linear.class);
		}
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
			tv.setText(activations.get(position).getSimpleName());
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
			tv.setText(activations.get(position).getSimpleName());
			return (row);
		}
		return row;
	}
}
