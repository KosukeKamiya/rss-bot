package com.kosuke.rssbot.model;

import java.util.List;

public class LINE_Webhook {
	public List<Event> events;

	public LINE_Webhook() {
	}

	public static class Event {
		public String type;
		public String replyToken;
		Number timestamp;
		Source source;
		public Message message;
		Postback postback;

		public Event() {
		}

		public static class Source {
			String type;
			String userId;

			public Source() {
			}
		}

		public static class Message {
			// common
			String id;
			public String type;

			// text message
			public String text;

			// location
			String title;
			String address;
			String latitude;
			String longitude;

			// sticker
			String packageId;
			String stickerId;

			public Message() {
			}
		}

		public static class Postback {
			String data;

			public Postback() {
			}
		}

	}

}
