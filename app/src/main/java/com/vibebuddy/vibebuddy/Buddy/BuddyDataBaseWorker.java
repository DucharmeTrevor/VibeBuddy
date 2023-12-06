package com.vibebuddy.vibebuddy.Buddy;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BuddyDataBaseWorker extends Worker {

	public BuddyDataBaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
	}

	@NonNull
	@Override
	public Result doWork() {
		try {
			Log.d("Buddy", "Saving Buddies");
			BuddyDataBase buddyDataBase = new BuddyDataBase(getApplicationContext());
			if(BuddyManager.Buddies.size()<=0){
				buddyDataBase.DeleteAllBuddies();
			}else {
				buddyDataBase.SaveBuddies(BuddyManager.Buddies);
			}
			buddyDataBase.close();
			Log.d("Buddy", "Saved Buddies");
			return Result.success();
		} catch (Exception e) {
			Log.e("Buddy", "Error saving buddies: " + e.getMessage());
			return Result.failure();
		}
	}
}
