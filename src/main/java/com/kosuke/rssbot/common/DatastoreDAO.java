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


public final class DAO {
	private static final Logger log = Logger.getLogger(DAO.class.getName());
	
	// Get all channels
	public static Map<String, M_Channel> getAllChannel(){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
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
	public static M_Channel getChannelById(String channel){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
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
	public static String getChannelIdByUserid(String userId){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
		Query query_token = new Query("M_User").setFilter(filter);
		PreparedQuery preparedQuery_channel = datastoreService.prepare(query_token);
		
		for (Entity entity : preparedQuery_channel.asIterable()) {
			String channel  = (String)entity.getProperty("channel");
			return channel;
		}
		return "";
	}

	// get all Feeds
	public static ArrayList<T_Feed> getAllFeeds(){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query_token = new Query("T_Feed");
		PreparedQuery preparedQuery_channel = datastoreService.prepare(query_token);
		
		T_Feed f = null;
		ArrayList<T_Feed> ret = new ArrayList<T_Feed>();
		for (Entity entity : preparedQuery_channel.asIterable()) {
			Key    key          = entity.getKey();
			String userId       = (String)entity.getProperty("userId");
			String url          = (String)entity.getProperty("URL");
			Date   lastmodified = (Date)entity.getProperty("lastmodified");

			f = new T_Feed(key, userId, url, lastmodified);
			ret.add(f);
		}
		return ret;
	}

	// get Feeds by userId
	public static ArrayList<T_Feed> getFeedsBuUserId(String userId){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
		Query query_token = new Query("T_Feed").setFilter(filter);
		PreparedQuery preparedQuery_channel = datastoreService.prepare(query_token);
		
		T_Feed f = null;
		ArrayList<T_Feed> ret = new ArrayList<T_Feed>();
		for (Entity entity : preparedQuery_channel.asIterable()) {
			Key    key          = entity.getKey();
			String url          = (String)entity.getProperty("url");
			Date   lastmodified = (Date)entity.getProperty("lastmodified");

			log.info("LOG feed get result: " + entity.getKind() + " - " + entity.getProperty("url"));
			f = new T_Feed(key, userId, url, lastmodified);
			ret.add(f);
		}
		return ret;
	}
	
	// update Feeds
	public static void updateDatastore(List<Entity> feedList){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		datastoreService.put(feedList);
	}

	public static void updateFeeds(List<T_Feed> updatesList){
		List<Entity> newfeedsList = new ArrayList<Entity>();
		
		for(T_Feed feed: updatesList){
			Entity e = new Entity(feed.getKey());
			e.setProperty("lastmodified", feed.getLastmodified());
			e.setProperty("userId", feed.getUserId());
			e.setProperty("URL", feed.getUrl());
			newfeedsList.add(e);
		}
		updateDatastore(newfeedsList);
	}
}
