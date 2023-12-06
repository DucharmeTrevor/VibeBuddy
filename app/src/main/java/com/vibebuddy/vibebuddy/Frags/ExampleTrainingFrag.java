package com.vibebuddy.vibebuddy.Frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.lovense.sdklibrary.LovenseToy;
import com.vibebuddy.vibebuddy.Buddy.BuddyManager;
import com.vibebuddy.vibebuddy.Lovense.InputState;
import com.vibebuddy.vibebuddy.Lovense.Toy;
import com.vibebuddy.vibebuddy.Lovense.ToyControlPanel;
import com.vibebuddy.vibebuddy.MainActivity;
import com.vibebuddy.vibebuddy.R;

import java.util.ArrayList;

public class ExampleTrainingFrag extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener,Runnable {

	FrameLayout ControlPanel;
	Button TrainingToggleButton;
	Button ClimaxButton;
	Button SearchButton;
	Spinner ToySpinner;

	ToyControlPanel toyControlPanel;

	Boolean Training = false;
	Thread ExampleTrainingThread;

	ArrayList<InputState> prev_inputStates = new ArrayList<>();

	InputState inputState = new InputState(false,false,0,0,0,0,0,false);
	private Boolean HasClimaxed;

	TextView BestLossText;
	TextView CurrentLossText;

	public ExampleTrainingFrag() {
		// Required empty public constructor
	}

	//on frag changes
	@Override
	public void onPause(){
		super.onPause();
		Training = false;
	}

	@Override
	public void finalize(){
		Training = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_example_training, container, false);
		ControlPanel = root.findViewById(R.id.ControlPanel);
		TrainingToggleButton = root.findViewById(R.id.TrainingToggleButton);
		ClimaxButton = root.findViewById(R.id.ClimaxButton);
		SearchButton = root.findViewById(R.id.SearchButton);
		ToySpinner = root.findViewById(R.id.ToySelector);
		BestLossText = root.findViewById(R.id.BestLossText);
		CurrentLossText = root.findViewById(R.id.CurrentLossText);

		HasClimaxed = false;
		//on button click
		TrainingToggleButton.setOnClickListener(this);
		ClimaxButton.setOnClickListener(this);
		SearchButton.setOnClickListener(this);
		toyControlPanel = new ToyControlPanel(getContext());
		ControlPanel.addView(toyControlPanel);
		//on spinner change
		ToySpinner.setOnItemSelectedListener(this);
		ToySpinner.setAdapter(MainActivity.lovenseManager);
		// Inflate the layout for this fragment
		return root;
	}

	@Override
	public void onClick(View v) {
		if(v == TrainingToggleButton){
			if(TrainingToggleButton.getText().equals("Start Training")){
				TrainingToggleButton.setText("Stop Training");
				Training = true;
				ExampleTrainingThread = new Thread(this);
				ExampleTrainingThread.start();
			}
			else{
				TrainingToggleButton.setText("Start Training");
				Training = false;
				//MainActivity.buddyManager.SaveBuddies();
			}
		}
		if (v == ClimaxButton) {
			HasClimaxed = true;
		}
		if (v == SearchButton) {
			MainActivity.lovenseManager.SearchForToys();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Log.d("Spinner", "Selected: " + position);
		((Toy)ToySpinner.getSelectedItem()).Connect();
		toyControlPanel.setToy((Toy)ToySpinner.getSelectedItem());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private void updateTextView(final String s,final Integer tvid) {
		//get MainActivity
		this.getActivity().runOnUiThread(() -> {
			TextView tv= (TextView) getView().findViewById(tvid);
			tv.setText(s);
		});
	}

	@Override
	public void run() {
		Thread InputStateThread = new Thread(() -> {
			while(Training){
				prev_inputStates.add(inputState);
				inputState = toyControlPanel.getInputState();
				if(prev_inputStates.size()>MainActivity.buddyManager.GetSelectedBuddy().GetMemorySize()){
					prev_inputStates.remove(0);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		InputStateThread.start();

		while(Training){
			if(ToySpinner.getSelectedItem() == null) {
				Training = false;
				TrainingToggleButton.setText("Start Training");
				break;
			}
			if(HasClimaxed){
				MainActivity.buddyManager.GetSelectedBuddy().TrainBuddy((Toy)ToySpinner.getSelectedItem(),21,prev_inputStates,inputState,2);
				HasClimaxed = false;
			}else{
				MainActivity.buddyManager.GetSelectedBuddy().TrainBuddy((Toy)ToySpinner.getSelectedItem(),20,prev_inputStates,inputState,1);
			}
			updateTextView("Best Loss: "+MainActivity.buddyManager.GetSelectedBuddy().GetBestLoss().toString(),R.id.BestLossText);
			updateTextView("Current Loss: "+MainActivity.buddyManager.GetSelectedBuddy().GetCurrentLoss().toString(),R.id.CurrentLossText);
		}
		MainActivity.buddyManager.SaveBuddies();
	}
}