package com.kosuke.rssbot.model;

public class M_Channel {
	private String channel;
	private String secret;
	private String token;

	public M_Channel(String channel, String secret, String token) {
		this.channel = channel;
		this.secret = secret;
		this.token = token;
	}
	
	public String getChannelId(){
		return this.channel;
	}

	public String getSecret(){
		return this.secret;
	}

	public String getToken(){
		return this.token;
	}
	
}
