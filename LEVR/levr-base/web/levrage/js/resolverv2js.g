grammar resolverv2js;

options{
	language=JavaScript;
}

@parser::header{
	var debug = false;
	var ids = {};
	var servlets = {};
	var obj = {};
	var stk = [];
}

@lexer::header{
}

@parser::members{
}

ID  :	('A'..'Z'|'a'..'z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|':')*
    ;

SERVLETID  :	'/'('A'..'Z'|'a'..'z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'-'|':'|'/')*
    ;

ATID	:	'@'('A'..'Z'|'a'..'z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

WS 	:	(' '|'\t'|'\r'|'\n')+
	;

FUNCTIONID  :	'#'('a'..'z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;
    
DOTFUNCTIONID  :	'.'('a'..'z')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

COMMENT : '//' ( ~('\n') )* '\n'
	;

INT :	'0'..'9'+
    ;

STRING
    :  '"' ( ~('\\'|'"') )* '"'
    |  '\'' ( ~('\\'|'\'') )* '\''
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
 	|	WS parse?
 	|	EOF
	;

decl
	:	i=ID WS?'='WS?					
		({pvariableDeclare(i.getText());}	functioncall				{ids[i.getText()]=obj;obj = {};pvariableDeclareEnd();}
		|{pvariableGroupDeclare(i.getText());}	'{'param'}'				{ids[i.getText()]=obj;obj = {};pvariableDeclareEnd();}
		)						
	|	x=ID WS?'='WS? y=ID 				{obj=ids[y.getText()];pvariableDeclare(x.getText());pvariableReference(x.getText());}
		dotfunctioncall?				{ids[x.getText()]=obj;obj = {};pvariableDeclareEnd();}
	|	s=SERVLETID WS? ' ->' WS? i=ID			{servlets[s.getText()]=ids[i.getText()];pservletDeclare(s.getText(),i.getText());}
	|	s=SERVLETID WS? '=' WS? i=ID			{servlets[s.getText()]=ids[i.getText()];pservletDeclare(s.getText(),i.getText());}
	;


functioncall
	:	x=FUNCTIONID WS?'('				{obj["function"]=x.getText().substring(1);pfunctionDeclare(x.getText());}
		param')' 					{pfunctionDeclareEnd();}
		dotfunctioncall?				
	;
	
dotfunctioncall
	:	y=DOTFUNCTIONID WS?'('				{stk.push(obj);obj = {};obj["function"]=y.getText().substring(1);pdot();pfunctionDeclare(y.getText().substring(1));}
		param						{obj["obj"]=stk.pop();}{pfunctionDeclareEnd();}
		')' dotfunctioncall?				
	;
	
param
	:	WS? (x=ID WS? '=' WS?				{pparameterBegin(x.getText());}
			 (
			  y=FUNCTIONID WS?'('			{stk.push(obj);obj = {};obj["function"]=y.getText().substring(1);pfunctionDeclare(y.getText());}
			  param					{pfunctionDeclareEnd();}
			  ')' dotfunctioncall?			{var jo = obj;obj=stk.pop();obj[x.getText()]=jo;}
			| y=STRING				{obj[x.getText()]=y.getText().substring(1,y.getText().length-1);pstringLiteral(y.getText());} 
			| y=INT					{obj[x.getText()]=y.getText().substring(1,y.getText().length-1);pstringLiteral(y.getText());}
								//{obj.put(x.getText(),ids.get(y.getText()));}
			| y=ID					{stk.push(obj);obj = ids[y.getText()];pvariableReference(y.getText());}
			  dotfunctioncall?			{var jo = obj;obj=stk.pop();obj[x.getText()]=jo;}
			)		{pparameterEnd();}			
		|x=ID{				var jo = ids[x.getText()];pvariableGroupReference(x.getText());
						for (var key in jo)
							obj[key],jo[key];
			} {}
		| WS?
		)
	( WS? ',' param)? WS?
	| WS? x=ATID				{obj[x.getText().substring(1)]=x.getText();}
	( WS? ',' param)? WS?
	;
