package com.kosuke.rssbot.model;

import java.util.ArrayList;
import java.util.List;

import com.kosuke.rssbot.common.Constants;

public class LINE_Message {
	String type;

	// Text
	String text;

	// Buttons template
	ButtonsTemplate template;
	String altText;

	// Image, Video
	String originalContentUrl;
	String previewImageUrl;

	public LINE_Message() {
	}

	public static LINE_Message createTextMessageObject(final String text) {
		LINE_Message message = new LINE_Message();
		message.type = Constants.TYPE_TEXT;
		message.text = text;
		return message;
	}

	public static LINE_Message createImageMessageObject(final String originalContentUrl, final String previewImageUrl) {
		LINE_Message message = new LINE_Message();
		message.type = Constants.TYPE_IMAGE;
		message.originalContentUrl = originalContentUrl;
		message.previewImageUrl = previewImageUrl;
		return message;
	}

	public static LINE_Message createButtonsTemplateMessageObject(final String imgUrl, final String title, final String text, final String entryUrl) {
		LINE_Message message = new LINE_Message();

		message.type = "template";
		message.template = message.new ButtonsTemplate(imgUrl, title, text, entryUrl);
		message.altText =entryUrl;

		return message;
	}

	public static LINE_Message createButtonsTemplateMessageObject(final String title, final String text, final String entryUrl) {
		LINE_Message message = new LINE_Message();

		message.type = "template";
		message.template = message.new ButtonsTemplate(null, title, text, entryUrl);
		message.altText =text;

		return message;
	}

	class ButtonsTemplate{
		String type;
		String thumbnailImageUrl;
		String title;
		String text;
		List<TemplateAction> actions;
		
		ButtonsTemplate(){
			type = "buttons";
			actions = new ArrayList<TemplateAction>();
		}
		
		ButtonsTemplate(final String imgUrl, final String title, final String text, final String entryUrl){
			this();
			this.thumbnailImageUrl = imgUrl;
			this.title = title;
			this.text = text;
			
			TemplateAction act = new TemplateAction("uri", "見る", entryUrl);

			this.actions.add(act);
		}
	}
	

	class TemplateAction {
		String type;
		String label;
		String uri;

		TemplateAction(final String type, final String label, final String uri) {
			this.type=type;
			this.label=label;
			this.uri=uri;
		}
		TemplateAction(){
			
		}
	}
}
