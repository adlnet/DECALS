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

package com.eduworks.gwt.client.pagebuilder;

import com.eduworks.gwt.client.pagebuilder.screen.ScreenTemplate;

public class HistoryClosure {
	public ScreenTemplate screen;
	public String token;
	
	public HistoryClosure (ScreenTemplate st, String token) {
		this.screen = st;
		this.token = token;
	}
}