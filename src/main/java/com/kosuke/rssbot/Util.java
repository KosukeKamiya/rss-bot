package com.kosuke.rssbot;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.kosuke.rssbot.model.T_Feed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public final class Util {
	private static final Logger log = Logger.getLogger(Util.class.getName());

	// check update and return list of recent entries
	// https://rometools.github.io/rome/RssAndAtOMUtilitiEsROMEV0.5AndAboveTutorialsAndArticles/FeedsDateElementsMappingToSyndFeedAndSyndEntry.html
	public static ArrayList<SyndEntry> checkUpdates(T_Feed f, SyndFeed feed){
		ArrayList<SyndEntry> ret = new ArrayList<SyndEntry>();
		
		for(SyndEntry entry : feed.getEntries()){
			// Can retrieve updatedDate (Atom 1.0)
			if(entry.getUpdatedDate() != null && entry.getUpdatedDate().getTime() > f.getLastmodified().getTime()){
				ret.add(entry);
				continue;

			// Can't retrieve updatedDate
			}else if(entry.getPublishedDate().getTime() > f.getLastmodified().getTime()){
				ret.add(entry);
				continue;				
			}	
		}
		return ret;
	}
}
