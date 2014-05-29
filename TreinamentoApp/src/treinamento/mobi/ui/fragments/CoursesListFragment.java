package treinamento.mobi.ui.fragments;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import treinamento.mobi.app.MainActivity;
import treinamento.mobi.app.R;
import treinamento.mobi.rest.RestClient;
import treinamento.mobi.rest.RestClient.HttpResponseCallback;
import treinamento.mobi.utils.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CoursesListFragment extends Fragment {
	JSONArray array;

	String url;
	private View content;
	private GridView gridView;

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public static Fragment newInstance(String url) {

		// Referenciar uma instancia do Fragment para transporte de argumentos
		
		Fragment f = new CoursesListFragment();
		Bundle args = new Bundle();
		args.putString("URL", url);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Carregar argumentos utilizados
		super.onCreate(savedInstanceState);
		url = getArguments().getString("URL");
		

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflar o Layout que ser� utilizado
		content = inflater.inflate(R.layout.courses_layout, null);
		gridView = (GridView) content.findViewById(R.id.gridview);
		
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	           //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
	        	
	        	try {
					JSONObject courseSelected = array.getJSONObject(position);
					
					FragmentTransaction ft = getActivity()
							.getSupportFragmentManager().beginTransaction();
	 
					Fragment course;
					try {
						course = CourseFragment.newInstance(array
								.getJSONObject(position));
						
//						ft.setCustomAnimations(R.anim.slide_in_left,
//								R.anim.slide_out_right);
						
						ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
						ft.replace(R.id.container, course, "course_selected");
						ft.addToBackStack("course_list");
						ft.commit();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
//					/////
//					Fragment course = new CourseFragment().newInstance(courseSelected);
//					
//					getActivity().getSupportFragmentManager()
//						.beginTransaction().replace(R.id.container, course)
//						.addToBackStack("course_list").commit();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
	        }
	    });
		
		return content;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Buscar dados Rest/DB/XML etc..
		RestClient.LoadRestClient(getActivity());
		RestClient.getInstance().getJson(url, callBackCourses);

	}

	public HttpResponseCallback callBackCourses = new HttpResponseCallback() {

		@Override
		public void onSucess(JSONObject json) {
			
			try {
				array = json.getJSONArray("list");
				if (array != null){
					((MainActivity)getActivity()).setArrayCourses(array);
					
					getActivity().invalidateOptionsMenu();
					
					gridView.setAdapter(new ImageAdapter(getActivity(), array));
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onError(String erro) {
			// TODO Auto-generated method stub

			Log.d("Treinamento", erro);

		}
	};

	public class ImageAdapter extends BaseAdapter {

		private Context mContext;
		private JSONArray courses;

		public ImageAdapter(Context context, JSONArray array) {
			super(context, array);
			mContext = context;
			courses = array;
		}

		public int getCount() {
			return courses.length();
		}

		public JSONObject getItem(int position) {
			
			try {
				return (JSONObject) courses.get(position);
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
			   row = inflater.inflate(R.layout.row_course, parent, false);
			}
			
			ImageView imageView = (ImageView) row.findViewById(R.id.imageCourse);
			TextView textCourse = (TextView) row.findViewById(R.id.nameCourse);
			
//			if (convertView == null) { // if it's not recycled, initialize some
//										// attributes
//				imageView = new ImageView(mContext);
//				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//				imageView.setPadding(8, 8, 8, 8);
//			} else {
//				imageView = (ImageView) convertView;
//			}

			ImageLoader imgLoader = new ImageLoader(getActivity()
					.getApplicationContext());
			
			int loader = R.drawable.logo;
			String image_url = null;
			try {

				textCourse.setText(getItem(position).getString("name"));
				image_url = RestClient.URLMEDIA
						+ getItem(position).getString("logo");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			imgLoader.DisplayImage(image_url, loader, imageView);

			// imageView.setImageResource(courses.[position]);
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

			/*
			 * ImageLoader imgLoader = new ImageLoader(getActivity()
			 * .getApplicationContext()); ImageView profileImage = (ImageView)
			 * convertView .findViewById(R.id.img_profile_rest); int loader =
			 * R.drawable.ic; String image_url = null; try { image_url =
			 * RestClient.URLMEDIA + getItem(position).getString("home_thumb");
			 * } catch (JSONException e) { e.printStackTrace(); }
			 * imgLoader.DisplayImage(image_url, loader, profileImage);
			 */

			return convertView;
		}
	}

}