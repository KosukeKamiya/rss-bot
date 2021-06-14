package com.kosuke.rssbot.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kosuke.rssbot.common.DatastoreDAO;
import com.kosuke.rssbot.common.Util;
import com.kosuke.rssbot.model.M_Twitter;
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
		DatastoreDAO dao = new DatastoreDAO();
		// get all Feeds
		List<T_Feed> feedsList = dao.getAllFeeds();
		List<T_Feed> newfeedsList = new ArrayList<T_Feed>();

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
			UpdatedEntries updatesMap = f.checkUpdates(feed);
			List<SyndEntry> updatesList = updatesMap.getEntrylist();
			
			for(SyndEntry entry : updatesList){
				if(f.getTarget().equals("L")){ //target=Lの場合：更新があったらLINEで通知する
					String channelId = dao.getChannelIdByUserid(f.getUserId());
					String accessToken = dao.getChannelById(channelId).getToken();
					Util.sendUpdateNoticeByLine(f.getUserId(), feed.getTitle(), entry.getTitle(), entry.getLink() + "?openExternalBrowser=1", accessToken);

				}else if(f.getTarget().equals("T")){ //target=Tの場合：更新があったらTwitterにpostする
					// 要リファクタリング：useridのカラムにTwitterのaccesstokenが入っている
					String accessToken = f.getUserId();
					M_Twitter twitterProperty = dao.getTwitterPropertyByToken(accessToken);

					Util.sendUpdateTwieet(f.getUserId(), feed.getTitle(), entry.getTitle(), entry.getLink(), twitterProperty);
					
				}
			}
			
			if(updatesList.size() > 0){
				T_Feed newfeed = new T_Feed(f.getKey(), f.getUserId(), f.getUrl(), updatesMap.getLastmodified(), f.getTarget());
				newfeedsList.add(newfeed);
			}
		}

		//更新があった場合、データストアのlastmodifiedを更新する
		if(newfeedsList.size() >0){
			log.info("LOG attempt to update. Size: " + newfeedsList.size());
			dao.updateFeeds(newfeedsList);
		}

	}
}
