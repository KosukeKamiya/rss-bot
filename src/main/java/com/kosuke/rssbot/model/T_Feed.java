package com.kosuke.rssbot.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class T_Feed {
	private Key key;
	private String userId;
	private String url;
	private Date lastmodified;
	private String target;

	public T_Feed(Key key, String userId, String url, Date lastmodified, String target) {
		this.key = key;
		this.userId = userId;
		this.url = url;
		this.lastmodified = lastmodified;
		this.target = target;
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
	
	public String getTarget(){
		return this.target;
	}
	
	public UpdatedEntries checkUpdates(SyndFeed feed){
		List<SyndEntry> entrylist = new ArrayList<SyndEntry>();
		Date lastmodified = this.getLastmodified();
		
		for(SyndEntry entry : feed.getEntries()){
			Date entryUpdateDate;

			// Can retrieve updatedDate (Atom 1.0)
			if(entry.getUpdatedDate() != null) {
				entryUpdateDate = entry.getUpdatedDate();
			}
			// Can't retrieve updatedDate
			else {
				entryUpdateDate = entry.getPublishedDate();
			}

			if(entryUpdateDate.getTime() > this.getLastmodified().getTime()){
				entrylist.add(entry);
				
				// lastmodified update to latest entryUpdateDate
				if(entryUpdateDate.getTime() > lastmodified.getTime()){
					lastmodified = entryUpdateDate;
				}
			}else{
				break;
			}
		}
		return (new UpdatedEntries(entrylist, lastmodified));
	}
	
}
