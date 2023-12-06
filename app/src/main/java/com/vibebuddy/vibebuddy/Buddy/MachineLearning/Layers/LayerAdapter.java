package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LayerAdapter extends ArrayAdapter<Class<? extends Layer>>{

	public static ArrayList<Class<? extends Layer>> layers = new ArrayList<>();
	public LayerAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item, layers);
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(layers.size()==0) {
			layers.add(FullyConnectedLayer.class);
			layers.add(RecurrentLayer.class);
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
			tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));
			tv.setText(layers.get(position).getSimpleName());
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
			tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
			tv.setText(layers.get(position).getSimpleName());
			return (row);
		}
		return row;
	}
}
