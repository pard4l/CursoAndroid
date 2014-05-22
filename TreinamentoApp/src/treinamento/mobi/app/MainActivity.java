package treinamento.mobi.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import treinamento.mobi.rest.RestClient;
import treinamento.mobi.ui.fragments.AboutFragment;
import treinamento.mobi.ui.fragments.CourseFragment;
import treinamento.mobi.ui.fragments.CoursesListFragment;
import android.R.menu;
import android.app.ProgressDialog;
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

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity {

	ProgressDialog progress;
	SlidingMenu menuApp;

	private JSONArray arrayCourses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		// if (savedInstanceState == null) {
		// getFragmentManager().beginTransaction()
		// .replace(R.id.container, new CoursesListFragment()).commit();
		// }
		//
		// ContentResolver cp = getContentResolver();
		// ContentValues values = new ContentValues();
		// values.put(treinamentoContent.Person.Columns.FIRST_NAME.getName(),
		// "Pardal");
		// values.put(treinamentoContent.Person.Columns.BIRTHDAY.getName(), 29);
		// values.put(treinamentoContent.Person.Columns.LAST_NAME.getName(),
		// "Pardal");
		// values.put(treinamentoContent.Person.Columns.PHONE.getName(),
		// "99999999");
		//
		// cp.insert(treinamentoContent.Person.CONTENT_URI, values);
		//
		//
		// Cursor c = cp.query(treinamentoContent.Person.CONTENT_URI, null ,
		// null, null, null);
		// c.moveToFirst();
		//
		// while (!c.isAfterLast()){
		//
		// Log.d("Treinamento",
		// c.getString(treinamentoContent.Person.Columns.ID.getIndex()));
		// Log.d("Treinamento",
		// c.getString(treinamentoContent.Person.Columns.FIRST_NAME.getIndex()));
		// Log.d("Treinamento",
		// c.getInt(treinamentoContent.Person.Columns.BIRTHDAY.getIndex()) +
		// "");
		//
		// c.moveToNext();
		//
		// }

		// String where = "id = ?" ;
		// String val[] = { "1" };

		// cp.delete(treinamentoContent.Person.CONTENT_URI, where, val);
		// http://treinamentos.mobi/api/v1/course/?api_key=special-key

		// progress = new ProgressDialog(MainActivity.this);
		// progress.setMessage("Carregando");
		// progress.show();

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
						Log.d("Treinamento", nameCourse);
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

}
