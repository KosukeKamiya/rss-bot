package com.kosuke.rssbot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.kosuke.rssbot.model.T_Feed;
import com.kosuke.rssbot.model.UpdatedEntries;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public final class Util {
	// check update and return recent entries MAP
	// https://rometools.github.io/rome/RssAndAtOMUtilitiEsROMEV0.5AndAboveTutorialsAndArticles/FeedsDateElementsMappingToSyndFeedAndSyndEntry.html
	public static UpdatedEntries checkUpdates(T_Feed f, SyndFeed feed){
		List<SyndEntry> entrylist = new ArrayList<SyndEntry>();
		Date lastmodified = f.getLastmodified();
		
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

			if(entryUpdateDate.getTime() > f.getLastmodified().getTime()){
				entrylist.add(entry);
				
				// lastmodified update to latest entryUpdateDate
				if(entryUpdateDate.getTime() > lastmodified.getTime()){
					lastmodified = entryUpdateDate;
				}
			}
		}
		return (new UpdatedEntries(entrylist, lastmodified));
	}
}
