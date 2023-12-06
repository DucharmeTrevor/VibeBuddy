package com.vibebuddy.vibebuddy.Buddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vibebuddy.vibebuddy.Lovense.InputState;
import com.vibebuddy.vibebuddy.Lovense.Toy;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.Layer;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.Loss;

import java.util.ArrayList;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class BuddyManager extends ArrayAdapter<Buddy> {

	static Integer SelectedBuddy=0;
	static ArrayList<Buddy> Buddies= new ArrayList<>();
	WorkManager workManager;
	public BuddyManager(Context context) {
		super(context, android.R.layout.simple_spinner_item,Buddies);
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		workManager = WorkManager.getInstance(context);
		//if sqlite database does not exist create it
		//SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("Buddies.db", null);
		//if buddies table does not exist create it
		//db.execSQL("CREATE TABLE IF NOT EXISTS buddies (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, brain TEXT)");
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
			tv.setText(Buddies.get(position).Name);
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
			tv.setText(Buddies.get(position).Name);
			return (row);
		}
		return row;
	}

	public Buddy GetSelectedBuddy() {
		if(Buddies.size()==0){
			Buddies.add(Buddy.DefaultBuddy());
			notifyDataSetChanged();
		}
		return Buddies.get(SelectedBuddy);
	}

	public void SelectBuddy(Integer buddy) {
		SelectedBuddy = buddy;
	}
	public Integer AddBuddy(Buddy buddy) {
		Buddies.add(buddy);
		notifyDataSetChanged();
		return Buddies.size()-1;
	}

	public void UpdateBuddy(Integer Index, String Name, ArrayList<Integer> Layers, ArrayList<Class<? extends Layer>> LayerTypes, Double LR, Integer MemorySize, Class<? extends Activation> activation, Class<? extends Loss> loss) {
		if(Index>=Buddies.size()){
			Buddies.add(new Buddy());
		}
		if(Index<0){
			Buddies.add(new Buddy());
			Index=0;
		}
		Buddy buddy = Buddies.get(Index);
		buddy.Name = Name;
		buddy.LearningRate = LR;
		buddy.MemorySize = MemorySize;
		buddy.Inputs = (MemorySize * InputState.GetOneHotEncodeSize()) + Toy.GetOneHotEncodeSize() + 20;
		buddy.Outputs = InputState.GetOneHotEncodeSize();
		buddy.BrainShape = Layers;
		buddy.LayerTypes = LayerTypes;
		buddy.activation = activation;
		buddy.loss = loss;
		buddy.CreateNeuralNetwork();
		Buddies.set(Index, buddy);
		notifyDataSetChanged();
	}

	public void SaveBuddies() {
		//add new task to work manager
		workManager.enqueue(new OneTimeWorkRequest.Builder(BuddyDataBaseWorker.class).build());
	}

	public void LoadBuddies() {
		Buddies.clear();
		BuddyDataBase buddyDataBase = new BuddyDataBase(getContext());
		Buddies.addAll(buddyDataBase.LoadBuddies());
		buddyDataBase.close();
		notifyDataSetChanged();
	}

	ArrayList<Buddy> GetBuddies(SQLiteDatabase db){
		return Buddies;
	}

	public Integer BuddyCount() {
		return Buddies.size();
	}

	public void DeleteBuddy(int selectedItemPosition) {
		Buddies.remove(selectedItemPosition);
		notifyDataSetChanged();
	}

	public void DeleteAllBuddies() {
		Buddies.clear();
		notifyDataSetChanged();
	}
}
