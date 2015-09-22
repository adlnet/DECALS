package com.eduworks.decals.ui.client.pagebuilder.screen.enums;

public enum GRADE_LEVEL {
	KINDERGARTEN{
		public String toString(){
			return "Kindergarten";
		}
		
		public String toSymbol(){
			return "kg";
		}
	},
	ELEMENTARY{
		public String toString(){
			return "Elementary";
		}
		
		public String toSymbol(){
			return "es";
		}
	},
	MIDDLE_SCHOOL{
		public String toString(){
			return "Middle School";
		}
		
		public String toSymbol(){
			return "ms";
		}
	},
	HIGH_SCHOOL{
		public String toString(){
			return "High School";
		}
		
		public String toSymbol(){
			return "hs";
		}
	},
	HIGHER_ED{
		public String toString(){
			return "Higher Education";
		}
		
		public String toSymbol(){
			return "he";
		}
	},
	VOCATIONAL{
		public String toString(){
			return "Vocational Training";
		}
		
		public String toSymbol(){
			return "vt";
		}
	};
	
	public abstract String toSymbol();
	
	public static GRADE_LEVEL findSymbolGrade(String symbol){
		for(GRADE_LEVEL l : values()){
			if(l.toSymbol().equals(symbol)){
				return l;
			}
		}
		
		return null;
	}
}
