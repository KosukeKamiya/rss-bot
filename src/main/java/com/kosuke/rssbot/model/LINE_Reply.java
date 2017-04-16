package com.kosuke.rssbot.model;

import java.util.ArrayList;

public class LINE_Reply {
	String replyToken;
	public ArrayList<LINE_Message> messages;

	public LINE_Reply() {
	}

	public LINE_Reply(final String replyToken) {
		this.replyToken = replyToken;
		this.messages = new ArrayList<LINE_Message>();
	}

}
