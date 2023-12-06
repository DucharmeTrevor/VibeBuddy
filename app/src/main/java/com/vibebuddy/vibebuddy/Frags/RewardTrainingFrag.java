package com.vibebuddy.vibebuddy.Frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.lovense.sdklibrary.LovenseToy;
import com.vibebuddy.vibebuddy.Lovense.Toy;
import com.vibebuddy.vibebuddy.MainActivity;
import com.vibebuddy.vibebuddy.R;

public class RewardTrainingFrag extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener {

	SeekBar seekBar;
	Button ClimaxButton;
	Button SearchButton;
	Spinner ToySpinner;
	public RewardTrainingFrag() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_reward_training, container, false);
		seekBar = root.findViewById(R.id.SliderBar);
		ClimaxButton = root.findViewById(R.id.ClimaxButton);
		SearchButton = root.findViewById(R.id.SearchButton);
		ToySpinner = root.findViewById(R.id.ToySelector);

		//on button click
		ClimaxButton.setOnClickListener(this);
		SearchButton.setOnClickListener(this);
		//on seekbar change
		seekBar.setOnSeekBarChangeListener(this);
		//on spinner change
		ToySpinner.setOnItemSelectedListener(this);
		ToySpinner.setAdapter(MainActivity.lovenseManager);
		// Inflate the layout for this fragment
		return root;
	}

	@Override
	public void onClick(View v) {
		if (v == ClimaxButton) {
			MainActivity.lovenseManager.SendCommandToAll(LovenseToy.COMMAND_VIBRATE, 0);
		}
		if (v == SearchButton) {
			MainActivity.lovenseManager.SearchForToys();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Log.d("SeekBar", "Progress: " + progress);
		if(ToySpinner.getSelectedItem() != null)
			if(((Toy)ToySpinner.getSelectedItem()).IsConnected())
				((Toy)ToySpinner.getSelectedItem()).SendCommand(LovenseToy.COMMAND_VIBRATE,progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Log.d("Spinner", "Selected: " + position);
		((Toy)ToySpinner.getSelectedItem()).Connect();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}