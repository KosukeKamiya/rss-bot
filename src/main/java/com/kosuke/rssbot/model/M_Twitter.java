package com.kosuke.rssbot.model;

public class M_Twitter {
	private String apikey;
	private String apisecretkey;
	private String token;
	private String tokensecret;

	public M_Twitter(String apikey, String apisecretkey, String token, String tokensecret) {
		this.apikey = apikey;
		this.apisecretkey = apisecretkey;
		this.token = token;
		this.tokensecret=tokensecret;
	}
	
	public String getApiKey(){
		return this.apikey;
	}

	public String getApiSecretKey(){
		return this.apisecretkey;
	}

	public String getAccessToken(){
		return this.token;
	}

	public String getAccessTokenSecret(){
		return this.tokensecret;
	}
	
}
