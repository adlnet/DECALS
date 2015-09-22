grammar resolver;

options{
   // IGNORE_CASE=false ;
  //DEBUG_LOOKAHEAD= true ;
  //DEBUG_parseR= true ;
	
	//STATIC = false;
//  DEBUG_LOOKAHEAD= true ;
}

@parser::header{
package com.eduworks.resolver.lang.output;

import org.json.JSONObject;
import org.json.JSONException;
import com.eduworks.lang.EwMap;
}

@lexer::header{
package com.eduworks.resolver.lang.output;

}

@parser::members{
	public boolean debug = false;
	public EwMap<String,JSONObject> ids = new EwMap<String,JSONObject>();
	public EwMap<String,JSONObject> servlets = new EwMap<String,JSONObject>();
	public JSONObject obj = new JSONObject();
	public Stack<JSONObject> stk = new Stack<JSONObject>();
}

ID  :	('A'..'Z'|'a'..'z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

WS 	:	(' '|'\r'|'\n')*
	;

FUNCTIONID  :	'#'('a'..'z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

STRING
    :  '"' (~('\\'|'"'))* '"'
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

parse
 	:	(decl';'WS)(parse|EOF)?
	;

decl
	:	i=ID'='(functioncall|'{'param'}')
	|	'/'s=ID'->'i=ID
	;


functioncall
	:	x=FUNCTIONID'('param')'
	;
param
	:	x=ID'='
			 (
			  y=FUNCTIONID'('param')'
			| y=STRING
			| y=INT
			| y=ID
			)
	(','param)?
	;
