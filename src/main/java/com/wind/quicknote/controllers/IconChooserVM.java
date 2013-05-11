package com.wind.quicknote.controllers;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

import com.wind.quicknote.views.tree.TopicItem;

/**
 * @author Luke
 * 
 * http://forum.zkoss.org/question/76567/mvvm-and-mvc-mix-communication-createcomponents/
 * http://stackoverflow.com/questions/14704311/how-to-close-a-window-in-zk
 * 
 */

public class IconChooserVM {

	private Image image = null;
	private TopicItem topic = null;
	
	private int indexInit = 0;
	public int getIndexInit() {
		return indexInit;
	}
	public void setIndexInit(int indexInit) {
		this.indexInit = indexInit;
	}
	
	private int indexAssigned = 0;
	public int getIndexAssigned() {
		return indexAssigned;
	}
	public void setIndexAssigned(int indexAssigned) {
		this.indexAssigned = indexAssigned;
	}

	//@Init
	@AfterCompose
	public void init(@ExecutionArgParam("topicItem") TopicItem topicItem, @ExecutionArgParam("image") Image imageObj) {
		
		String numStr = topicItem.getIcon().replace("/assets/images/filetypes/ft", "").replace(".gif", "");
		int num = Integer.valueOf(numStr);
		topic = topicItem;
		image = imageObj;
		indexInit = num - 1;
		indexAssigned = num - 1;
	}
	
	@Command
    public void confirm(@BindingParam("cmp")  Window win) {
		
		String value = String.valueOf((indexAssigned + 1));
    	String newImagesrc = "/assets/images/filetypes/ft" + value + ".gif";
    	
    	// compare values
    	if(! topic.getIcon().equals(newImagesrc)){
    		
    		// update imageSrc
        	image.setSrc(newImagesrc);
        	// image.invalidate();
        	topic.setIcon(newImagesrc);
        	
        	Map<String, Object> outParams = new HashMap<String, Object>();
        	outParams.put("topicItem", topic);
    		BindUtils.postGlobalCommand(null, null, "updateTopicIconCommand", outParams);
    	}
    	
		win.detach();
		
    }
	
}


