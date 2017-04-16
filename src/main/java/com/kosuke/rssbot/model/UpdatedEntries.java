package com.kosuke.rssbot.model;

import java.util.Date;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;

public class UpdatedEntries {
	private Date lastmodified;
	private List<SyndEntry> entrylist;

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
