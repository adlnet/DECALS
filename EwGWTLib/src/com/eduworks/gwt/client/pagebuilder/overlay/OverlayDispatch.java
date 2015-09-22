package com.eduworks.gwt.client.pagebuilder.overlay;

import com.eduworks.gwt.client.component.Constants;
import com.eduworks.gwt.client.pagebuilder.HistoryClosure;
import com.eduworks.gwt.client.pagebuilder.screen.ScreenDispatch;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

public class OverlayDispatch extends ScreenDispatch {
	private String prevScreen = null;
	
	public OverlayDispatch(){
		History.addValueChangeHandler(new ValueChangeHandler<String>() {	
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				if(historyToken.contains("Overlay")){
					OverlayAssembler.showOverlay();
				}else{
					if(!History.getToken().contains("Overlay"))
						OverlayAssembler.hideOverlay();
				}
			}
		});
	}
	
	protected void loadOverlay(OverlayTemplate st, boolean storeHistory){		
		
		if(history.size() > 0 && history.lastElement().token.contains("Screen"))
			prevScreen = history.lastElement().token;
		
		if (st!=null) {
			if (history.size()!=0 && storeHistory)
				history.lastElement().screen.lostFocus();
			if (history.size()==getHistoryLimit()) 
				history.remove(0);
			
			String token = st.getScreenName();
			if (storeHistory) {
				history.add(new HistoryClosure(st, token));
				History.newItem(token, false);
			}
			st.display();
		}
		Constants.setOverlayed(true);
		OverlayAssembler.showOverlay();
		
	}
	
	private static void loadHistoryOverlay(String token){
		for (int x=history.size()-1;x>-1;x--) {
			if (history.get(x).token.equals(token)) {
				if (history.get(x).screen!=null)
					history.get(x).screen.display();
				//history.setSize(x+1);
				History.newItem(token, false);
				return;
			}
		}
	}
	
	public void hideOverlay(){	
		if(prevScreen != null)
			loadHistoryScreen(prevScreen);
		Constants.setWaiting(false);
		Constants.setOverlayed(false);
		
		OverlayAssembler.hideOverlay();
	}
}
