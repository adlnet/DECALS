package com.eduworks.gwt.client.pagebuilder.modal;

public class ModalDispatch {
	
	protected void loadModal(ModalTemplate m){		
		m.display();
		
		ModalAssembler.resizeModal(m.getModalSize());

		// Right now this call does nothing because showing is handled by Foundation JS
		ModalAssembler.showModal();
	}
	
	public void hideModal(){	
		ModalAssembler.hideModal();
	}
}
