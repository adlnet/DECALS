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
package com.eduworks.gwt.client.util;

import org.vectomatic.arrays.ArrayBuffer;

import com.google.gwt.core.client.JsArrayInteger;

public class Uint8Array {
  public static final native JsArrayInteger createUint8Array(ArrayBuffer buffer) /*-{
    return new Uint8Array(buffer);
  }-*/;

  public final native int getLength() /*-{
    return this.length;
  }-*/;
  
  public final native byte get(int index) /*-{
    return this[index];
  }-*/;
}
