package treinamento.mobi.menu;

import treinamento.mobi.app.MainActivity;
import treinamento.mobi.app.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAppFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MenuAdapter adapter = new MenuAdapter(getActivity());

		adapter.add(new MenuItem("Treinamentos", R.drawable.cursos_off));
		adapter.add(new MenuItem("Turmas", R.drawable.turmas_off));
		adapter.add(new MenuItem("Professores", R.drawable.professores_off));
		adapter.add(new MenuItem("Sobre", R.drawable.sobre_off));

		setListAdapter(adapter);

		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, int position, long id) {

						String res = view.getTag().toString();
						
						if ("Treinamentos".equalsIgnoreCase(res)){
							((MainActivity)getActivity()).onCoursesSelected();
						} else if ("Turmas".equalsIgnoreCase(res)){
							((MainActivity)getActivity()).onTurmaSelected();
						} else if ("Professores".equalsIgnoreCase(res)){
							((MainActivity)getActivity()).onTeachersSelected();
						} else if ("Sobre".equalsIgnoreCase(res)){
							((MainActivity)getActivity()).onAboutSelected();
							
						}
					}
				});

	}

	private class MenuItem {
		public String tag;
		public int iconRes;

		public MenuItem(String tag, int iconRes) {
			this.tag = tag;
			this.iconRes = iconRes;
		}
	}

	public class MenuAdapter extends ArrayAdapter<MenuItem> {

		public MenuAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.row_menu, null);
			}
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.img_menu);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.txt_menu);
			title.setText(getItem(position).tag);
			convertView.setTag(getItem(position).tag);

			return convertView;
		}

	}
}