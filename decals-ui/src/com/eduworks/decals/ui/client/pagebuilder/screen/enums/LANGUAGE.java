package com.eduworks.decals.ui.client.pagebuilder.screen.enums;

public enum LANGUAGE {
	ENGLISH{
		public String toString(){
			return "English";
		}
		
		public String toSymbol(){
			return "en";
		}
	},
	GERMAN{
		public String toString(){
			return "German";
		}
		
		public String toSymbol(){
			return "de";
		}
	},
	ROMANIAN{
		public String toString(){
			return "Romanian";
		}
		
		public String toSymbol(){
			return "ro";
		}
	},
	POLISH{
		public String toString(){
			return "Polish";
		}
		
		public String toSymbol(){
			return "pl";
		}
	},
	UKRANIAN{
		public String toString(){
			return "Ukraine";
		}
		
		public String toSymbol(){
			return "ua";
		}
	},
	DUTCH{
		public String toString(){
			return "Dutch";
		}
		
		public String toSymbol(){
			return "nl";
		}
	},
	ICELANDIC{
		public String toString(){
			return "Icelandic";
		}
		
		public String toSymbol(){
			return "is";
		}
	},
	SPANISH{
		public String toString(){
			return "Spanish";
		}
		
		public String toSymbol(){
			return "es";
		}
	},
	FRENCH{
		public String toString(){
			return "French";
		}
		
		public String toSymbol(){
			return "fr";
		}
	},
	PORTUGESE{
		public String toString(){
			return "Portugese";
		}
		
		public String toSymbol(){
			return "pt";
		}
	},
	FINNISH{
		public String toString(){
			return "Finnish";
		}
		
		public String toSymbol(){
			return "fi";
		}
	},
	SLOVAK{
		public String toString(){
			return "Slovak";
		}
		
		public String toSymbol(){
			return "sk";
		}
	},
	ITALIAN{
		public String toString(){
			return "Italian";
		}
		
		public String toSymbol(){
			return "it";
		}
	},
	ESTONIAN{
		public String toString(){
			return "Estonian";
		}
		
		public String toSymbol(){
			return "ee";
		}
	},
	NORWEGIAN{
		public String toString(){
			return "Norwegian";
		}
		
		public String toSymbol(){
			return "no";
		}
	},
	SLOVENIAN{
		public String toString(){
			return "Slovenian";
		}
		
		public String toSymbol(){
			return "sl";
		}
	},
	LITHUANIAN{
		public String toString(){
			return "Lithuanian";
		}
		
		public String toSymbol(){
			return "lt";
		}
	},
	SWEDISH{
		public String toString(){
			return "Swedish";
		}
		
		public String toSymbol(){
			return "sv";
		}
	},
	MALTESE{
		public String toString(){
			return "Maltese";
		}
		
		public String toSymbol(){
			return "mt";
		}
	},
	RUSSIAN{
		public String toString(){
			return "Russian";
		}
		
		public String toSymbol(){
			return "ru";
		}
	},
	DANISH{
		public String toString(){
			return "Danish";
		}
		
		public String toSymbol(){
			return "da";
		}
	},
	CZECH{
		public String toString(){
			return "Czech";
		}
		
		public String toSymbol(){
			return "cz";
		}
	},
	OTHER{
		public String toString(){
			return "Other";
		}
		
		public String toSymbol(){
			return "ot";
		}
	};
	
	public abstract String toSymbol();
	
	public static LANGUAGE findSymbolLanguage(String symbol){
		for(LANGUAGE l : values()){
			if(l.toSymbol().equals(symbol)){
				return l;
			}
		}
		
		return null;
	}
}
