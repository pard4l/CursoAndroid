package treinamento.mobi.ui.fragments;

import treinamento.mobi.app.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class ScheduleFragment extends Fragment {

	String url;
	View content;

	ProgressDialog progress;

	WebChromeClient wbChrome;

	public static Fragment newInstance(String url) {

		Fragment f = new ScheduleFragment();
		Bundle args = new Bundle();
		args.putString("url", url);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			url = getArguments().getString("url");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		content = inflater.inflate(R.layout.schedule_layout, null);

		wbChrome = new WebChromeClient();

		WebView wb = (WebView) content.findViewById(R.id.webview);
		wb.setWebChromeClient(wbChrome);
		wb.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view,
					String urlNewString) {
				view.loadUrl(urlNewString);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap facIcon) {
				// SHOW LOADING IF IT ISNT ALREADY VISIBLE
				progress = new ProgressDialog(getActivity());
				progress.setTitle("Treinamento");
				progress.setCancelable(false);
				progress.setMessage("Carregando .. ");
				progress.show();

			}

			@Override
			public void onPageFinished(WebView view, String url) {

				progress.dismiss();
			}
		});
		wb.getSettings().setJavaScriptEnabled(true);
		wb.loadUrl(url);

		return content;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
