package treinamento.mobi.ui.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import treinamento.mobi.app.R;
import treinamento.mobi.rest.RestClient;
import treinamento.mobi.utils.ImageLoader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CourseFragment extends Fragment {

	JSONObject course;
	View content;

	public static Fragment newInstance(JSONObject json) {

		Fragment f = new CourseFragment();
		Bundle args = new Bundle();
		args.putString("json", json.toString());
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			course = new JSONObject(getArguments().getString("json"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		content = inflater.inflate(R.layout.course_layout, null);

		ImageView img_logo = (ImageView) content.findViewById(R.id.logo);
		TextView txt_title = (TextView) content.findViewById(R.id.txt_title);
		TextView txt_description = (TextView) content
				.findViewById(R.id.txt_description);

		ImageLoader imgLoader = new ImageLoader(getActivity()
				.getApplicationContext());

		int loader = R.drawable.ic_launcher;
		String image_url = null;
		try {
			image_url = RestClient.URLMEDIA + course.getString("logo");

			txt_title.setText(course.getString("name"));

			txt_description.setText(course.getString("description"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		imgLoader.DisplayImage(image_url, loader, img_logo);

		return content;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
