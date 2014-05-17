package treinamento.mobi.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class RestClient {

	private static RestClient INSTANCE = null;
	private static Context cont;
	private ProgressDialog dialog_login = null;

	public JSONObject obj;

	public int lastActivity = 0;
	public static String TAG = "TREINAMENTO";
	HttpURLConnection urlConnection = null;

	private static String URLBASE = "http://treinamentos.mobi/api/v1/";
	public static String URLMEDIA = "http://treinamentos.mobi";

	public RestClient(Context _cont) {
		cont = _cont;
	}

	public RestClient() {

	}

	public static RestClient getInstance() {

		return INSTANCE;
	}

	public static void LoadRestClient(Context _cont) {
		if (INSTANCE == null)
			INSTANCE = new RestClient(_cont);
		cont = _cont;

	}

	public interface HttpResponseCallback {
		void onSucess(JSONObject json);
		void onError(String erro);
	}

	public interface HttpResponseStringCallback {
		void onSucess(String json);
		void onError(String erro);
	}

	public void connectWithHttpPost(final String givenUsername,
			String givenPassword) {

		class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... params) {

				String paramUsername = params[0];
				String paramPassword = params[1];
				
				
				CookieManager cookieManager = new CookieManager();
				cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
				
				CookieHandler.setDefault(cookieManager);

				try {

					String urlParameters = "username=" + paramUsername
							+ "&password=" + paramPassword;

					urlConnection = (HttpURLConnection) new URL(
							"http://senddataurl/api/auth/")
							.openConnection();

					urlConnection
							.setRequestProperty("Connection", "Keep-Alive");
					urlConnection.setRequestMethod("POST");
					urlConnection.setDoOutput(true);
					urlConnection.setDoInput(true);
					urlConnection.setInstanceFollowRedirects(false);
					urlConnection.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					urlConnection.setRequestProperty("charset", "utf-8");
					urlConnection
							.setRequestProperty(
									"Content-Length",
									""
											+ Integer.toString(urlParameters
													.getBytes().length));
					urlConnection.setRequestProperty("Accept",
							"application/json");

					DataOutputStream wr = new DataOutputStream(
							urlConnection.getOutputStream());
					wr.writeBytes(urlParameters);
					wr.flush();
					wr.close();

					JSONObject userJson = convertJson(urlConnection
							.getInputStream());

					return userJson.toString();
				} catch (Exception e) {
					e.printStackTrace();

				}
				return "";

			}
			@Override
			protected void onProgressUpdate(Void... values) {
				super.onProgressUpdate(values);
			}
			
			@Override
			protected void onPreExecute() {

				super.onPreExecute();
				
				dialog_login = ProgressDialog.show(cont, "",
						"Carregando, aguarde...", true);

			}

			@Override
			protected void onPostExecute(String result) {
				dialog_login.dismiss();
				super.onPostExecute(result);

				if (!"".equals(result)) {
					//TRATAR RESPOSTA
					
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(cont);

					builder.setTitle("Treinamento");
					builder.setMessage("Usuario ou senha invalido(s)");
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {

								}
							});
					AlertDialog alerta = builder.create();
					alerta.show();

				}
			}
		}

		HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
		httpGetAsyncTask.execute(givenUsername, givenPassword);

	}

	
	public String convertToString(InputStream input) {
		BufferedReader streamReader;
		try {
			streamReader = new BufferedReader(new InputStreamReader(input,
					"UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
			return responseStrBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject convertJson(InputStream input) {
		BufferedReader streamReader;
		try {
			streamReader = new BufferedReader(new InputStreamReader(input,
					"UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);
			JSONObject json = null;
			try {
				json = new JSONObject(responseStrBuilder.toString());
			} catch (Exception e) {
				JSONObject js = new JSONObject();
				js.put("list", new JSONArray(responseStrBuilder.toString()));
				json = js;
				//e.printStackTrace();
			}
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public JSONObject Getjson(String url) {
		class HttpGetAsyncTask extends AsyncTask<String, Void, JSONObject> {

			@Override
			protected JSONObject doInBackground(String... params) {
				String param = params[0];
				try {
					urlConnection = (HttpURLConnection) new URL(param)
							.openConnection();
					urlConnection
							.setRequestProperty("Connection", "Keep-Alive");
					urlConnection.setRequestProperty("Accept",
							"application/json");
					obj = convertJson(urlConnection.getInputStream());

					return obj;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;

			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
			}
		}

		HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();

		httpGetAsyncTask.execute(url);
		try {
			return httpGetAsyncTask.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void getJson(String url, final HttpResponseCallback callback) {
		class HttpGetAsyncTask extends AsyncTask<String, Void, JSONObject> {

			@Override
			protected JSONObject doInBackground(String... params) {
				String param = params[0];
				try {

					urlConnection = (HttpURLConnection) new URL(param)
							.openConnection();
					urlConnection
							.setRequestProperty("Connection", "Keep-Alive");

					urlConnection.setRequestProperty("Accept",
							"application/json");

					int status = urlConnection.getResponseCode();

					if (status >= HttpStatus.SC_BAD_REQUEST)
						obj = convertJson(urlConnection.getErrorStream());
					else
						obj = convertJson(urlConnection.getInputStream());

					return obj;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;

			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (result != null)
					callback.onSucess(result);
				else
					callback.onError("Erro genérico");
			}
		}

		HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
		httpGetAsyncTask.execute(url);
	}

	public void getJsonString(String url,
			final HttpResponseStringCallback callback) {
		class HttpGetAsyncTask extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... params) {

				String param = params[0];
				try {
					urlConnection = (HttpURLConnection) new URL(param)
							.openConnection();
					urlConnection
							.setRequestProperty("Connection", "Keep-Alive");
					urlConnection.setRequestProperty("Accept",
							"application/json");
					return convertToString(urlConnection.getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;

			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null)
					callback.onSucess(result);
				else
					callback.onError("Erro genérico");
			}
		}

		HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
		httpGetAsyncTask.execute(url);
	}

	public static String getURLBASE() {
		return URLBASE;
	}

	public static void setURLBASE(String uRLBASE) {
		URLBASE = uRLBASE;
	}

}
