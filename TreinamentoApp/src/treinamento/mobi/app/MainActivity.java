package treinamento.mobi.app;

import static treinamento.mobi.app.CommonUtilities.EXTRA_MESSAGE;
import static treinamento.mobi.app.CommonUtilities.SENDER_ID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import treinamento.mobi.push.AlertDialogManager;
import treinamento.mobi.push.ConnectionDetector;
import treinamento.mobi.push.WakeLocker;
import treinamento.mobi.rest.RestClient;
import treinamento.mobi.ui.fragments.AboutFragment;
import treinamento.mobi.ui.fragments.CourseFragment;
import treinamento.mobi.ui.fragments.CoursesListFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity {

	ProgressDialog progress;
	SlidingMenu menuApp;

	private JSONArray arrayCourses;
	
	private ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	
	// Asyntask 
	AsyncTask<Void, Void, Void> mRegisterTask;

	
	@Override
	protected void onResume() {
		super.onResume();
		
		GCMRegistrar.register(this, SENDER_ID);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		
		if (getIntent().getExtras() != null){
			if (getIntent().getExtras().getBoolean("vimDoNotificacao", false)){
				
				alert.showAlertDialog(MainActivity.this,
						"PUSH NOTIFICAÇÃO EXEMPLO",
						"Vim da Notificação", false);
				// stop executing code by return
				return;
				
			}
		}
		
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.d("Treinamento", "ID " + regId);
		
		// Check if regid already presents
		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				Toast.makeText(getApplicationContext(),
						"Already registered with GCM", Toast.LENGTH_LONG)
						.show();
			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
//						ServerUtilities.register(context, "Treinamento",
//								"flaviano@treinamentos.mobi", regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

		// Criando o menu lateral
		menuApp = new SlidingMenu(this);
		menuApp.setMode(SlidingMenu.LEFT);
		menuApp.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menuApp.setShadowWidthRes(R.dimen.shadow_width);
		menuApp.setShadowDrawable(R.drawable.shadow);
		menuApp.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menuApp.setFadeDegree(0.35f);
		menuApp.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menuApp.setMenu(R.layout.menu_app);

		// Configurando nosso action bar
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setTitle("Treinamento");
		getActionBar().setIcon(R.drawable.ic_drawer);

		Fragment go = CoursesListFragment.newInstance(RestClient.getURLBASE()
				+ "course/?api_key=special-key");

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, go).commit();

	}

	public void onCoursesSelected() {

		Fragment go = CoursesListFragment.newInstance(RestClient.getURLBASE()
				+ "course/?api_key=special-key");

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, go).commit();

	}

	public void onTurmaSelected() {

		Log.d("Treinamento", "Turma selecionada");
		menuApp.toggle();

	}

	public void onAboutSelected() {

		Log.d("Treinamento", "About selecionada");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		Fragment about;
		about = new AboutFragment();
		ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		ft.replace(R.id.container, about, "about_app");
		ft.addToBackStack("about");
		ft.commit();

		menuApp.toggle();

	}

	public void onTeachersSelected() {

		Log.d("Treinamento", "Teachers selecionada");
		menuApp.toggle();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		SubMenu sbCursos = menu.getItem(0).getSubMenu();
		sbCursos.clear();

		if (arrayCourses != null) {
			if (arrayCourses.length() > 0) {
				for (int i = 0; i < arrayCourses.length(); i++) {
					try {
						String nameCourse = arrayCourses.getJSONObject(i)
								.getString("name");
						sbCursos.add(nameCourse);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return true;
	}

	public void selectCourse(int position) {
		JSONObject courseSelected = null;
		try {
			courseSelected = arrayCourses.getJSONObject(position);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		Fragment course;
		try {
			course = CourseFragment.newInstance(arrayCourses
					.getJSONObject(position));

			ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			ft.replace(R.id.container, course, "course_selected");
			ft.addToBackStack("course_list");
			ft.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == android.R.id.home) {
			menuApp.toggle();
			return true;
		}

		if (arrayCourses != null){
			for (int i = 0; i < arrayCourses.length(); i++) {
				try {
					if (item.getTitle()
							.toString()
							.equalsIgnoreCase(
									arrayCourses.getJSONObject(i).getString("name"))) {
						selectCourse(i);
						return true;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public JSONArray getArrayCourses() {
		return arrayCourses;
	}

	public void setArrayCourses(JSONArray arrayCourses) {
		this.arrayCourses = arrayCourses;
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

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			Toast.makeText(getApplicationContext(),
					"New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
