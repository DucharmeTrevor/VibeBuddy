package com.vibebuddy.vibebuddy.Frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.vibebuddy.vibebuddy.Buddy.Buddy;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.ActivationAdapter;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.Layer;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.LayerAdapter;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.Loss;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.LossAdapter;
import com.vibebuddy.vibebuddy.MainActivity;
import com.vibebuddy.vibebuddy.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuddySettingsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuddySettingsFrag extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {

	Spinner BuddySelector;
	EditText NameTextBox;
	EditText LayersTextBox;
	EditText LearningRateTextBox;
	EditText MemorySizeTextBox;

	Spinner ActivationSelector;
	ActivationAdapter activationAdapter;
	Spinner LossSelector;
	LossAdapter lossAdapter;
	LinearLayout LayerTypeHolder;
	ArrayList<Spinner> LayerTypeSpinners;
	LayerAdapter layerAdapter;
	Button ApplyButton;
	Button AddBuddyButton;
	Button DeleteBuddyButton;
	Button DeleteAllButton;

	public BuddySettingsFrag() {
		// Required empty public constructor
	}

	public static BuddySettingsFrag newInstance(String param1, String param2) {
		BuddySettingsFrag fragment = new BuddySettingsFrag();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_buddy_settings, container, false);
		BuddySelector = view.findViewById(R.id.BuddySelector);
		BuddySelector.setAdapter(MainActivity.buddyManager);
		BuddySelector.setOnItemSelectedListener(this);

		NameTextBox = view.findViewById(R.id.NameTextBox);
		LayersTextBox = view.findViewById(R.id.LayersTextBox);
		LearningRateTextBox = view.findViewById(R.id.LearningRateTextBox);
		MemorySizeTextBox = view.findViewById(R.id.MemorySizeTextBox);

		activationAdapter = new ActivationAdapter(getContext());
		ActivationSelector = view.findViewById(R.id.ActivationSpinner);
		ActivationSelector.setAdapter(activationAdapter);
		ActivationSelector.setOnItemSelectedListener(this);

		lossAdapter = new LossAdapter(getContext());
		LossSelector = view.findViewById(R.id.LossSpinner);
		LossSelector.setAdapter(lossAdapter);
		LossSelector.setOnItemSelectedListener(this);

		LayerTypeSpinners = new ArrayList<>();
		LayerTypeHolder = view.findViewById(R.id.LayerTypeHolder);

		ApplyButton = view.findViewById(R.id.ApplyButton);
		ApplyButton.setOnClickListener(this);

		AddBuddyButton = view.findViewById(R.id.AddBuddyButton);
		AddBuddyButton.setOnClickListener(this);

		DeleteBuddyButton = view.findViewById(R.id.DeleteBuddyButton);
		DeleteBuddyButton.setOnClickListener(this);

		DeleteAllButton = view.findViewById(R.id.DeleteAllButton);
		DeleteAllButton.setOnClickListener(this);

		layerAdapter = new LayerAdapter(getContext());
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ApplyButton) {
			ArrayList<Integer> Layers = new ArrayList<>();
			String[] LayersString = LayersTextBox.getText().toString().split(",");
			for (int i = 0; i < LayersString.length; i++) {
				Layers.add(Integer.parseInt(LayersString[i]));
			}
			ArrayList<Class<? extends Layer>> LayerTypes = new ArrayList<>();
			for (int i = 0; i < LayerTypeSpinners.size(); i++) {
				LayerTypes.add(layerAdapter.getItem(LayerTypeSpinners.get(i).getSelectedItemPosition()).asSubclass(Layer.class));
			}

			MainActivity.buddyManager.UpdateBuddy(BuddySelector.getSelectedItemPosition(),
					NameTextBox.getText().toString(), Layers,LayerTypes,
					Double.parseDouble(LearningRateTextBox.getText().toString()),
					Integer.parseInt(MemorySizeTextBox.getText().toString()), (Class<? extends Activation>) ActivationSelector.getSelectedItem(),
					(Class<? extends Loss>) LossSelector.getSelectedItem());
					MainActivity.buddyManager.SaveBuddies();
		}
		if (v.getId() == R.id.AddBuddyButton) {
			MainActivity.buddyManager.AddBuddy(Buddy.DefaultBuddy());
			MainActivity.buddyManager.SaveBuddies();
		}
		if(v.getId() == R.id.DeleteBuddyButton){
			MainActivity.buddyManager.DeleteBuddy(BuddySelector.getSelectedItemPosition());
			MainActivity.buddyManager.SaveBuddies();
		}
		if(v.getId() == R.id.DeleteAllButton){
			MainActivity.buddyManager.DeleteAllBuddies();
			MainActivity.buddyManager.SaveBuddies();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(parent.getId() == R.id.BuddySelector) {
			MainActivity.buddyManager.SelectBuddy(position);
			NameTextBox.setText(MainActivity.buddyManager.GetSelectedBuddy().GetName());
			ArrayList<Integer> BrainShape = MainActivity.buddyManager.GetSelectedBuddy().GetBrainShape();
			String BrainShapeString = "";
			for (int i = 0; i < BrainShape.size(); i++) {
				BrainShapeString += BrainShape.get(i).toString();
				if (i < BrainShape.size() - 1) {
					BrainShapeString += ",";
				}
			}
			LayersTextBox.setText(BrainShapeString);
			LearningRateTextBox.setText(String.valueOf(MainActivity.buddyManager.GetSelectedBuddy().GetLearningRate()));
			MemorySizeTextBox.setText(String.valueOf(MainActivity.buddyManager.GetSelectedBuddy().GetMemorySize()));
			ActivationSelector.setSelection(ActivationAdapter.activations.indexOf(MainActivity.buddyManager.GetSelectedBuddy().GetActivation()));
			LossSelector.setSelection(LossAdapter.losses.indexOf(MainActivity.buddyManager.GetSelectedBuddy().GetLoss()));
			LayerTypeHolder.removeAllViewsInLayout();
			LayerTypeSpinners.clear();
			for (int i = 0; i < BrainShape.size()+2; i++) {
				Spinner spinner = new Spinner(getContext());
				spinner.setAdapter(layerAdapter);
				spinner.setOnItemSelectedListener(this);
				spinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
				spinner.setSelection(LayerAdapter.layers.indexOf(MainActivity.buddyManager.GetSelectedBuddy().GetLayerType(i)));
				LayerTypeHolder.addView(spinner);
				LayerTypeSpinners.add(spinner);
			}
		}
		if(parent.getId() == R.id.ActivationSpinner) {
			MainActivity.buddyManager.GetSelectedBuddy().SetActivation(activationAdapter.getItem(position));
		}
		if(parent.getId() == R.id.LossSpinner) {
			MainActivity.buddyManager.GetSelectedBuddy().SetLoss(lossAdapter.getItem(position));
		}
		//if it was a layer type spinner
		if(LayerTypeSpinners.contains(parent)) {
			MainActivity.buddyManager.GetSelectedBuddy().SetLayerType(LayerTypeSpinners.indexOf(parent),layerAdapter.getItem(position));
		}
	}



	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}