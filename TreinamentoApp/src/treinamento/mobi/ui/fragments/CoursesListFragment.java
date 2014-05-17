package treinamento.mobi.ui.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoursesListFragment extends ListFragment {
	JSONArray array;

	String url;
	private View content;

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public static Fragment newInstance(String url) {
		
		//Referenciar uma instancia do Fragment para transporte de argumentos
		
		Fragment f = new CoursesListFragment();
		Bundle args = new Bundle();
		args.putString("URL", url);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		//Carregar argumentos utilizados
		
		super.onCreate(savedInstanceState);
		url = getArguments().getString("URL");

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Inflar o Layout que ser‡ utilizado
		//content = inflater.inflate(R.layout.LAYOUT, null);
		return content;

	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//Buscar dados Rest/DB/XML etc..
		//RestClient.getInstance().getJson(url, callback);

	}

	public class BaseAdapter extends ArrayAdapter<JSONObject> {
		Context context;
		JSONArray array;
		private LayoutInflater inflater;

		public BaseAdapter(Context context, JSONArray array) {
			super(context, android.R.layout.activity_list_item, new ArrayList<JSONObject>());
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
			
			convertView = inflater.inflate(android.R.layout.activity_list_item, null, true);
			
			/*
			ImageLoader imgLoader = new ImageLoader(getActivity()
					.getApplicationContext());
			ImageView profileImage = (ImageView) convertView
					.findViewById(R.id.img_profile_rest);
			int loader = R.drawable.ic;
			String image_url = null;
			try {
				image_url = RestClient.URLMEDIA + getItem(position).getString("home_thumb");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			imgLoader.DisplayImage(image_url, loader, profileImage);
			*/
			
			return convertView;
		}
	}
	
	
}