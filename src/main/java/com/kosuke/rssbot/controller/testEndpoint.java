package com.kosuke.rssbot.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class testEndpoint extends HttpServlet {

	private static final Logger log = Logger.getLogger(testEndpoint.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.warning("get");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// Retrieve Request body
		String httpRequestBody = getBody(req);
		log.warning("LOG body: " + httpRequestBody);
	}

	private String getBody(final HttpServletRequest req) {
		String ret = "";
		BufferedReader reader;
		StringBuffer jsonSb = new StringBuffer();

		try {
			reader = req.getReader();
			jsonSb = new StringBuffer();
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				jsonSb.append(temp);
			}
			ret = jsonSb.toString();

		} catch (IOException e) {
			log.warning("LOG error: " + e.toString());

			StackTraceElement[] stElem = e.getStackTrace();
			for (int i = 0; i < stElem.length; i++) {
				log.warning(stElem[i].getClassName() + ": "
					+ stElem[i].getMethodName() + " "
					+ stElem[i].getLineNumber());
			}
		}

		return ret;
	}
}
