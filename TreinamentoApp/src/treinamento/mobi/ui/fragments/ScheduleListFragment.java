package treinamento.mobi.ui.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import treinamento.mobi.app.R;
import treinamento.mobi.rest.RestClient;
import treinamento.mobi.rest.RestClient.HttpResponseCallback;
import treinamento.mobi.singleton.TreinamentoSingleton;
import treinamento.mobi.utils.ImageLoader;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleListFragment extends Fragment {

	JSONArray schedules;
	View content;
	String url;

	ListView listview;

	ProgressDialog progress;

	public static Fragment newInstance(String url) {

		Fragment f = new ScheduleListFragment();
		Bundle args = new Bundle();
		args.putString("URL", url);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		url = getArguments().getString("URL");
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		content = inflater.inflate(R.layout.schedules_layout, null);

		listview = (ListView) content.findViewById(R.id.listview);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				try {

					String short_course = schedules.getJSONObject(position)
							.getJSONObject("course").getString("short");

					String url = "http://treinamentos.mobi/course/";
					url += short_course + "/";

					Fragment schedule_frag = ScheduleFragment.newInstance(url);

					getActivity().getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.container, schedule_frag).commit();

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		});
		return content;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		progress = new ProgressDialog(getActivity());
		progress.setTitle("Treinamento");
		// progress.setCancelable(false);
		progress.setMessage("Carregando ...");
		progress.show();

		RestClient.LoadRestClient(getActivity());
		RestClient.getInstance().getJson(url, callback);

		super.onActivityCreated(savedInstanceState);
	}

	HttpResponseCallback callback = new HttpResponseCallback() {

		@Override
		public void onSucess(JSONObject json) {
			try {
				schedules = json.getJSONArray("list");
				listview.setAdapter(new ListAdapter(getActivity(), schedules));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			progress.dismiss();
		}

		@Override
		public void onError(String erro) {
			progress.dismiss();
		}
	};

	public class ListAdapter extends BaseAdapter {

		private Context mContext;
		private JSONArray schedules;

		public ListAdapter(Context context, JSONArray array) {
			super(context, array);
			mContext = context;
			schedules = array;
		}

		public int getCount() {
			return schedules.length();
		}

		public JSONObject getItem(int position) {

			try {
				return (JSONObject) schedules.get(position);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (getActivity()).getLayoutInflater();
				row = inflater.inflate(R.layout.row_schedule, parent, false);
			}

			ImageView img_background = (ImageView) row
					.findViewById(R.id.img_background);
			// ImageView img_icon = (ImageView) row.findViewById(R.id.img_icon);
			TextView textCourse = (TextView) row.findViewById(R.id.txt_title);
			TextView textTeacher = (TextView) row
					.findViewById(R.id.txt_teacher);

			ImageLoader imgLoader = new ImageLoader(getActivity()
					.getApplicationContext());

			int loader = R.drawable.back_schedule;
			String image_url = null;

			JSONObject course = null;
			try {
				course = getItem(position).getJSONObject("course");
				textCourse.setText(course.getString("name"));

				if (course.getString("banner") != null) {
					image_url = RestClient.URLMEDIA
							+ course.getString("banner");

					imgLoader.DisplayImage(image_url, loader, img_background);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				// resource_uri
				for (int i = 0; i < TreinamentoSingleton.getINSTANCE()
						.getJsonTeachers().length(); i++) {
					if (((JSONObject) TreinamentoSingleton.getINSTANCE()
							.getJsonTeachers().get(i))
							.getString("resource_uri").equals(
									course.getJSONArray("teachers")
											.getString(0))) {

						textTeacher.setText(((JSONObject) TreinamentoSingleton
								.getINSTANCE().getJsonTeachers().get(i))
								.getString("name"));

						break;

					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			// loader = R.drawable.logo;
			// imgLoader.DisplayImage(image_url, loader, img_icon);

			return row;
		}

	}

	public class BaseAdapter extends ArrayAdapter<JSONObject> {
		Context context;
		JSONArray array;
		private LayoutInflater inflater;

		public BaseAdapter(Context context, JSONArray array) {
			super(context, android.R.layout.activity_list_item,
					new ArrayList<JSONObject>());
			this.context = context;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			for (int i = 0; i < array.length(); i++) {
				try {
					this.add(array.getJSONObject(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = inflater.inflate(android.R.layout.activity_list_item,
					null, true);

			return convertView;
		}
	}

}
