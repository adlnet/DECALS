package com.eduworks.decals.ui.client.pagebuilder.screen.enums;

public enum RESOURCE_TYPE{
	DOCUMENT {
		public String toString(){
			return "Document";
		}
		
		public String toSymbol(){
			return "text";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	IMAGE {
		public String toString(){
			return "Image";
		}
		
		public String toSymbol(){
			return "image";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	AUDIO {
		public String toString(){
			return "Audio";
		}
		
		public String toSymbol(){
			return "audio";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	VIDEO {
		public String toString(){
			return "Video";
		}
		
		public String toSymbol(){
			return "video";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	PRESENTATION {
		public String toString(){
			return "Presentation";
		}
		
		public String toSymbol(){
			return "slideshow";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	WEBPAGE {
		public String toString(){
			return "Webpage";
		}
		
		public String toSymbol(){
			return "web";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	TUTOR {
		public String toString(){
			return "Live Tutor/Webcast";
		}
		
		public String toSymbol(){
			return "tutor";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	SIMULATION {
		public String toString(){
			return "Simulation/Game";
		}
		
		public String toSymbol(){
			return "interactive";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	},
	OTHER {
		public String toString(){
			return "Other";
		}
		
		public String toSymbol(){
			return "other";
		}
		
		public String[] getMimeTypes(){
			return new String[1];
		}
	};

	public abstract String toSymbol();
	
	public abstract String[] getMimeTypes();
	
	public static RESOURCE_TYPE findSymbolType(String symbol){
		for(RESOURCE_TYPE t : values()){
			if(t.toSymbol().equals(symbol)){
				return t;
			}
		}
		
		return null;
	}
}
