package com.eduworks.russel.ds.client;

import com.eduworks.gwt.client.net.callback.AlfrescoCallback;
import com.eduworks.gwt.client.net.callback.EventCallback;
import com.eduworks.gwt.client.net.packet.AlfrescoPacket;
import com.eduworks.russel.ui.client.epss.ProjectFileModel;
import com.eduworks.russel.ui.client.handler.Adl3DRSearchHandler;
import com.eduworks.russel.ui.client.handler.AlfrescoSearchHandler;
import com.eduworks.russel.ds.client.handler.TileHandler;
import com.eduworks.russel.ui.client.Constants;
import com.eduworks.russel.ui.client.EventHandlers;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

public class DsEventHandlers extends EventHandlers
{
	EventHandlers me = (EventHandlers)this;
	
	protected DsScreenDispatch view()
	{
		return (DsScreenDispatch) Constants.view;
	}

	public EventCallback tileClickHandler(final TileHandler tile)
	{
		return new EventCallback()
		{
			@Override
			public void onEvent(Event event)
			{
				if (tile.tileType.equals(AlfrescoSearchHandler.PROJECT_TYPE))
					ProjectFileModel.importFromAlfrescoNode(tile.searchRecord.getNodeId(),
							tile.searchRecord.getFilename(), new AlfrescoCallback<AlfrescoPacket>()
							{
								@Override
								public void onSuccess(AlfrescoPacket alfrescoPacket)
								{
									Constants.view.loadEPSSEditScreen(alfrescoPacket);
								}

								@Override
								public void onFailure(Throwable caught)
								{
									Window.alert("Fooing couldn't load project file " + caught);
								}
							});
//				else if (tile.tileType.equals(AlfrescoSearchHandler.RECENT_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.ASSET_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.SEARCH_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.COLLECTION_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.FLR_TYPE)
//						|| tile.tileType.equals(Adl3DRSearchHandler.SEARCH3DR_TYPE)
//						|| tile.tileType.equals(Adl3DRSearchHandler.ASSET3DR_TYPE))
//					view().loadDetailScreen(tile.searchRecord, tile);
			}
		};
	}

	public EventCallback tileOpenHandler(final TileHandler tile) {
		return new EventCallback()
		{
			@Override
			public void onEvent(Event event)
			{
				if (tile.tileType.equals(AlfrescoSearchHandler.PROJECT_TYPE))
					ProjectFileModel.importFromAlfrescoNode(tile.searchRecord.getNodeId(),
							tile.searchRecord.getFilename(), new AlfrescoCallback<AlfrescoPacket>()
							{
								@Override
								public void onSuccess(AlfrescoPacket alfrescoPacket)
								{
									Constants.view.loadEPSSEditScreen(alfrescoPacket);
								}

								@Override
								public void onFailure(Throwable caught)
								{
									Window.alert("Fooing couldn't load project file " + caught);
								}
							});
//				else if (tile.tileType.equals(AlfrescoSearchHandler.RECENT_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.ASSET_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.SEARCH_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.COLLECTION_TYPE)
//						|| tile.tileType.equals(AlfrescoSearchHandler.FLR_TYPE)
//						|| tile.tileType.equals(Adl3DRSearchHandler.SEARCH3DR_TYPE)
//						|| tile.tileType.equals(Adl3DRSearchHandler.ASSET3DR_TYPE))
//					view().loadDetailScreen(tile.searchRecord, tile);
			}
		};
	}
}
