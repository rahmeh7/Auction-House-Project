package com.zensar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class EmailVerificationAndValidation {

	// Converting the Stream type to String type
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static boolean verifyEmailAddress(String emailId) {
		String accessKey = "aff1f018fe723e2e63219b9e4dd0d699";
		String emailVerificationURL = "https://apilayer.net/api/check?access_key=" + accessKey + "&email=" + emailId;

		try {
			// Getting the URL ready to execute
			@SuppressWarnings("resource")
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(emailVerificationURL);
			HttpResponse response;
			String result = null;

			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			InputStream instream = entity.getContent();
			result = convertStreamToString(instream);

			// Getting JSON Object from the response String
			JSONObject jsonObject = new JSONObject(result);
			return jsonObject.getBoolean("smtp_check");

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

}
