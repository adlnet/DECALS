grammar resolver;

@parser::header{
package com.eduworks.resolver.lang.output;

import org.json.JSONObject;
import org.json.JSONException;
import com.eduworks.lang.EwMap;
import java.util.Iterator;
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

ID  :	('A'..'Z'|'a'..'z'|'_'|'[')(('a'..'z'|'A'..'Z'|'0'..'9'|'_'|':'|'.'|'['|']')*|('a'..'z'|'A'..'Z'|'0'..'9'|'_'|':'|'.'|'['|']'|'-')*('a'..'z'|'A'..'Z'|'0'..'9'|'_'|':'|'.'|'['|']'))
    ;

WS 	:	(' '|'\t'|'\r'|'\n')+
	;

FUNCTIONID  :	'#'('a'..'z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

COMMENT : '//' ( ~('\n') )* '\n'
	;

INT :	'0'..'9'+
    ;

STRING
    :  '"' ( ~('\\'|'"') )* '"'
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
 	:	(decl';'WS?) parse?
 	|	COMMENT WS? parse?
 	|	EOF
	;

decl
	:	i=ID WS?'='WS?
		(	functioncall				{ids.put(i.getText(),obj);obj = new JSONObject();}
		|	'{'param'}'				{ids.put(i.getText(),obj);obj = new JSONObject();}
		)
	|	'/'s=ID WS? '->' WS? i=ID			{servlets.put(s.getText(),ids.get(i.getText()));}
	;


functioncall
	:	x=FUNCTIONID WS?'('				{try{obj.put("function",x.getText().substring(1));}catch(JSONException e){}}param')'
	;
param
	:	WS? (x=ID WS? '=' WS?
			 (
			  y=FUNCTIONID WS?'('			{try{stk.push(obj);obj = new JSONObject();obj.put("function",y.getText().substring(1));}catch(JSONException e){}}
			  param					{try{JSONObject jo = obj;obj=stk.pop();obj.put(x.getText(),jo);}catch(JSONException e){}}
			  ')'
			| y=STRING				{try{obj.put(x.getText(),y.getText().substring(1,y.getText().length()-1));}catch(JSONException e){}} 
			| y=INT					{try{obj.put(x.getText(),y.getText().substring(1,y.getText().length()-1));}catch(JSONException e){}}
			| y=ID					{try{obj.put(x.getText(),ids.get(y.getText()));}catch(JSONException e){}}
			)
		|x=ID{				try{JSONObject jo = ids.get(x.getText());
						Iterator<String> it = jo.keys();
						while(it.hasNext())
						{
							String s = it.next();
							obj.put(s,jo.get(s));
						}}catch(JSONException e){}
			}
		)
	( WS? ',' param)? WS?
	;
