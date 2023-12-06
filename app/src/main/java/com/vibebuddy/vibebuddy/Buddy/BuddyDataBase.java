package com.vibebuddy.vibebuddy.Buddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Activations.Activation;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Layers.Layer;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.Losses.Loss;
import com.vibebuddy.vibebuddy.Buddy.MachineLearning.NeuralNetwork;

import java.util.ArrayList;

public class BuddyDataBase extends SQLiteOpenHelper
{
	private static final String Table_Name = "buddies";
	private static final String Column_ID = "id";
	private static final String Column_Name = "name";
	private static final String Column_Inputs = "inputs";
	private static final String Column_Outputs = "outputs";
	private static final String Column_BrainShape = "brainshape";
	private static final String Column_LayerTypes = "layertypes";
	private static final String Column_LearningRate = "learningrate";
	private static final String Column_MemorySize = "memorysize";
	private static final String Column_Activation = "activation";
	private static final String Column_Loss = "loss";
	private static final String Column_Weights = "weights";
	public static Boolean Saving = false;


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Table_Name + " (" +
				Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				Column_Name + " TEXT, " +
				Column_Inputs + " INTEGER, " +
				Column_Outputs + " INTEGER, " +
				Column_BrainShape + " TEXT, " +
				Column_LayerTypes + " TEXT, " +
				Column_LearningRate + " REAL, " +
				Column_MemorySize + " INTEGER, " +
				Column_Activation + " TEXT, " +
				Column_Loss + " TEXT, " +
				Column_Weights + " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
		onCreate(db);
	}

	public BuddyDataBase(Context context) {
		super(context, "Buddies.db", null, 1);
		SQLiteDatabase db = getWritableDatabase();
		onCreate(db);
		db.close();
	}

	String BrainShapeToString(ArrayList<Integer> BrainShape){
		String brainShapeString = "";
		for (int i = 0; i < BrainShape.size(); i++) {
			brainShapeString+=BrainShape.get(i)+",";
		}
		return brainShapeString;
	}

	ArrayList<Integer> BrainShapeFromString(String BrainShapeString){
		ArrayList<Integer> BrainShape = new ArrayList<Integer>();
		String[] BrainShapeStringArray = BrainShapeString.split(",");
		for (int i = 0; i < BrainShapeStringArray.length; i++) {
			BrainShape.add(Integer.parseInt(BrainShapeStringArray[i]));
		}
		return BrainShape;
	}

	String WeightsToString(ArrayList<ArrayList<ArrayList<Double>>> weights){
		String weightsString = "";
		for (int i = 0; i < weights.size(); i++) {
			for (int j = 0; j < weights.get(i).size(); j++) {
				for (int k = 0; k < weights.get(i).get(j).size(); k++) {
					weightsString+=weights.get(i).get(j).get(k)+",";
				}
			}
		}
		return weightsString;
	}

	ArrayList<ArrayList<ArrayList<Double>>> WeightsFromString(String weightstring,String BrainShape){
		ArrayList<ArrayList<ArrayList<Double>>> weights = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<Integer> BrainShapeArray = BrainShapeFromString(BrainShape);
		String[] weightstringArray = weightstring.split(",");
		int weightstringArrayIndex=0;
		for (int i = 0; i < BrainShapeArray.size()-1; i++) {
			weights.add(new ArrayList<ArrayList<Double>>());
			for (int j = 0; j < BrainShapeArray.get(i+1); j++) {
				weights.get(i).add(new ArrayList<Double>());
				for (int k = 0; k < BrainShapeArray.get(i); k++) {
					weights.get(i).get(j).add(Double.parseDouble(weightstringArray[weightstringArrayIndex]));
					weightstringArrayIndex++;
				}
			}
		}
		return weights;
	}

	public void AddBuddy(Buddy buddy) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Column_Name, buddy.Name);
		values.put(Column_Inputs, buddy.Inputs);
		values.put(Column_Outputs, buddy.Outputs);
		values.put(Column_BrainShape, BrainShapeToString(buddy.BrainShape));
		ArrayList<String> LayerTypes = new ArrayList<>();
		for (int i = 0; i < buddy.LayerTypes.size(); i++) {
			LayerTypes.add(buddy.LayerTypes.get(i).getName());
		}
		values.put(Column_LayerTypes, TextUtils.join(",", LayerTypes));
		values.put(Column_LearningRate, buddy.LearningRate);
		values.put(Column_MemorySize, buddy.MemorySize);
		values.put(Column_Activation, buddy.activation.getName());
		values.put(Column_Loss, buddy.loss.getName());
		values.put(Column_Weights, WeightsToString(buddy.neuralNetwork.GetWeights()));

		db.insert(Table_Name, null, values);
		db.close();
	}

	public void UpdateBuddy(Buddy buddy) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Column_Name, buddy.Name);
		values.put(Column_Inputs, buddy.Inputs);
		values.put(Column_Outputs, buddy.Outputs);
		values.put(Column_BrainShape, BrainShapeToString(buddy.BrainShape));
		ArrayList<String> LayerTypes = new ArrayList<>();
		for (int i = 0; i < buddy.LayerTypes.size(); i++) {
			LayerTypes.add(buddy.LayerTypes.get(i).getName());
		}
		values.put(Column_LayerTypes, TextUtils.join(",", LayerTypes));
		values.put(Column_LearningRate, buddy.LearningRate);
		values.put(Column_MemorySize, buddy.MemorySize);
		values.put(Column_Activation, buddy.activation.getName());
		values.put(Column_Loss, buddy.loss.getName());
		values.put(Column_Weights, WeightsToString(buddy.neuralNetwork.GetWeights()));

		db.update(Table_Name, values, "id=?", new String[]{String.valueOf(buddy.ID)});
		db.close();
	}

	public void DeleteBuddy(Buddy buddy){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM Buddy WHERE id="+buddy.ID);
		db.close();
	}

	public Buddy GetBuddy(Integer Id){
		SQLiteDatabase db = getWritableDatabase();
		Buddy buddy = new Buddy();
		buddy.ID = Id;
		Cursor cursor = db.rawQuery("SELECT * FROM Buddy WHERE id="+Id, null);
		if(cursor.moveToFirst()){
			buddy.SetName(cursor.getString(1));
			buddy.SetInputs(cursor.getInt(2));
			buddy.SetOutputs(cursor.getInt(3));
			buddy.SetBrainShape(BrainShapeFromString(cursor.getString(4)));
			ArrayList<Class<? extends Layer>> LayerTypes = new ArrayList<>();
			String[] LayerTypesString = cursor.getString(5).split(",");
			for (int i = 0; i < LayerTypesString.length; i++) {
				try {
					LayerTypes.add((Class<? extends Layer>) Class.forName(LayerTypesString[i]));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			buddy.SetLayerTypes(LayerTypes);
			buddy.SetLearningRate(cursor.getDouble(6));
			buddy.SetMemorySize(cursor.getInt(7));
			try {
				buddy.SetActivation((Class<? extends Activation>) Class.forName(cursor.getString(8)));
				buddy.SetLoss((Class<? extends Loss>) Class.forName(cursor.getString(9)));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			buddy.CreateNeuralNetwork();
			buddy.SetWeights(WeightsFromString(cursor.getString(10),cursor.getString(4)));
		}
		cursor.close();
		db.close();
		return buddy;
	}

	public void SaveBuddies(ArrayList<Buddy> buddies) {
		for(Buddy buddy : buddies){
			if(buddy.ID==null){
				AddBuddy(buddy);
			}else{
				UpdateBuddy(buddy);
			}
		}
	}

	public ArrayList<Buddy> LoadBuddies() {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Buddy> buddies = new ArrayList<>();
		Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Name, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					Buddy buddy = new Buddy();
					buddy.SetName(cursor.getString(1));
					buddy.SetInputs(cursor.getInt(2));
					buddy.SetOutputs(cursor.getInt(3));
					buddy.SetBrainShape(BrainShapeFromString(cursor.getString(4)));
					ArrayList<Class<? extends Layer>> LayerTypes = new ArrayList<>();
					String[] LayerTypesString = cursor.getString(5).split(",");
					for (int i = 0; i < LayerTypesString.length; i++) {
						try {
							LayerTypes.add((Class<? extends Layer>) Class.forName(LayerTypesString[i]));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					buddy.SetLayerTypes(LayerTypes);
					buddy.SetLearningRate(cursor.getDouble(6));
					buddy.SetMemorySize(cursor.getInt(7));
					try {
						buddy.SetActivation((Class<? extends Activation>) Class.forName(cursor.getString(8)));
						buddy.SetLoss((Class<? extends Loss>) Class.forName(cursor.getString(9)));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					buddy.CreateNeuralNetwork();
					buddy.SetWeights(WeightsFromString(cursor.getString(10),cursor.getString(4)));
					buddies.add(buddy);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}

		return buddies;
	}

	public boolean isOpen() {
		//check id dbhelper is open
		return getReadableDatabase().isOpen();
	}

	public void DeleteAllBuddies() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE FROM Buddy");
		db.close();
	}
}
