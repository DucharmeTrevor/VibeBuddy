package com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LossAdapter extends ArrayAdapter<Class<? extends Loss>>{
	public static ArrayList<Class<? extends Loss>> losses = new ArrayList<>();
	public LossAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item, losses);
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(losses.size()==0) {
			losses.add(MeanSquaredError.class);
			losses.add(MeanAbsoluteError.class);
			losses.add(MeanAbsolutePercentageError.class);
			losses.add(MeanSquaredLogarithmicError.class);
			losses.add(SquaredHinge.class);
			losses.add(Hinge.class);
			losses.add(CategoricalHinge.class);
			losses.add(LogCosh.class);
			losses.add(CategoricalCrossentropy.class);
			losses.add(SparseCategoricalCrossentropy.class);
			losses.add(BinaryCrossentropy.class);
			losses.add(KullbackLeiblerDivergence.class);
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
			tv.setText(losses.get(position).getSimpleName());
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
			tv.setText(losses.get(position).getSimpleName());
			return (row);
		}
		return row;
	}
}
