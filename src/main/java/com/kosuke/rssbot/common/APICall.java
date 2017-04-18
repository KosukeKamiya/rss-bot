package com.kosuke.rssbot.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class APICall {
		public static String APICallByPost(String Url, String requestBodyStr, Map<String, String> propMap)
			throws IOException {

		URL url = new URL(Url);

		HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		uc.setRequestMethod("POST");
		uc.setDoOutput(true);

		for (Map.Entry<String, String> prop : propMap.entrySet()) {
			uc.setRequestProperty(prop.getKey(), prop.getValue());
		}

		// Post data
		PrintStream ps = new PrintStream(uc.getOutputStream(), false, "utf-8");
		ps.print(requestBodyStr);
		ps.close();

		// Get result
		String temp = null;
		StringBuffer resultSb = new StringBuffer();
		BufferedReader reader;
		try {
			InputStream is = uc.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));

			while ((temp = reader.readLine()) != null) {
				resultSb.append(temp);
			}
			return resultSb.toString();

		} catch (Exception e) {
			InputStream err = uc.getErrorStream();
			reader = new BufferedReader(new InputStreamReader(err));

			while ((temp = reader.readLine()) != null) {
				resultSb.append(temp);
			}
			return resultSb.toString();
		}

	}

	public static String APICallByGet(String Url, Map<String, String> propMap)
			throws IOException {
		URL url = new URL(Url);

		HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		uc.setRequestMethod("GET");

		for (Map.Entry<String, String> prop : propMap.entrySet()) {
			uc.setRequestProperty(prop.getKey(), prop.getValue());
		}

		uc.connect();

		String temp = null;
		StringBuffer resultSb = new StringBuffer();

		BufferedReader reader;

		try {
			InputStream is = uc.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));

			while ((temp = reader.readLine()) != null) {
				resultSb.append(temp);
			}

		} catch (Exception e) {
			InputStream err = uc.getErrorStream();
			reader = new BufferedReader(new InputStreamReader(err));

			while ((temp = reader.readLine()) != null) {
				resultSb.append(temp);
			}
		}
		return resultSb.toString();
	}
}
