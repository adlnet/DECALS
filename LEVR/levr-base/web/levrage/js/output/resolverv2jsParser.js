// $ANTLR 3.1.1 \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g 2014-02-10 15:48:46

	var debug = false;
	var ids = {};
	var servlets = {};
	var obj = {};
	var stk = [];


var resolverv2jsParser = function(input, state) {
    if (!state) {
        state = new org.antlr.runtime.RecognizerSharedState();
    }

    (function(){


    }).call(this);

    resolverv2jsParser.superclass.constructor.call(this, input, state);

    this.dfa17 = new resolverv2jsParser.DFA17(this);

         

    /* @todo only create adaptor if output=AST */
    this.adaptor = new org.antlr.runtime.tree.CommonTreeAdaptor();

};

org.antlr.lang.augmentObject(resolverv2jsParser, {
    SERVLETID: 5,
    T__24: 24,
    T__23: 23,
    FUNCTIONID: 8,
    T__22: 22,
    T__21: 21,
    UNICODE_ESC: 14,
    T__20: 20,
    OCTAL_ESC: 15,
    HEX_DIGIT: 13,
    INT: 11,
    ATID: 6,
    ID: 4,
    EOF: -1,
    T__19: 19,
    ESC_SEQ: 16,
    WS: 7,
    T__18: 18,
    T__17: 17,
    DOTFUNCTIONID: 9,
    COMMENT: 10,
    STRING: 12
});

(function(){
// public class variables
var SERVLETID= 5,
    T__24= 24,
    T__23= 23,
    FUNCTIONID= 8,
    T__22= 22,
    T__21= 21,
    UNICODE_ESC= 14,
    T__20= 20,
    OCTAL_ESC= 15,
    HEX_DIGIT= 13,
    INT= 11,
    ATID= 6,
    ID= 4,
    EOF= -1,
    T__19= 19,
    ESC_SEQ= 16,
    WS= 7,
    T__18= 18,
    T__17= 17,
    DOTFUNCTIONID= 9,
    COMMENT= 10,
    STRING= 12;

// public instance methods/vars
org.antlr.lang.extend(resolverv2jsParser, org.antlr.runtime.Parser, {
        

    getTokenNames: function() { return resolverv2jsParser.tokenNames; },
    getGrammarFileName: function() { return "\\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g"; }
});
org.antlr.lang.augmentObject(resolverv2jsParser.prototype, {


    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:72:1: parse : ( ( decl ';' ( WS )? ) ( parse )? | COMMENT ( WS )? ( parse )? | WS ( parse )? | EOF );
    // $ANTLR start "parse"
    parse: function() {
        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:3: ( ( decl ';' ( WS )? ) ( parse )? | COMMENT ( WS )? ( parse )? | WS ( parse )? | EOF )
            var alt6=4;
            switch ( this.input.LA(1) ) {
            case ID:
            case SERVLETID:
                alt6=1;
                break;
            case COMMENT:
                alt6=2;
                break;
            case WS:
                alt6=3;
                break;
            case EOF:
                alt6=4;
                break;
            default:
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 6, 0, this.input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:5: ( decl ';' ( WS )? ) ( parse )?
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:5: ( decl ';' ( WS )? )
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:6: decl ';' ( WS )?
                    this.pushFollow(resolverv2jsParser.FOLLOW_decl_in_parse556);
                    this.decl();

                    this.state._fsp--;

                    this.match(this.input,17,resolverv2jsParser.FOLLOW_17_in_parse557); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:13: ( WS )?
                    var alt1=2;
                    var LA1_0 = this.input.LA(1);

                    if ( (LA1_0==WS) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:13: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_parse558); 


                            break;

                    }




                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:18: ( parse )?
                    var alt2=2;
                    var LA2_0 = this.input.LA(1);

                    if ( (LA2_0==EOF||(LA2_0>=ID && LA2_0<=SERVLETID)||LA2_0==WS||LA2_0==COMMENT) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:73:18: parse
                            this.pushFollow(resolverv2jsParser.FOLLOW_parse_in_parse562);
                            this.parse();

                            this.state._fsp--;



                            break;

                    }



                    break;
                case 2 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:74:5: COMMENT ( WS )? ( parse )?
                    this.match(this.input,COMMENT,resolverv2jsParser.FOLLOW_COMMENT_in_parse569); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:74:13: ( WS )?
                    var alt3=2;
                    var LA3_0 = this.input.LA(1);

                    if ( (LA3_0==WS) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:74:13: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_parse571); 


                            break;

                    }

                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:74:17: ( parse )?
                    var alt4=2;
                    var LA4_0 = this.input.LA(1);

                    if ( (LA4_0==EOF||(LA4_0>=ID && LA4_0<=SERVLETID)||LA4_0==WS||LA4_0==COMMENT) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:74:17: parse
                            this.pushFollow(resolverv2jsParser.FOLLOW_parse_in_parse574);
                            this.parse();

                            this.state._fsp--;



                            break;

                    }



                    break;
                case 3 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:75:5: WS ( parse )?
                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_parse581); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:75:8: ( parse )?
                    var alt5=2;
                    var LA5_0 = this.input.LA(1);

                    if ( (LA5_0==EOF||(LA5_0>=ID && LA5_0<=SERVLETID)||LA5_0==WS||LA5_0==COMMENT) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:75:8: parse
                            this.pushFollow(resolverv2jsParser.FOLLOW_parse_in_parse583);
                            this.parse();

                            this.state._fsp--;



                            break;

                    }



                    break;
                case 4 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:76:5: EOF
                    this.match(this.input,EOF,resolverv2jsParser.FOLLOW_EOF_in_parse590); 


                    break;

            }
        }
        catch (re) {
            if (re instanceof org.antlr.runtime.RecognitionException) {
                this.reportError(re);
                this.recover(this.input,re);
            } else {
                throw re;
            }
        }
        finally {
        }
        return ;
    },


    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:79:1: decl : (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )? | s= SERVLETID ( WS )? ' ->' ( WS )? i= ID | s= SERVLETID ( WS )? '=' ( WS )? i= ID );
    // $ANTLR start "decl"
    decl: function() {
        var i = null;
        var x = null;
        var y = null;
        var s = null;

        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:80:2: (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )? | s= SERVLETID ( WS )? ' ->' ( WS )? i= ID | s= SERVLETID ( WS )? '=' ( WS )? i= ID )
            var alt17=4;
            alt17 = this.dfa17.predict(this.input);
            switch (alt17) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:80:4: i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' )
                    i=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_decl603); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:80:9: ( WS )?
                    var alt7=2;
                    var LA7_0 = this.input.LA(1);

                    if ( (LA7_0==WS) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:80:9: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl605); 


                            break;

                    }

                    this.match(this.input,18,resolverv2jsParser.FOLLOW_18_in_decl607); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:80:15: ( WS )?
                    var alt8=2;
                    var LA8_0 = this.input.LA(1);

                    if ( (LA8_0==WS) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:80:15: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl608); 


                            break;

                    }

                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:81:3: ( functioncall | '{' param '}' )
                    var alt9=2;
                    var LA9_0 = this.input.LA(1);

                    if ( (LA9_0==FUNCTIONID) ) {
                        alt9=1;
                    }
                    else if ( (LA9_0==19) ) {
                        alt9=2;
                    }
                    else {
                        var nvae =
                            new org.antlr.runtime.NoViableAltException("", 9, 0, this.input);

                        throw nvae;
                    }
                    switch (alt9) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:81:4: functioncall
                            pvariableDeclare(i.getText());
                            this.pushFollow(resolverv2jsParser.FOLLOW_functioncall_in_decl621);
                            this.functioncall();

                            this.state._fsp--;

                            ids[i.getText()]=obj;obj = {};pvariableDeclareEnd();


                            break;
                        case 2 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:82:4: '{' param '}'
                            pvariableGroupDeclare(i.getText());
                            this.match(this.input,19,resolverv2jsParser.FOLLOW_19_in_decl633); 
                            this.pushFollow(resolverv2jsParser.FOLLOW_param_in_decl634);
                            this.param();

                            this.state._fsp--;

                            this.match(this.input,20,resolverv2jsParser.FOLLOW_20_in_decl635); 
                            ids[i.getText()]=obj;obj = {};pvariableDeclareEnd();


                            break;

                    }



                    break;
                case 2 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:84:4: x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )?
                    x=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_decl657); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:84:9: ( WS )?
                    var alt10=2;
                    var LA10_0 = this.input.LA(1);

                    if ( (LA10_0==WS) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:84:9: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl659); 


                            break;

                    }

                    this.match(this.input,18,resolverv2jsParser.FOLLOW_18_in_decl661); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:84:15: ( WS )?
                    var alt11=2;
                    var LA11_0 = this.input.LA(1);

                    if ( (LA11_0==WS) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:84:15: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl662); 


                            break;

                    }

                    y=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_decl667); 
                    obj=ids[y.getText()];pvariableDeclare(x.getText());pvariableReference(y.getText());
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:85:3: ( dotfunctioncall )?
                    var alt12=2;
                    var LA12_0 = this.input.LA(1);

                    if ( (LA12_0==DOTFUNCTIONID) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:85:3: dotfunctioncall
                            this.pushFollow(resolverv2jsParser.FOLLOW_dotfunctioncall_in_decl677);
                            this.dotfunctioncall();

                            this.state._fsp--;



                            break;

                    }

                    ids[x.getText()]=obj;obj = {};pvariableDeclareEnd();


                    break;
                case 3 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:86:4: s= SERVLETID ( WS )? ' ->' ( WS )? i= ID
                    s=this.match(this.input,SERVLETID,resolverv2jsParser.FOLLOW_SERVLETID_in_decl690); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:86:16: ( WS )?
                    var alt13=2;
                    var LA13_0 = this.input.LA(1);

                    if ( (LA13_0==WS) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:86:16: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl692); 


                            break;

                    }

                    this.match(this.input,21,resolverv2jsParser.FOLLOW_21_in_decl695); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:86:26: ( WS )?
                    var alt14=2;
                    var LA14_0 = this.input.LA(1);

                    if ( (LA14_0==WS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:86:26: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl697); 


                            break;

                    }

                    i=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_decl702); 
                    servlets[s.getText()]=ids[i.getText()];pservletDeclare(s.getText(),i.getText());


                    break;
                case 4 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:87:4: s= SERVLETID ( WS )? '=' ( WS )? i= ID
                    s=this.match(this.input,SERVLETID,resolverv2jsParser.FOLLOW_SERVLETID_in_decl713); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:87:16: ( WS )?
                    var alt15=2;
                    var LA15_0 = this.input.LA(1);

                    if ( (LA15_0==WS) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:87:16: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl715); 


                            break;

                    }

                    this.match(this.input,18,resolverv2jsParser.FOLLOW_18_in_decl718); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:87:24: ( WS )?
                    var alt16=2;
                    var LA16_0 = this.input.LA(1);

                    if ( (LA16_0==WS) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:87:24: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_decl720); 


                            break;

                    }

                    i=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_decl725); 
                    servlets[s.getText()]=ids[i.getText()];pservletDeclare(s.getText(),i.getText());


                    break;

            }
        }
        catch (re) {
            if (re instanceof org.antlr.runtime.RecognitionException) {
                this.reportError(re);
                this.recover(this.input,re);
            } else {
                throw re;
            }
        }
        finally {
        }
        return ;
    },


    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:91:1: functioncall : x= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? ;
    // $ANTLR start "functioncall"
    functioncall: function() {
        var x = null;

        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:92:2: (x= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:92:4: x= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )?
            x=this.match(this.input,FUNCTIONID,resolverv2jsParser.FOLLOW_FUNCTIONID_in_functioncall743); 
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:92:17: ( WS )?
            var alt18=2;
            var LA18_0 = this.input.LA(1);

            if ( (LA18_0==WS) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:92:17: WS
                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_functioncall745); 


                    break;

            }

            this.match(this.input,22,resolverv2jsParser.FOLLOW_22_in_functioncall747); 
            obj["function"]=x.getText().substring(1);pfunctionDeclare(x.getText());
            this.pushFollow(resolverv2jsParser.FOLLOW_param_in_functioncall756);
            this.param();

            this.state._fsp--;

            this.match(this.input,23,resolverv2jsParser.FOLLOW_23_in_functioncall757); 
            pfunctionDeclareEnd();
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:94:3: ( dotfunctioncall )?
            var alt19=2;
            var LA19_0 = this.input.LA(1);

            if ( (LA19_0==DOTFUNCTIONID) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:94:3: dotfunctioncall
                    this.pushFollow(resolverv2jsParser.FOLLOW_dotfunctioncall_in_functioncall768);
                    this.dotfunctioncall();

                    this.state._fsp--;



                    break;

            }




        }
        catch (re) {
            if (re instanceof org.antlr.runtime.RecognitionException) {
                this.reportError(re);
                this.recover(this.input,re);
            } else {
                throw re;
            }
        }
        finally {
        }
        return ;
    },


    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:97:1: dotfunctioncall : y= DOTFUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? ;
    // $ANTLR start "dotfunctioncall"
    dotfunctioncall: function() {
        var y = null;

        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:98:2: (y= DOTFUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:98:4: y= DOTFUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )?
            y=this.match(this.input,DOTFUNCTIONID,resolverv2jsParser.FOLLOW_DOTFUNCTIONID_in_dotfunctioncall787); 
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:98:20: ( WS )?
            var alt20=2;
            var LA20_0 = this.input.LA(1);

            if ( (LA20_0==WS) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:98:20: WS
                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_dotfunctioncall789); 


                    break;

            }

            this.match(this.input,22,resolverv2jsParser.FOLLOW_22_in_dotfunctioncall791); 
            stk.push(obj);obj = {};obj["function"]=y.getText().substring(1);pdot();pfunctionDeclare(y.getText().substring(1));
            this.pushFollow(resolverv2jsParser.FOLLOW_param_in_dotfunctioncall800);
            this.param();

            this.state._fsp--;

            obj["obj"]=stk.pop();
            pfunctionDeclareEnd();
            this.match(this.input,23,resolverv2jsParser.FOLLOW_23_in_dotfunctioncall812); 
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:100:7: ( dotfunctioncall )?
            var alt21=2;
            var LA21_0 = this.input.LA(1);

            if ( (LA21_0==DOTFUNCTIONID) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:100:7: dotfunctioncall
                    this.pushFollow(resolverv2jsParser.FOLLOW_dotfunctioncall_in_dotfunctioncall814);
                    this.dotfunctioncall();

                    this.state._fsp--;



                    break;

            }




        }
        catch (re) {
            if (re instanceof org.antlr.runtime.RecognitionException) {
                this.reportError(re);
                this.recover(this.input,re);
            } else {
                throw re;
            }
        }
        finally {
        }
        return ;
    },


    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:103:1: param : ( ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? ) ( ( WS )? ',' param )? ( WS )? | ( WS )? x= ATID ( ( WS )? ',' param )? ( WS )? );
    // $ANTLR start "param"
    param: function() {
        var x = null;
        var y = null;

        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:2: ( ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? ) ( ( WS )? ',' param )? ( WS )? | ( WS )? x= ATID ( ( WS )? ',' param )? ( WS )? )
            var alt38=2;
            switch ( this.input.LA(1) ) {
            case WS:
                var LA38_1 = this.input.LA(2);

                if ( (LA38_1==ID||LA38_1==WS||LA38_1==20||(LA38_1>=23 && LA38_1<=24)) ) {
                    alt38=1;
                }
                else if ( (LA38_1==ATID) ) {
                    alt38=2;
                }
                else {
                    var nvae =
                        new org.antlr.runtime.NoViableAltException("", 38, 1, this.input);

                    throw nvae;
                }
                break;
            case ID:
            case 20:
            case 23:
            case 24:
                alt38=1;
                break;
            case ATID:
                alt38=2;
                break;
            default:
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 38, 0, this.input);

                throw nvae;
            }

            switch (alt38) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:4: ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? ) ( ( WS )? ',' param )? ( WS )?
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:4: ( WS )?
                    var alt22=2;
                    var LA22_0 = this.input.LA(1);

                    if ( (LA22_0==WS) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:4: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param831); 


                            break;

                    }

                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:8: (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? )
                    var alt30=3;
                    var LA30_0 = this.input.LA(1);

                    if ( (LA30_0==ID) ) {
                        switch ( this.input.LA(2) ) {
                        case WS:
                            var LA30_3 = this.input.LA(3);

                            if ( (LA30_3==WS||LA30_3==20||(LA30_3>=23 && LA30_3<=24)) ) {
                                alt30=2;
                            }
                            else if ( (LA30_3==18) ) {
                                alt30=1;
                            }
                            else {
                                var nvae =
                                    new org.antlr.runtime.NoViableAltException("", 30, 3, this.input);

                                throw nvae;
                            }
                            break;
                        case 20:
                        case 23:
                        case 24:
                            alt30=2;
                            break;
                        case 18:
                            alt30=1;
                            break;
                        default:
                            var nvae =
                                new org.antlr.runtime.NoViableAltException("", 30, 1, this.input);

                            throw nvae;
                        }

                    }
                    else if ( (LA30_0==WS||LA30_0==20||(LA30_0>=23 && LA30_0<=24)) ) {
                        alt30=3;
                    }
                    else {
                        var nvae =
                            new org.antlr.runtime.NoViableAltException("", 30, 0, this.input);

                        throw nvae;
                    }
                    switch (alt30) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:9: x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? )
                            x=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_param837); 
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:14: ( WS )?
                            var alt23=2;
                            var LA23_0 = this.input.LA(1);

                            if ( (LA23_0==WS) ) {
                                alt23=1;
                            }
                            switch (alt23) {
                                case 1 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:14: WS
                                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param839); 


                                    break;

                            }

                            this.match(this.input,18,resolverv2jsParser.FOLLOW_18_in_param842); 
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:22: ( WS )?
                            var alt24=2;
                            var LA24_0 = this.input.LA(1);

                            if ( (LA24_0==WS) ) {
                                alt24=1;
                            }
                            switch (alt24) {
                                case 1 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:104:22: WS
                                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param844); 


                                    break;

                            }

                            pparameterBegin(x.getText());
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:105:5: (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? )
                            var alt28=4;
                            switch ( this.input.LA(1) ) {
                            case FUNCTIONID:
                                alt28=1;
                                break;
                            case STRING:
                                alt28=2;
                                break;
                            case INT:
                                alt28=3;
                                break;
                            case ID:
                                alt28=4;
                                break;
                            default:
                                var nvae =
                                    new org.antlr.runtime.NoViableAltException("", 28, 0, this.input);

                                throw nvae;
                            }

                            switch (alt28) {
                                case 1 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:106:6: y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )?
                                    y=this.match(this.input,FUNCTIONID,resolverv2jsParser.FOLLOW_FUNCTIONID_in_param865); 
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:106:19: ( WS )?
                                    var alt25=2;
                                    var LA25_0 = this.input.LA(1);

                                    if ( (LA25_0==WS) ) {
                                        alt25=1;
                                    }
                                    switch (alt25) {
                                        case 1 :
                                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:106:19: WS
                                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param867); 


                                            break;

                                    }

                                    this.match(this.input,22,resolverv2jsParser.FOLLOW_22_in_param869); 
                                    stk.push(obj);obj = {};obj["function"]=y.getText().substring(1);pfunctionDeclare(y.getText());
                                    this.pushFollow(resolverv2jsParser.FOLLOW_param_in_param880);
                                    this.param();

                                    this.state._fsp--;

                                    pfunctionDeclareEnd();
                                    this.match(this.input,23,resolverv2jsParser.FOLLOW_23_in_param893); 
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:108:10: ( dotfunctioncall )?
                                    var alt26=2;
                                    var LA26_0 = this.input.LA(1);

                                    if ( (LA26_0==DOTFUNCTIONID) ) {
                                        alt26=1;
                                    }
                                    switch (alt26) {
                                        case 1 :
                                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:108:10: dotfunctioncall
                                            this.pushFollow(resolverv2jsParser.FOLLOW_dotfunctioncall_in_param895);
                                            this.dotfunctioncall();

                                            this.state._fsp--;



                                            break;

                                    }

                                    var jo = obj;obj=stk.pop();obj[x.getText()]=jo;


                                    break;
                                case 2 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:109:6: y= STRING
                                    y=this.match(this.input,STRING,resolverv2jsParser.FOLLOW_STRING_in_param909); 
                                    obj[x.getText()]=y.getText().substring(1,y.getText().length-1);pstringLiteral(y.getText());


                                    break;
                                case 3 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:110:6: y= INT
                                    y=this.match(this.input,INT,resolverv2jsParser.FOLLOW_INT_in_param924); 
                                    obj[x.getText()]=y.getText().substring(1,y.getText().length-1);pstringLiteral(y.getText());


                                    break;
                                case 4 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:112:6: y= ID ( dotfunctioncall )?
                                    y=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_param948); 
                                    stk.push(obj);obj = ids[y.getText()];pvariableReference(y.getText());
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:113:6: ( dotfunctioncall )?
                                    var alt27=2;
                                    var LA27_0 = this.input.LA(1);

                                    if ( (LA27_0==DOTFUNCTIONID) ) {
                                        alt27=1;
                                    }
                                    switch (alt27) {
                                        case 1 :
                                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:113:6: dotfunctioncall
                                            this.pushFollow(resolverv2jsParser.FOLLOW_dotfunctioncall_in_param961);
                                            this.dotfunctioncall();

                                            this.state._fsp--;



                                            break;

                                    }

                                    var jo = obj;obj=stk.pop();obj[x.getText()]=jo;


                                    break;

                            }

                            pparameterEnd();


                            break;
                        case 2 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:115:4: x= ID
                            x=this.match(this.input,ID,resolverv2jsParser.FOLLOW_ID_in_param984); 
                            				var jo = ids[x.getText()];pvariableGroupReference(x.getText());
                            						for (var key in jo)
                            							obj[key],jo[key];
                            			



                            break;
                        case 3 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:119:5: ( WS )?
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:119:5: ( WS )?
                            var alt29=2;
                            var LA29_0 = this.input.LA(1);

                            if ( (LA29_0==WS) ) {
                                alt29=1;
                            }
                            switch (alt29) {
                                case 1 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:119:5: WS
                                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param993); 


                                    break;

                            }



                            break;

                    }

                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:121:2: ( ( WS )? ',' param )?
                    var alt32=2;
                    var LA32_0 = this.input.LA(1);

                    if ( (LA32_0==WS) ) {
                        var LA32_1 = this.input.LA(2);

                        if ( (LA32_1==24) ) {
                            alt32=1;
                        }
                    }
                    else if ( (LA32_0==24) ) {
                        alt32=1;
                    }
                    switch (alt32) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:121:4: ( WS )? ',' param
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:121:4: ( WS )?
                            var alt31=2;
                            var LA31_0 = this.input.LA(1);

                            if ( (LA31_0==WS) ) {
                                alt31=1;
                            }
                            switch (alt31) {
                                case 1 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:121:4: WS
                                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param1003); 


                                    break;

                            }

                            this.match(this.input,24,resolverv2jsParser.FOLLOW_24_in_param1006); 
                            this.pushFollow(resolverv2jsParser.FOLLOW_param_in_param1008);
                            this.param();

                            this.state._fsp--;



                            break;

                    }

                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:121:20: ( WS )?
                    var alt33=2;
                    var LA33_0 = this.input.LA(1);

                    if ( (LA33_0==WS) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:121:20: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param1012); 


                            break;

                    }



                    break;
                case 2 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:122:4: ( WS )? x= ATID ( ( WS )? ',' param )? ( WS )?
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:122:4: ( WS )?
                    var alt34=2;
                    var LA34_0 = this.input.LA(1);

                    if ( (LA34_0==WS) ) {
                        alt34=1;
                    }
                    switch (alt34) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:122:4: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param1018); 


                            break;

                    }

                    x=this.match(this.input,ATID,resolverv2jsParser.FOLLOW_ATID_in_param1023); 
                    obj[x.getText().substring(1)]=x.getText();
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:123:2: ( ( WS )? ',' param )?
                    var alt36=2;
                    var LA36_0 = this.input.LA(1);

                    if ( (LA36_0==WS) ) {
                        var LA36_1 = this.input.LA(2);

                        if ( (LA36_1==24) ) {
                            alt36=1;
                        }
                    }
                    else if ( (LA36_0==24) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:123:4: ( WS )? ',' param
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:123:4: ( WS )?
                            var alt35=2;
                            var LA35_0 = this.input.LA(1);

                            if ( (LA35_0==WS) ) {
                                alt35=1;
                            }
                            switch (alt35) {
                                case 1 :
                                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:123:4: WS
                                    this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param1033); 


                                    break;

                            }

                            this.match(this.input,24,resolverv2jsParser.FOLLOW_24_in_param1036); 
                            this.pushFollow(resolverv2jsParser.FOLLOW_param_in_param1038);
                            this.param();

                            this.state._fsp--;



                            break;

                    }

                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:123:20: ( WS )?
                    var alt37=2;
                    var LA37_0 = this.input.LA(1);

                    if ( (LA37_0==WS) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:123:20: WS
                            this.match(this.input,WS,resolverv2jsParser.FOLLOW_WS_in_param1042); 


                            break;

                    }



                    break;

            }
        }
        catch (re) {
            if (re instanceof org.antlr.runtime.RecognitionException) {
                this.reportError(re);
                this.recover(this.input,re);
            } else {
                throw re;
            }
        }
        finally {
        }
        return ;
    }

    // Delegated rules




}, true); // important to pass true to overwrite default implementations

org.antlr.lang.augmentObject(resolverv2jsParser, {
    DFA17_eotS:
        "\u000b\uffff",
    DFA17_eofS:
        "\u000b\uffff",
    DFA17_minS:
        "\u0001\u0004\u0002\u0007\u0001\u0012\u0001\u0004\u0001\u0012\u0002"+
    "\uffff\u0001\u0004\u0002\uffff",
    DFA17_maxS:
        "\u0001\u0005\u0001\u0012\u0001\u0015\u0001\u0012\u0001\u0013\u0001"+
    "\u0015\u0002\uffff\u0001\u0013\u0002\uffff",
    DFA17_acceptS:
        "\u0006\uffff\u0001\u0003\u0001\u0004\u0001\uffff\u0001\u0002\u0001"+
    "\u0001",
    DFA17_specialS:
        "\u000b\uffff}>",
    DFA17_transitionS: [
            "\u0001\u0001\u0001\u0002",
            "\u0001\u0003\u000a\uffff\u0001\u0004",
            "\u0001\u0005\u000a\uffff\u0001\u0007\u0002\uffff\u0001\u0006",
            "\u0001\u0004",
            "\u0001\u0009\u0002\uffff\u0001\u0008\u0001\u000a\u000a\uffff"+
            "\u0001\u000a",
            "\u0001\u0007\u0002\uffff\u0001\u0006",
            "",
            "",
            "\u0001\u0009\u0003\uffff\u0001\u000a\u000a\uffff\u0001\u000a",
            "",
            ""
    ]
});

org.antlr.lang.augmentObject(resolverv2jsParser, {
    DFA17_eot:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsParser.DFA17_eotS),
    DFA17_eof:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsParser.DFA17_eofS),
    DFA17_min:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(resolverv2jsParser.DFA17_minS),
    DFA17_max:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(resolverv2jsParser.DFA17_maxS),
    DFA17_accept:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsParser.DFA17_acceptS),
    DFA17_special:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsParser.DFA17_specialS),
    DFA17_transition: (function() {
        var a = [],
            i,
            numStates = resolverv2jsParser.DFA17_transitionS.length;
        for (i=0; i<numStates; i++) {
            a.push(org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsParser.DFA17_transitionS[i]));
        }
        return a;
    })()
});

resolverv2jsParser.DFA17 = function(recognizer) {
    this.recognizer = recognizer;
    this.decisionNumber = 17;
    this.eot = resolverv2jsParser.DFA17_eot;
    this.eof = resolverv2jsParser.DFA17_eof;
    this.min = resolverv2jsParser.DFA17_min;
    this.max = resolverv2jsParser.DFA17_max;
    this.accept = resolverv2jsParser.DFA17_accept;
    this.special = resolverv2jsParser.DFA17_special;
    this.transition = resolverv2jsParser.DFA17_transition;
};

org.antlr.lang.extend(resolverv2jsParser.DFA17, org.antlr.runtime.DFA, {
    getDescription: function() {
        return "79:1: decl : (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )? | s= SERVLETID ( WS )? ' ->' ( WS )? i= ID | s= SERVLETID ( WS )? '=' ( WS )? i= ID );";
    },
    dummy: null
});
 

// public class variables
org.antlr.lang.augmentObject(resolverv2jsParser, {
    tokenNames: ["<invalid>", "<EOR>", "<DOWN>", "<UP>", "ID", "SERVLETID", "ATID", "WS", "FUNCTIONID", "DOTFUNCTIONID", "COMMENT", "INT", "STRING", "HEX_DIGIT", "UNICODE_ESC", "OCTAL_ESC", "ESC_SEQ", "';'", "'='", "'{'", "'}'", "' ->'", "'('", "')'", "','"],
    FOLLOW_decl_in_parse556: new org.antlr.runtime.BitSet([0x00020000, 0x00000000]),
    FOLLOW_17_in_parse557: new org.antlr.runtime.BitSet([0x000004B2, 0x00000000]),
    FOLLOW_WS_in_parse558: new org.antlr.runtime.BitSet([0x000004B2, 0x00000000]),
    FOLLOW_parse_in_parse562: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_COMMENT_in_parse569: new org.antlr.runtime.BitSet([0x000004B2, 0x00000000]),
    FOLLOW_WS_in_parse571: new org.antlr.runtime.BitSet([0x000004B2, 0x00000000]),
    FOLLOW_parse_in_parse574: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_WS_in_parse581: new org.antlr.runtime.BitSet([0x000004B2, 0x00000000]),
    FOLLOW_parse_in_parse583: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_EOF_in_parse590: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_ID_in_decl603: new org.antlr.runtime.BitSet([0x00040080, 0x00000000]),
    FOLLOW_WS_in_decl605: new org.antlr.runtime.BitSet([0x00040000, 0x00000000]),
    FOLLOW_18_in_decl607: new org.antlr.runtime.BitSet([0x00080180, 0x00000000]),
    FOLLOW_WS_in_decl608: new org.antlr.runtime.BitSet([0x00080100, 0x00000000]),
    FOLLOW_functioncall_in_decl621: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_19_in_decl633: new org.antlr.runtime.BitSet([0x011000D0, 0x00000000]),
    FOLLOW_param_in_decl634: new org.antlr.runtime.BitSet([0x00100000, 0x00000000]),
    FOLLOW_20_in_decl635: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_ID_in_decl657: new org.antlr.runtime.BitSet([0x00040080, 0x00000000]),
    FOLLOW_WS_in_decl659: new org.antlr.runtime.BitSet([0x00040000, 0x00000000]),
    FOLLOW_18_in_decl661: new org.antlr.runtime.BitSet([0x00000090, 0x00000000]),
    FOLLOW_WS_in_decl662: new org.antlr.runtime.BitSet([0x00000010, 0x00000000]),
    FOLLOW_ID_in_decl667: new org.antlr.runtime.BitSet([0x00000202, 0x00000000]),
    FOLLOW_dotfunctioncall_in_decl677: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_SERVLETID_in_decl690: new org.antlr.runtime.BitSet([0x00200080, 0x00000000]),
    FOLLOW_WS_in_decl692: new org.antlr.runtime.BitSet([0x00200000, 0x00000000]),
    FOLLOW_21_in_decl695: new org.antlr.runtime.BitSet([0x00000090, 0x00000000]),
    FOLLOW_WS_in_decl697: new org.antlr.runtime.BitSet([0x00000010, 0x00000000]),
    FOLLOW_ID_in_decl702: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_SERVLETID_in_decl713: new org.antlr.runtime.BitSet([0x00040080, 0x00000000]),
    FOLLOW_WS_in_decl715: new org.antlr.runtime.BitSet([0x00040000, 0x00000000]),
    FOLLOW_18_in_decl718: new org.antlr.runtime.BitSet([0x00000090, 0x00000000]),
    FOLLOW_WS_in_decl720: new org.antlr.runtime.BitSet([0x00000010, 0x00000000]),
    FOLLOW_ID_in_decl725: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_FUNCTIONID_in_functioncall743: new org.antlr.runtime.BitSet([0x00400080, 0x00000000]),
    FOLLOW_WS_in_functioncall745: new org.antlr.runtime.BitSet([0x00400000, 0x00000000]),
    FOLLOW_22_in_functioncall747: new org.antlr.runtime.BitSet([0x018000D0, 0x00000000]),
    FOLLOW_param_in_functioncall756: new org.antlr.runtime.BitSet([0x00800000, 0x00000000]),
    FOLLOW_23_in_functioncall757: new org.antlr.runtime.BitSet([0x00000202, 0x00000000]),
    FOLLOW_dotfunctioncall_in_functioncall768: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_DOTFUNCTIONID_in_dotfunctioncall787: new org.antlr.runtime.BitSet([0x00400080, 0x00000000]),
    FOLLOW_WS_in_dotfunctioncall789: new org.antlr.runtime.BitSet([0x00400000, 0x00000000]),
    FOLLOW_22_in_dotfunctioncall791: new org.antlr.runtime.BitSet([0x018000D0, 0x00000000]),
    FOLLOW_param_in_dotfunctioncall800: new org.antlr.runtime.BitSet([0x00800000, 0x00000000]),
    FOLLOW_23_in_dotfunctioncall812: new org.antlr.runtime.BitSet([0x00000202, 0x00000000]),
    FOLLOW_dotfunctioncall_in_dotfunctioncall814: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_WS_in_param831: new org.antlr.runtime.BitSet([0x01000092, 0x00000000]),
    FOLLOW_ID_in_param837: new org.antlr.runtime.BitSet([0x00040080, 0x00000000]),
    FOLLOW_WS_in_param839: new org.antlr.runtime.BitSet([0x00040000, 0x00000000]),
    FOLLOW_18_in_param842: new org.antlr.runtime.BitSet([0x00001990, 0x00000000]),
    FOLLOW_WS_in_param844: new org.antlr.runtime.BitSet([0x00001910, 0x00000000]),
    FOLLOW_FUNCTIONID_in_param865: new org.antlr.runtime.BitSet([0x00400080, 0x00000000]),
    FOLLOW_WS_in_param867: new org.antlr.runtime.BitSet([0x00400000, 0x00000000]),
    FOLLOW_22_in_param869: new org.antlr.runtime.BitSet([0x018000D0, 0x00000000]),
    FOLLOW_param_in_param880: new org.antlr.runtime.BitSet([0x00800000, 0x00000000]),
    FOLLOW_23_in_param893: new org.antlr.runtime.BitSet([0x01000282, 0x00000000]),
    FOLLOW_dotfunctioncall_in_param895: new org.antlr.runtime.BitSet([0x01000082, 0x00000000]),
    FOLLOW_STRING_in_param909: new org.antlr.runtime.BitSet([0x01000082, 0x00000000]),
    FOLLOW_INT_in_param924: new org.antlr.runtime.BitSet([0x01000082, 0x00000000]),
    FOLLOW_ID_in_param948: new org.antlr.runtime.BitSet([0x01000282, 0x00000000]),
    FOLLOW_dotfunctioncall_in_param961: new org.antlr.runtime.BitSet([0x01000082, 0x00000000]),
    FOLLOW_ID_in_param984: new org.antlr.runtime.BitSet([0x01000082, 0x00000000]),
    FOLLOW_WS_in_param993: new org.antlr.runtime.BitSet([0x01000082, 0x00000000]),
    FOLLOW_WS_in_param1003: new org.antlr.runtime.BitSet([0x01000000, 0x00000000]),
    FOLLOW_24_in_param1006: new org.antlr.runtime.BitSet([0x010000D0, 0x00000000]),
    FOLLOW_param_in_param1008: new org.antlr.runtime.BitSet([0x00000082, 0x00000000]),
    FOLLOW_WS_in_param1012: new org.antlr.runtime.BitSet([0x00000002, 0x00000000]),
    FOLLOW_WS_in_param1018: new org.antlr.runtime.BitSet([0x00000040, 0x00000000]),
    FOLLOW_ATID_in_param1023: new org.antlr.runtime.BitSet([0x01000082, 0x00000000]),
    FOLLOW_WS_in_param1033: new org.antlr.runtime.BitSet([0x01000000, 0x00000000]),
    FOLLOW_24_in_param1036: new org.antlr.runtime.BitSet([0x010000D0, 0x00000000]),
    FOLLOW_param_in_param1038: new org.antlr.runtime.BitSet([0x00000082, 0x00000000]),
    FOLLOW_WS_in_param1042: new org.antlr.runtime.BitSet([0x00000002, 0x00000000])
});

})();