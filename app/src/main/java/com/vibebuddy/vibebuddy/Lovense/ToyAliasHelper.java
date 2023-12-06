package com.vibebuddy.vibebuddy.Lovense;

import java.util.ArrayList;
import java.util.Comparator;

public class ToyAliasHelper {

	static ArrayList<ToyAlias> ToyAliases;
	private ToyAliasHelper(){
		if(ToyAliases==null) {
			ToyAliases = new ArrayList<ToyAlias>() {{
				add(new ToyAlias("Lapis"));
				add(new ToyAlias("Lush"));
				add(new ToyAlias("Ferri"));
				add(new ToyAlias("Gravity"));
				add(new ToyAlias("Hyphy"));
				add(new ToyAlias("Osci"));
				add(new ToyAlias("Vulse"));
				add(new ToyAlias("Nora"));
				add(new ToyAlias("Flexer"));
				add(new ToyAlias("Dolce", "LVS-J0123"));
				add(new ToyAlias("Ambi"));
				add(new ToyAlias("Exomoon"));
				add(new ToyAlias("Solace"));
				add(new ToyAlias("Max"));
				add(new ToyAlias("Gush"));
				add(new ToyAlias("Calor"));
				add(new ToyAlias("Kraken"));
				add(new ToyAlias("Edge"));
				add(new ToyAlias("Diamo"));
				add(new ToyAlias("Ridge"));
				add(new ToyAlias("Hush"));
				add(new ToyAlias("Domi"));
				add(new ToyAlias("Gemini"));
				add(new ToyAlias("Sex Machine"));
			}};
			//sort Aliases by ToyName alphabetically
			ToyAliases.sort(Comparator.comparing(o -> o.ToyName));
		}
	}

	public static Integer AliasToToyType(String alias){
		for (ToyAlias toyAlias : ToyAliases) {
			if (toyAlias.Aliases.contains(alias)||toyAlias.ToyName.equals(alias)){
				return ToyAliases.indexOf(toyAlias);
			}
		}
		return -1;
	}

	public static String AliasToToyName(String alias){
		for (ToyAlias toyAlias : ToyAliases) {
			if (toyAlias.Aliases.contains(alias)||toyAlias.ToyName.equals(alias)){
				return toyAlias.ToyName;
			}
		}
		return null;
	}

	public static void AddAlias(String toyName, String alias){
		for (ToyAlias toyAlias : ToyAliases) {
			if (toyAlias.ToyName.equals(toyName)){
				toyAlias.Aliases.add(alias);
				return;
			}
		}
		ToyAliases.add(new ToyAlias(toyName,alias));
	}
	private static ToyAliasHelper instance;
	public static ToyAliasHelper getInstance(){
		if(instance==null)
			instance = new ToyAliasHelper();
		return instance;
	}
}
