package com.vibebuddy.vibebuddy.Lovense;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ToyAlias {

	public String ToyName;
	public ArrayList<String> Aliases = new ArrayList<>();

	public ToyAlias(String toyName, @Nullable String... aliases) {
		ToyName = toyName;
		if (aliases != null) {
			for (String alias : aliases) {
				Aliases.add(alias);
			}
		}
	}
}
