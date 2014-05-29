package treinamento.mobi.singleton;

import org.json.JSONArray;

public class TreinamentoSingleton {
	
	private static TreinamentoSingleton INSTANCE;
	
	private JSONArray jsonTeachers;

	public static TreinamentoSingleton getINSTANCE() {
		
		if (INSTANCE == null)
			INSTANCE = new TreinamentoSingleton();
		
		return INSTANCE;
	}

	public static void setINSTANCE(TreinamentoSingleton iNSTANCE) {
		INSTANCE = iNSTANCE;
	}

	public JSONArray getJsonTeachers() {
		return jsonTeachers;
	}

	public void setJsonTeachers(JSONArray jsonTeachers) {
		this.jsonTeachers = jsonTeachers;
	}
	
	

}
