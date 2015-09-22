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

package com.eduworks.gwt.client.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.Window;

public abstract class DragDropHandler implements DropHandler, DragLeaveHandler, DragEnterHandler, DragOverHandler
{

	public List<File>	readQueue	= new ArrayList<File>();
	//private DropPanel		w;

	public DragDropHandler(DropPanel w)
	{
		//this.w = w;
		w.addDragEnterHandler(this);
		w.addDragLeaveHandler(this);
		w.addDragOverHandler(this);
		w.addDropHandler(this);
	}

	@Override
	public void onDragOver(DragOverEvent event)
	{
		// Mandatory handler, otherwise the default
		// behavior will kick in and onDrop will never
		// be called
		event.stopPropagation();
		event.preventDefault();
	}

	@Override
	public void onDragEnter(DragEnterEvent event)
	{
//		enableDragDropStyle(true);
		event.stopPropagation();
		event.preventDefault();
	}

	@Override
	public void onDragLeave(DragLeaveEvent event)
	{
//		enableDragDropStyle(false);
		event.stopPropagation();
		event.preventDefault();
	}

	public void processFiles(FileList files)
	{
		GWT.log("length=" + files.getLength());
		for (File file : files)
		{
			readQueue.add(file);
		}
		readNext();
	}

	public void readNext()
	{
		if (readQueue.size() > 0)
		{
			try
			{
				File file = readQueue.get(0);
				run(file);
				readQueue.remove(0);
			}
			catch (Throwable t)
			{
				// Necessary for FF (see bug
				// https://bugzilla.mozilla.org/show_bug.cgi?id=701154)
				// Standard-complying browsers will to go in this branch
				handleError(readQueue.get(0), t);
				readQueue.remove(0);
				readNext();
			}
		}
	}

	public abstract  void run(File file);

	public void handleError(File file, Throwable t)
	{
		String errorDesc = t.getMessage();
		Window.alert("File loading error for file: " + file.getName() + "\n" + errorDesc);
	}

	@Override
	public void onDrop(DropEvent event)
	{
		processFiles(event.getDataTransfer().<DataTransferExt> cast().getFiles());
//		enableDragDropStyle(false);
		event.stopPropagation();
		event.preventDefault();
	}

//	private void enableDragDropStyle(boolean enable)
//	{
////		if (enable)
////			Ui.applyStyleTo(w, RusselStyle.DRAG_DROP_HOVER);
////		else
////			Ui.removeStyleFrom(w, RusselStyle.DRAG_DROP_HOVER);
//	}
}
