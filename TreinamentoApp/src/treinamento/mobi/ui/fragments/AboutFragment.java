package treinamento.mobi.ui.fragments;

import treinamento.mobi.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {

	View content;

	private void oncreate() {
		// TODO Auto-generated method stub

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		content = inflater.inflate(R.layout.layout_about, null);

		TextView txt_description = (TextView) content
				.findViewById(R.id.txt_description);

		String txt = "Depois de termos passado pela experi�ncia de treinar centenas de pessoas, percebemos que havia uma lacuna muito grande na �rea de mobilidade. Unimos isso ao fato de sermos profissionais focados em desenvolvimento para dispositivos m�veis e ao desejo de criar algo novo, com uma qualidade acima do que existe no mercado. Baseado nisso, decidmos criar uma nova central de treinamentos, focada na �rea de dispositivos m�veis. Alguns dos nossos diferenciais:";
		txt += "\n";
		txt += "\n - Professores com grande experi�ncia na �rea ";
		txt += "\n - Ambiente inovador e descontra�do";
		txt += "\n - Profissionais que criam e publicam apps";
		txt += "\n - Treinamentos focados na necessidade do mercado";
		
		txt_description.setText(txt);

		return content;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
