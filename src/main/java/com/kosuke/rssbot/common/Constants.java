package com.kosuke.rssbot.common;

public class Constants {

	public static final String URL_PUSH = "v2/bot/message/push";
	public static final String URL_REPLY = "v2/bot/message/reply";
	public static final String URL_GETCONTENT = "v2/bot/message/%s/content";
	public static final String URL_GETPROFILE = "v2/bot/profile?userId=%d";
	public static final String URL_LEAVE_GROUP = "v2/bot/group/%d/leave";
	public static final String URL_LEAVE_ROOM = "v2/bot/room/%d/leave";

	public static final String TYPE_USER = "user";
	public static final String TYPE_GROUP = "group";
	public static final String TYPE_ROOM = "room";

	public static final String TYPE_MESSAGE = "message";
	public static final String TYPE_TEXT = "text";
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_VIDEO = "video";
	public static final String TYPE_AUDIO = "audio";
	public static final String TYPE_LOCATION = "location";
	public static final String TYPE_STICKER = "sticker";
	public static final String TYPE_TEMPLATE = "template";

	public static final String TYPE_URI = "uri";

	public static final String TYPE_FOLLOW = "follow";
	public static final String TYPE_UNFOLLLOW = "unfollow";
	public static final String TYPE_JOIN = "join";
	public static final String TYPE_LEAVE = "leave";

	public static final String TYPE_POSTBACK = "postback";

	public Constants() {
		// TODO Auto-generated constructor stub
	}

}
