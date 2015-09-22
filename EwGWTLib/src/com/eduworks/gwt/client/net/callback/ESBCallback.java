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

package com.eduworks.gwt.client.net.callback;

import org.vectomatic.arrays.ArrayBuffer;

import com.eduworks.gwt.client.net.packet.AjaxPacket;
import com.eduworks.gwt.client.net.packet.ESBPacket;

public abstract class ESBCallback<Packet> extends AjaxCallback<Packet>
{	
	public abstract void onFailure(Throwable caught);

	public abstract void onSuccess(final ESBPacket esbPacket);
	
	public void onSuccess(String result)
	{
		try {
			onSuccess(new ESBPacket(AjaxPacket.parseJSON(result.trim())));
		} catch (Exception e) {
			onSuccess(new ESBPacket("text/plain", result));
		}
	}

	public void onFileSuccess(String mimeType, Object result)
	{
		if (mimeType==null)
			mimeType = "text/plain";
		onSuccess(new ESBPacket(mimeType, (ArrayBuffer) result));
	}
}