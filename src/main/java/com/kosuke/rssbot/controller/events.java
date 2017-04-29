package com.kosuke.rssbot.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kosuke.rssbot.common.APICall;
import com.kosuke.rssbot.common.Constants;
import com.kosuke.rssbot.common.DatastoreDAO;
import com.kosuke.rssbot.model.LINE_Message;
import com.kosuke.rssbot.model.LINE_Reply;
import com.kosuke.rssbot.model.LINE_Webhook;

@SuppressWarnings("serial")
public class events extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(events.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// account check
		String account = req.getPathInfo().substring(1);
		log.warning("LOG account: " + account);

		// Retrieve Request body
		String httpRequestBody = getBody(req);
		log.warning("LOG webhook X-Line-Signature: " + req.getHeader("X-Line-Signature"));
		log.warning("LOG webhook RequestBody: " + httpRequestBody);

		// Verify Signature
		Boolean verify = verifiSignature(req, httpRequestBody, account);
		if (!verify) {
			log.warning("LOG: Verify Signature Fail");
			resp.sendError(400);
		}

		//Gson gson = new Gson();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		LINE_Webhook webhook = gson.fromJson(httpRequestBody, LINE_Webhook.class);
		DatastoreDAO dao = new DatastoreDAO();
		
		for (LINE_Webhook.Event event : webhook.events) {
			// Switch by event type.
			switch (event.type) {
				case Constants.TYPE_MESSAGE:
					//log.warning("LOG: MESSAGE");

					LINE_Message message;
					LINE_Reply reply = new LINE_Reply(event.replyToken);
					Map<String, String> head = new HashMap<String, String>();

					// Switch by message type
					switch (event.message.type) {
						case Constants.TYPE_TEXT:
							//log.warning("LOG: MESSAGE_TEXT");

							String recievedMessage = event.message.text;
							switch (recievedMessage) {
								default:
									message = LINE_Message.createTextMessageObject(recievedMessage);
							}

							reply.messages.add(message);

							String body = gson.toJson(reply, LINE_Reply.class);

							head.put("Authorization", "Bearer " + dao.getChannelById(account).getToken());
							head.put("Content-Type", "application/json;charser=UTF-8");
							head.put("Content-length", Integer.toString(body.getBytes("UTF-8").length));

							log.warning("LOG RequestHeader:" + head.toString());
							log.warning("LOG RequestBody:" + body);

							String result = APICall.APICallByPost("https://api.line.me/"
								+ Constants.URL_REPLY, body, head);

							log.warning("LOG: result:" + result);

							break;

						default:
							log.warning("LOG: MESSAGE_OTHER: " + event.message.type);
							break;
					}
					break;

				case Constants.TYPE_POSTBACK:
					log.warning("LOG: POSTBACK");
					break;

				case Constants.TYPE_FOLLOW:
					log.warning("LOG: FOLLOW");
					break;

				case Constants.TYPE_UNFOLLLOW:
					log.warning("LOG: UNFOLLOW");
					break;

			}
		}

	}

	private String getBody(final HttpServletRequest req) {
		Logger log = Logger.getLogger(events.class.getName());
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

	private Boolean verifiSignature(HttpServletRequest req, String httpRequestBody, String account) {
		Logger log = Logger.getLogger(events.class.getName());
		DatastoreDAO dao = new DatastoreDAO();
		
		String channelSecret = dao.getChannelById(account).getSecret();
		try {
			SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);

			byte[] source = httpRequestBody.getBytes("UTF-8");
			String signature = Base64.encodeBase64String(mac.doFinal(source));
			log.warning("LOG signature_from_body: " + signature);
			log.warning("LOG X-Line-Signature:" + req.getHeader("X-Line-Signature"));

			return signature.equals(req.getHeader("X-Line-Signature"));

		} catch (Exception e) {
			log.warning("LOG error: " + e.toString());

			StackTraceElement[] stElem = e.getStackTrace();
			for (int i = 0; i < stElem.length; i++) {
				log.warning(stElem[i].getClassName() + ": "
					+ stElem[i].getMethodName() + " "
					+ stElem[i].getLineNumber());
			}
		}
		return false;

	}

}
