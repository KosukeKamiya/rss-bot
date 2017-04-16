package com.kosuke.rssbot.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kosuke.rssbot.APICall;
import com.kosuke.rssbot.Constants;
import com.kosuke.rssbot.DAO;
import com.kosuke.rssbot.Util;
import com.kosuke.rssbot.model.LINE_Message;
import com.kosuke.rssbot.model.LINE_Push;
import com.kosuke.rssbot.model.T_Feed;
import com.kosuke.rssbot.model.UpdatedEntries;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@SuppressWarnings("serial")
public class UpdateCheck extends HttpServlet {

	private static final Logger log = Logger.getLogger(UpdateCheck.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		log.info("LOG UpdateCheck start");
		
		// get all Feeds
		ArrayList<T_Feed> feedsList = DAO.getAllFeeds();
		List<Entity> newfeedsList = new ArrayList<Entity>();
		
		for(T_Feed f : feedsList){
			// purse rss
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = null;
			try {
				feed = input.build(new XmlReader(new URL(f.getUrl())));
			} catch (Exception e) {
				e.printStackTrace();
				log.info("LOG Error :"+ e.toString());
				log.info("LOG Error :"+ f.getUrl());
			}
			
			//更新を確認する
			UpdatedEntries updatesMap = Util.checkUpdates(f, feed);
			List<SyndEntry> updatesList = updatesMap.getEntrylist();

			for(SyndEntry entry : updatesList){
				//更新があった場合、LINEで通知する（TODO: 別Methodに切り出す）
				LINE_Message message = LINE_Message.createButtonsTemplateMessageObject(feed.getTitle(), entry.getTitle(), entry.getLink());
				LINE_Push push = new LINE_Push(f.getUserId());
				push.messages.add(message);
				Gson gson = new GsonBuilder().disableHtmlEscaping().create();
				
				String body = gson.toJson(push, LINE_Push.class);
				log.info("LOG body: "+body);
				
				Map<String, String> head = new HashMap<String, String>();
				String channelId = DAO.getChannelIdByUserid(f.getUserId());
				head.put("Authorization", "Bearer " + DAO.getChannelById(channelId).getToken());
				head.put("Content-Type", "application/json;charser=UTF-8");
				head.put("Content-length", Integer.toString(body.getBytes("UTF-8").length));

				log.warning("LOG RequestHeader:" + head.toString());
				log.warning("LOG RequestBody:" + body);

				String result = APICall.APICallByPost("https://api.line.me/"
					+ Constants.URL_PUSH, body, head);

				log.warning("LOG: result:" + result);
			}
			
			if(updatesList.size() > 0){
				Entity e = new Entity(f.getKey());
				e.setProperty("lastmodified", updatesMap.getLastmodified());
				e.setProperty("userId", f.getUserId());
				e.setProperty("URL", f.getUrl());
				newfeedsList.add(e);
			}
		}

		//更新があった場合、データストアのlastmodifiedを更新する
		if(newfeedsList.size() >0){
			log.info("LOG attempt to update. Size: " + newfeedsList.size());
			DAO.updateFeeds(newfeedsList);
		}

	}
}
