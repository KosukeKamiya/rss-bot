package com.kosuke.rssbot.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Key;
import com.kosuke.rssbot.model.*;


public class DatastoreDAO {
	private final Logger log;
	private final DatastoreService datastoreService;
	
	public DatastoreDAO() {
		log = Logger.getLogger(this.getClass().getName());
		datastoreService = DatastoreServiceFactory.getDatastoreService();
	}
	
	// Get all channels
	public Map<String, M_Channel> getAllChannel(){
		Query query_channel = new Query("M_Channel");
		PreparedQuery preparedQuery_channel = datastoreService.prepare(query_channel);

		Map<String, M_Channel> channelMap = new HashMap<String, M_Channel>();		
		for (Entity entity : preparedQuery_channel.asIterable()) {
			String channel = (String)entity.getProperty("channel");
			String secret  = (String)entity.getProperty("secret");
			String token   = (String)entity.getProperty("token");
			
			log.info("LOG channel get result: " + entity.getKind() + " - " + channel);
			M_Channel c = new M_Channel(channel, secret, token);

			channelMap.put(channel, c);
		}
		return channelMap;		
	}
	
	// Get 1 channel by ChannelId
	public M_Channel getChannelById(String channel){
		Filter filter = new FilterPredicate("channel", FilterOperator.EQUAL, channel);
		Query query_token = new Query("M_Channel").setFilter(filter);
		PreparedQuery preparedQuery_channel = datastoreService.prepare(query_token);
		
		M_Channel c = null;
		for (Entity entity : preparedQuery_channel.asIterable()) {
			String secret  = (String)entity.getProperty("secret");
			String token   = (String)entity.getProperty("token");
			
			c = new M_Channel(channel, secret, token);
			break;
		}
		return c;
	}

	// Get 1 channelId by userId
	public String getChannelIdByUserid(String userId){
		Filter filter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
		Query query_token = new Query("M_User").setFilter(filter);
		PreparedQuery preparedQuery_channel = datastoreService.prepare(query_token);
		
		for (Entity entity : preparedQuery_channel.asIterable()) {
			String channel  = (String)entity.getProperty("channel");
			return channel;
		}
		return "";
	}
	
	// get Twitter property by accessToken
	public M_Twitter getTwitterPropertyByToken(String token){
		Filter filter = new FilterPredicate("token", FilterOperator.EQUAL, token);
		Query query_token = new Query("M_Twitter").setFilter(filter);
		PreparedQuery preparedQuery_twitter = datastoreService.prepare(query_token);
		
		M_Twitter t = null;
		for (Entity entity : preparedQuery_twitter.asIterable()) {
			String apikey         = (String)entity.getProperty("apikey");
			String apisecretkey   = (String)entity.getProperty("apisecretkey");
			String tokensecret    = (String)entity.getProperty("tokensecret");
			
			t = new M_Twitter(apikey, apisecretkey, token, tokensecret);
			break;
		}
		return t;
	}
	
	
	// get all Feeds
	public ArrayList<T_Feed> getAllFeeds(){
		Query query_token = new Query("T_Feed");
		PreparedQuery preparedQuery_feed = datastoreService.prepare(query_token);
		
		T_Feed f = null;
		ArrayList<T_Feed> ret = new ArrayList<T_Feed>();
		for (Entity entity : preparedQuery_feed.asIterable()) {
			Key    key          = entity.getKey();
			String userId       = (String)entity.getProperty("userId");
			String url          = (String)entity.getProperty("URL");
			Date   lastmodified = (Date)entity.getProperty("lastmodified");
			String target       = (String)entity.getProperty("target");

			f = new T_Feed(key, userId, url, lastmodified, target);
			ret.add(f);
		}
		return ret;
	}

	// get Feeds by userId
	public ArrayList<T_Feed> getFeedsBuUserId(String userId){
		Filter filter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
		Query query_token = new Query("T_Feed").setFilter(filter);
		PreparedQuery preparedQuery_feed = datastoreService.prepare(query_token);
		
		T_Feed f = null;
		ArrayList<T_Feed> ret = new ArrayList<T_Feed>();
		for (Entity entity : preparedQuery_feed.asIterable()) {
			Key    key          = entity.getKey();
			String url          = (String)entity.getProperty("url");
			Date   lastmodified = (Date)entity.getProperty("lastmodified");
			String target       = (String)entity.getProperty("target");

			log.info("LOG feed get result: " + entity.getKind() + " - " + entity.getProperty("url"));
			f = new T_Feed(key, userId, url, lastmodified, target);
			ret.add(f);
		}
		return ret;
	}
	
	// update Feeds
	public void updateDatastore(List<Entity> feedList){
		datastoreService.put(feedList);
	}

	public void updateFeeds(List<T_Feed> updatesList){
		List<Entity> newfeedsList = new ArrayList<Entity>();
		
		for(T_Feed feed: updatesList){
			Entity e = new Entity(feed.getKey());
			e.setProperty("lastmodified", feed.getLastmodified());
			e.setProperty("userId", feed.getUserId());
			e.setProperty("URL", feed.getUrl());
			e.setProperty("target", feed.getTarget());
			newfeedsList.add(e);
		}
		updateDatastore(newfeedsList);
	}
}
