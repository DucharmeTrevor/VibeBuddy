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
import com.vibebuddy.vibebuddy.Lovense.InputState;
import com.vibebuddy.vibebuddy.Lovense.LovenseManager;
import com.vibebuddy.vibebuddy.Lovense.Toy;
import com.vibebuddy.vibebuddy.MainActivity;
import com.vibebuddy.vibebuddy.R;

import java.util.ArrayList;

public class BuddyControlFrag extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener,Runnable {

	Boolean BuddyControl = false;
	Thread ExampleTrainingThread;

	Integer DesiredPleasureLevel = 0;
	SeekBar seekBar;
	Button EnableButton;
	Button ClimaxButton;
	Button SearchButton;
	Spinner ToySpinner;
	public BuddyControlFrag() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_buddy_control, container, false);
		seekBar = root.findViewById(R.id.SliderBar);
		EnableButton = root.findViewById(R.id.EnableButton);
		ClimaxButton = root.findViewById(R.id.ClimaxButton);
		SearchButton = root.findViewById(R.id.SearchButton);
		ToySpinner = root.findViewById(R.id.ToySelector);

		//clear other onclick listeners
		//on button click
		EnableButton.setOnClickListener(this);
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
	protected void finalize() throws Throwable {
		super.finalize();
		BuddyControl = false;
	}

	@Override
	public void onClick(View v) {
		if (v == ClimaxButton) {
			MainActivity.lovenseManager.SendCommandToAll(LovenseToy.COMMAND_VIBRATE, 0);
		}
		if (v == SearchButton) {
			MainActivity.lovenseManager.SearchForToys();
		}
		if (v == EnableButton) {
			if(MainActivity.buddyManager.GetSelectedBuddy()==null){
				Log.d("Buddy","No buddy selected");
				return;
			}
			if(ToySpinner.getSelectedItem()==null){
				Log.d("Buddy","No toy selected");
				return;
			}
			if (EnableButton.getText().equals("Enable")) {
				EnableButton.setText("Disable");
				BuddyControl = true;
				ExampleTrainingThread = new Thread(this);
				ExampleTrainingThread.start();
			} else {
				EnableButton.setText("Enable");
				BuddyControl = false;
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Log.d("SeekBar", "Progress: " + progress);
		DesiredPleasureLevel = progress;
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

	@Override
	public void run() {
		ArrayList<InputState> prev_inputStates = new ArrayList<InputState>();
		while (BuddyControl){
			InputState inputState = MainActivity.buddyManager.GetSelectedBuddy().BuddyGuess(((Toy)ToySpinner.getSelectedItem()),DesiredPleasureLevel,prev_inputStates);
			((Toy)ToySpinner.getSelectedItem()).Apply(inputState);
			prev_inputStates.add(inputState);
			if(prev_inputStates.size()>MainActivity.buddyManager.GetSelectedBuddy().GetMemorySize())
				prev_inputStates.remove(0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		((Toy)ToySpinner.getSelectedItem()).Apply(new InputState(false,false,0,0,0,0,0,false));
	}
}