package com.kosuke.rssbot.model;

import java.util.Date;

import com.google.appengine.api.datastore.Key;

public class T_Feed {
	private Key key;
	private String userId;
	private String url;
	private Date lastmodified;

	public T_Feed(Key key, String userId, String url, Date lastmodified) {
		this.key = key;
		this.userId = userId;
		this.url = url;
		this.lastmodified = lastmodified;
	}
	
	public Key getKey(){
		return this.key;
	}

	public String getUserId(){
		return this.userId;
	}

	public String getUrl(){
		return this.url;
	}

	public Date getLastmodified(){
		return this.lastmodified;
	}
	
}
