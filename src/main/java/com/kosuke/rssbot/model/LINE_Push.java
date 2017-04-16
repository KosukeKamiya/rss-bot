package com.kosuke.rssbot.model;

import java.util.ArrayList;

public class LINE_Push {
	String to;
	public ArrayList<LINE_Message> messages;

	public LINE_Push() {
	}

	public LINE_Push(final String to) {
		this.to = to;
		this.messages = new ArrayList<LINE_Message>();
	}

}
