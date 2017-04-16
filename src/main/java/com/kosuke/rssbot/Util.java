package com.kosuke.rssbot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.kosuke.rssbot.model.T_Feed;
import com.kosuke.rssbot.model.UpdatedEntries;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public final class Util {
	// check update and return recent entries MAP
	// https://rometools.github.io/rome/RssAndAtOMUtilitiEsROMEV0.5AndAboveTutorialsAndArticles/FeedsDateElementsMappingToSyndFeedAndSyndEntry.html
	public static UpdatedEntries checkUpdates(T_Feed f, SyndFeed feed){
		UpdatedEntries ret = new UpdatedEntries();
		List<SyndEntry> entrylist = new ArrayList<SyndEntry>();
		Date lastmodified = null;
		
		for(SyndEntry entry : feed.getEntries()){
			// Can retrieve updatedDate (Atom 1.0)
			if(entry.getUpdatedDate() != null && entry.getUpdatedDate().getTime() > f.getLastmodified().getTime()){
				entrylist.add(entry);
				lastmodified = entry.getUpdatedDate();
				continue;

			// Can't retrieve updatedDate
			}else if(entry.getPublishedDate().getTime() > f.getLastmodified().getTime()){
				entrylist.add(entry);
				lastmodified = entry.getPublishedDate();
				continue;				
			}	
		}
		ret.setEntrylist(entrylist);
		ret.setLastmodified(lastmodified);
		return ret;
	}
}
