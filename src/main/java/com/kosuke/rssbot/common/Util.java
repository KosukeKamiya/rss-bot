package com.kosuke.rssbot.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kosuke.rssbot.model.LINE_Message;
import com.kosuke.rssbot.model.LINE_Push;
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
	
	public static void sendUpdateNoticeByLine(String to, String FeedTitle, String entryTitle, String linkUrl, String accessToken)throws IOException{
		LINE_Message message = LINE_Message.createButtonsTemplateMessageObject(FeedTitle, entryTitle, linkUrl);
		LINE_Push push = new LINE_Push(to);
		push.messages.add(message);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		
		String body = gson.toJson(push, LINE_Push.class);
		
		Map<String, String> head = new HashMap<String, String>();

		head.put("Authorization", "Bearer " + accessToken);
		head.put("Content-Type", "application/json;charser=UTF-8");
		head.put("Content-length", Integer.toString(body.getBytes("UTF-8").length));

		APICall.APICallByPost("https://api.line.me/" + Constants.URL_PUSH, body, head);
	}
}
