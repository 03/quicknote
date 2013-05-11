package com.wind.quicknote.controllers;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

/**
 * @author Luke
 * 
 */
public class IconChooserCtrl extends GenericForwardComposer<Window> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7343552701911219830L;

	@Wire
	private Radiogroup svrgroup;
	
	@Wire
	private Button confirmBtn;
	
	Window window;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Window win) throws Exception {
		super.doAfterCompose(win);
		
		window = win;
		Map<String, String> myParams = new HashMap<String, String>();
		myParams = (Map<String, String>) Executions.getCurrent().getArg();
		String currentImageSrc = myParams.get("currentImageSrc");
		String numStr = currentImageSrc.replace("/assets/images/filetypes/ft", "").replace(".gif", "");
		int num = Integer.valueOf(numStr);
		svrgroup.setSelectedIndex(num-1);
		
		confirmBtn.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {

            	String value = svrgroup.getSelectedItem().getValue();
            	Map<String, Object> outParams = new HashMap<String, Object>();
            	outParams.put("newImageSrc", "/assets/images/filetypes/ft" + value + ".gif");
        		
        		BindUtils.postGlobalCommand(null, null, "changeTopicIconCommand", outParams);
        		
        		window.detach();
            }
        });
		
	}
	
	
}


