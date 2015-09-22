/*
Copyright 2012-2013 Eduworks Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.eduworks.gwt.client.pagebuilder.screen;

import java.util.Vector;

import com.eduworks.gwt.client.pagebuilder.HistoryClosure;
import com.google.gwt.user.client.History;
 
public class ScreenDispatch {
	public static Vector<HistoryClosure> history = new Vector<HistoryClosure>();
	private final int historyLimit = 30;
	private ScreenTemplate defaultScreen;
	
	public void loadScreen(ScreenTemplate st, boolean storeHistory) {
		if (st!=null) {
			if (history.size()!=0&&storeHistory)
				history.lastElement().screen.lostFocus();
			if (history.size()==getHistoryLimit()) 
				history.remove(0);
			
			String token = st.getScreenName();
			if (history.size()>0&&token==history.lastElement().screen.getScreenName())
				history.lastElement().screen = st;
			else if (storeHistory) {
				history.add(new HistoryClosure(st, token));
				History.newItem(token, false);
			}
			st.display();
		}
	}
	
	public void setDefaultScreen(ScreenTemplate screenDefault) {
		defaultScreen = screenDefault;
	}
	
	public void loadHistoryScreen(String token) {
		for (int x=history.size()-1;x>-1;x--) {
			if (history.get(x).token.equals(token)) {
				if (history.get(x).screen!=null)
					history.get(x).screen.display();
				//history.setSize(x+1);
				History.newItem(token, false);
				return;
			}
		}
		if (token.equals("Login") || history.size()==0) { 
			clearHistory();
			loadScreen(defaultScreen, true);
		} else if ((token=="#") || (token.equalsIgnoreCase(history.lastElement().token)))
			History.back();
	}
	
	public void clearHistory() {
		history.clear();
		String initToken = History.getToken();
		if (initToken.length()==0)
			History.newItem("Login");
	}

	public int getHistoryLimit() {
		return historyLimit;
	}
}