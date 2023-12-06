package com.vibebuddy.vibebuddy.Lovense;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ToyControlPanel extends LinearLayout implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
	Toy toy;

	SeekBar RotationSpeedSlider;
	SeekBar GeneralVibrationSpeedSlider;
	SeekBar VibrationMotorOneSpeedSlider;
	SeekBar VibrationMotorTwoSpeedSlider;
	SeekBar VibrationMotorThreeSpeedSlider;
	SeekBar ThrustMotorSpeedSlider;
	CheckBox RotationDirectionCheckbox;
	CheckBox AidLightCheckbox;
	CheckBox LightCheckbox;

	InputState inputState = new InputState(false,false,0,0,0,0,0,false);

	//TODO: Setup Heartbeat for checking if toy is still connected
	//TODO: Setup Toy Disconnect Listener
	public ToyControlPanel(Context context) {
		super(context);
		//set orientation to vertical
		setOrientation(VERTICAL);
		//mach_parent for width and height
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}

	public void setToy(Toy toy) {
		this.toy = toy;
		LinearLayout TextViewLayout = new LinearLayout(getContext());
		TextViewLayout.setOrientation(HORIZONTAL);
		TextViewLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		TextViewLayout.setGravity(CENTER);
		LinearLayout SliderLayout = new LinearLayout(getContext());
		SliderLayout.setOrientation(HORIZONTAL);
		SliderLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
		SliderLayout.setGravity(CENTER);
		LinearLayout CheckboxLayout = new LinearLayout(getContext());
		CheckboxLayout.setOrientation(HORIZONTAL);
		CheckboxLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		CheckboxLayout.setGravity(CENTER);
		switch (toy.GetName()){
			case "Nora":
				//add rotation speed textview
				TextView RotationSpeedTextView = new TextView(TextViewLayout.getContext());
				RotationSpeedTextView.setText("Rotation Speed");
				RotationSpeedTextView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
				RotationSpeedTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
				TextViewLayout.addView(RotationSpeedTextView);
				//add vibration speed textview
				TextView VibrationSpeedTextView = new TextView(TextViewLayout.getContext());
				VibrationSpeedTextView.setText("Vibration Speed");
				VibrationSpeedTextView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
				VibrationSpeedTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
				TextViewLayout.addView(VibrationSpeedTextView);

				LinearLayout RotationSpeedLayout = new LinearLayout(SliderLayout.getContext());
				RotationSpeedLayout.setOrientation(HORIZONTAL);
				RotationSpeedLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
				RotationSpeedLayout.setGravity(CENTER);
				// add rotation speed slider
				RotationSpeedSlider = new SeekBar(RotationSpeedLayout.getContext());
				RotationSpeedSlider.setMax(20);
				RotationSpeedSlider.setProgress(toy.RotationSpeed);
				RotationSpeedSlider.setOnSeekBarChangeListener(this);
				RotationSpeedSlider.setRotation(-90);
				RotationSpeedSlider.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

				RotationSpeedLayout.addView(RotationSpeedSlider);
				SliderLayout.addView(RotationSpeedLayout);

				LinearLayout VibrationSpeedLayout = new LinearLayout(SliderLayout.getContext());
				VibrationSpeedLayout.setOrientation(HORIZONTAL);
				VibrationSpeedLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
				VibrationSpeedLayout.setGravity(CENTER);
				// add vibration speed slider
				GeneralVibrationSpeedSlider = new SeekBar(VibrationSpeedLayout.getContext());
				GeneralVibrationSpeedSlider.setMax(20);
				GeneralVibrationSpeedSlider.setProgress(toy.VibratorGeneralSpeed);
				GeneralVibrationSpeedSlider.setOnSeekBarChangeListener(this);
				GeneralVibrationSpeedSlider.setRotation(-90);
				GeneralVibrationSpeedSlider.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));

				VibrationSpeedLayout.addView(GeneralVibrationSpeedSlider);
				SliderLayout.addView(VibrationSpeedLayout);

				//add rotation direction checkbox
				RotationDirectionCheckbox = new CheckBox(CheckboxLayout.getContext());
				RotationDirectionCheckbox.setChecked(toy.RotationClockwise);
				RotationDirectionCheckbox.setOnCheckedChangeListener(this);
				RotationDirectionCheckbox.setText("Clockwise");
				CheckboxLayout.addView(RotationDirectionCheckbox);
				//add aid light checkbox
				AidLightCheckbox = new CheckBox(CheckboxLayout.getContext());
				AidLightCheckbox.setChecked(toy.AidLightStatus);
				AidLightCheckbox.setOnCheckedChangeListener(this);
				AidLightCheckbox.setText("Aid Light");
				CheckboxLayout.addView(AidLightCheckbox);
				//add light checkbox
				LightCheckbox = new CheckBox(CheckboxLayout.getContext());
				LightCheckbox.setChecked(toy.LightStatus);
				LightCheckbox.setOnCheckedChangeListener(this);
				LightCheckbox.setText("Light");
				CheckboxLayout.addView(LightCheckbox);
				break;
			case "Dolce":
				//add Motor One textview
				TextView MotorOneTextView = new TextView(TextViewLayout.getContext());
				MotorOneTextView.setText("Motor One");
				MotorOneTextView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
				MotorOneTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
				TextViewLayout.addView(MotorOneTextView);
				//add Motor Two textview
				TextView MotorTwoTextView = new TextView(TextViewLayout.getContext());
				MotorTwoTextView.setText("Motor Two");
				MotorTwoTextView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
				MotorTwoTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
				TextViewLayout.addView(MotorTwoTextView);

				//add Motor One slider
				LinearLayout MotorOneLayout = new LinearLayout(SliderLayout.getContext());
				MotorOneLayout.setOrientation(HORIZONTAL);
				MotorOneLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
				MotorOneLayout.setGravity(CENTER);
				VibrationMotorOneSpeedSlider = new SeekBar(MotorOneLayout.getContext());
				VibrationMotorOneSpeedSlider.setMax(20);
				VibrationMotorOneSpeedSlider.setProgress(toy.VibratorOneSpeed);
				VibrationMotorOneSpeedSlider.setOnSeekBarChangeListener(this);
				VibrationMotorOneSpeedSlider.setRotation(-90);
				VibrationMotorOneSpeedSlider.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
				MotorOneLayout.addView(VibrationMotorOneSpeedSlider);
				SliderLayout.addView(MotorOneLayout);
				//add Motor Two slider
				LinearLayout MotorTwoLayout = new LinearLayout(SliderLayout.getContext());
				MotorTwoLayout.setOrientation(HORIZONTAL);
				MotorTwoLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f));
				MotorTwoLayout.setGravity(CENTER);
				VibrationMotorTwoSpeedSlider = new SeekBar(MotorTwoLayout.getContext());
				VibrationMotorTwoSpeedSlider.setMax(20);
				VibrationMotorTwoSpeedSlider.setProgress(toy.VibratorTwoSpeed);
				VibrationMotorTwoSpeedSlider.setOnSeekBarChangeListener(this);
				VibrationMotorTwoSpeedSlider.setRotation(-90);
				VibrationMotorTwoSpeedSlider.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
				MotorTwoLayout.addView(VibrationMotorTwoSpeedSlider);
				SliderLayout.addView(MotorTwoLayout);

				//add Aid Light checkbox
				AidLightCheckbox = new CheckBox(CheckboxLayout.getContext());
				AidLightCheckbox.setChecked(toy.AidLightStatus);
				AidLightCheckbox.setOnCheckedChangeListener(this);
				AidLightCheckbox.setText("Aid Light");
				CheckboxLayout.addView(AidLightCheckbox);
				//add Light checkbox
				LightCheckbox = new CheckBox(CheckboxLayout.getContext());
				LightCheckbox.setChecked(toy.LightStatus);
				LightCheckbox.setOnCheckedChangeListener(this);
				LightCheckbox.setText("Light");
				CheckboxLayout.addView(LightCheckbox);
				break;
			default:
				TextView tv = new TextView(getContext());
				tv.setText("Toy not supported");
				addView(tv);
				break;
		}
		//add layouts
		addView(TextViewLayout);
		addView(SliderLayout);
		addView(CheckboxLayout);
	}

	public InputState getInputState() {
		return inputState;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView==AidLightCheckbox){
			inputState.AidLightStatus = isChecked;
		}
		if(buttonView==LightCheckbox){
			inputState.LightStatus = isChecked;
		}
		if(buttonView==RotationDirectionCheckbox){
			inputState.RotationClockwise = isChecked;
		}
		if(toy!=null) {
			toy.Apply(getInputState());
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(seekBar==RotationSpeedSlider){
			inputState.RotationSpeed = progress;
		}
		if(seekBar==GeneralVibrationSpeedSlider){
			inputState.VibratorGeneralSpeed = progress;
		}
		if(seekBar==VibrationMotorOneSpeedSlider){
			inputState.VibratorOneSpeed = progress;
		}
		if(seekBar==VibrationMotorTwoSpeedSlider){
			inputState.VibratorTwoSpeed = progress;
		}
		if(seekBar==VibrationMotorThreeSpeedSlider){
			inputState.VibratorThreeSpeed = progress;
		}
		if(toy!=null) {
			toy.Apply(getInputState());
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
