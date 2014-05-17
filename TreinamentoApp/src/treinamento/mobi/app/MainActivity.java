package treinamento.mobi.app;

import org.json.JSONObject;

import treinamento.mobi.rest.RestClient;
import treinamento.mobi.rest.RestClient.HttpResponseCallback;
import treinamento.mobi.ui.fragments.CoursesListFragment;
import treinamentos.mobi.db.data.provider.treinamentoContent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class MainActivity extends FragmentActivity {
	
	
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.replace(R.id.container, new CoursesListFragment()).commit();
//		}
//		
//		ContentResolver cp = getContentResolver();
//		ContentValues values = new ContentValues();
//		values.put(treinamentoContent.Person.Columns.FIRST_NAME.getName(), "Pardal");
//		values.put(treinamentoContent.Person.Columns.BIRTHDAY.getName(), 29);
//		values.put(treinamentoContent.Person.Columns.LAST_NAME.getName(), "Pardal");
//		values.put(treinamentoContent.Person.Columns.PHONE.getName(), "99999999");
//		
//		cp.insert(treinamentoContent.Person.CONTENT_URI, values);
//		
//		
//		Cursor c = cp.query(treinamentoContent.Person.CONTENT_URI, null , null, null, null);
//		c.moveToFirst();
//		
//		while (!c.isAfterLast()){
//			
//			Log.d("Treinamento", c.getString(treinamentoContent.Person.Columns.ID.getIndex()));
//			Log.d("Treinamento", c.getString(treinamentoContent.Person.Columns.FIRST_NAME.getIndex()));
//			Log.d("Treinamento", c.getInt(treinamentoContent.Person.Columns.BIRTHDAY.getIndex()) + "");
//			
//			c.moveToNext();
//			
//		}
		
//		String where = "id = ?" ;
//		String val[] = { "1" };
		
		//cp.delete(treinamentoContent.Person.CONTENT_URI, where, val);
		//http://treinamentos.mobi/api/v1/course/?api_key=special-key
	
		
//		progress = new ProgressDialog(MainActivity.this);
//		progress.setMessage("Carregando");
//		progress.show();
		
		Fragment go = CoursesListFragment
				.newInstance(RestClient.getURLBASE() + "course/?api_key=special-key");
		
		getSupportFragmentManager().beginTransaction().replace(R.id.container, go).commit();
				
	}
	
	HttpResponseCallback callback = new HttpResponseCallback() {
		
		@Override
		public void onSucess(JSONObject json) {
			// TODO Auto-generated method stub
//			progress.dismiss();
			Log.d("Treinamento", json.toString());
			
			
		}
		
		@Override
		public void onError(String erro) {
			// TODO Auto-generated method stub
//			progress.dismiss();
			Log.d("Treinamento", erro);
			
		}
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
