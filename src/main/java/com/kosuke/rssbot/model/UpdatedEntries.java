package com.kosuke.rssbot.model;

import java.util.Date;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;

public class UpdatedEntries {
	private List<SyndEntry> entrylist;
	private Date lastmodified;
	
	public UpdatedEntries(List<SyndEntry> entrylist, Date lastmodified){
		this.entrylist = entrylist;
		this.lastmodified = lastmodified;
	}

	public Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}

	public List<SyndEntry> getEntrylist() {
		return entrylist;
	}

	public void setEntrylist(List<SyndEntry> entrylist) {
		this.entrylist = entrylist;
	}
}
