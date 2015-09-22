// $ANTLR 3.1.1 \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g 2014-02-10 15:48:46



var resolverv2jsLexer = function(input, state) {
// alternate constructor @todo
// public resolverv2jsLexer(CharStream input)
// public resolverv2jsLexer(CharStream input, RecognizerSharedState state) {
    if (!state) {
        state = new org.antlr.runtime.RecognizerSharedState();
    }

    (function(){
    }).call(this);

    this.dfa14 = new resolverv2jsLexer.DFA14(this);
    resolverv2jsLexer.superclass.constructor.call(this, input, state);


};

org.antlr.lang.augmentObject(resolverv2jsLexer, {
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
    WS: 7,
    ESC_SEQ: 16,
    T__18: 18,
    T__17: 17,
    DOTFUNCTIONID: 9,
    COMMENT: 10,
    STRING: 12
});

(function(){
var HIDDEN = org.antlr.runtime.Token.HIDDEN_CHANNEL,
    EOF = org.antlr.runtime.Token.EOF;
org.antlr.lang.extend(resolverv2jsLexer, org.antlr.runtime.Lexer, {
    SERVLETID : 5,
    T__24 : 24,
    T__23 : 23,
    FUNCTIONID : 8,
    T__22 : 22,
    T__21 : 21,
    UNICODE_ESC : 14,
    T__20 : 20,
    OCTAL_ESC : 15,
    HEX_DIGIT : 13,
    INT : 11,
    ATID : 6,
    ID : 4,
    EOF : -1,
    T__19 : 19,
    WS : 7,
    ESC_SEQ : 16,
    T__18 : 18,
    T__17 : 17,
    DOTFUNCTIONID : 9,
    COMMENT : 10,
    STRING : 12,
    getGrammarFileName: function() { return "\\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g"; }
});
org.antlr.lang.augmentObject(resolverv2jsLexer.prototype, {
    // $ANTLR start T__17
    mT__17: function()  {
        try {
            var _type = this.T__17;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:10:7: ( ';' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:10:9: ';'
            this.match(';'); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__17",

    // $ANTLR start T__18
    mT__18: function()  {
        try {
            var _type = this.T__18;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:11:7: ( '=' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:11:9: '='
            this.match('='); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__18",

    // $ANTLR start T__19
    mT__19: function()  {
        try {
            var _type = this.T__19;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:12:7: ( '{' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:12:9: '{'
            this.match('{'); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__19",

    // $ANTLR start T__20
    mT__20: function()  {
        try {
            var _type = this.T__20;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:13:7: ( '}' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:13:9: '}'
            this.match('}'); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__20",

    // $ANTLR start T__21
    mT__21: function()  {
        try {
            var _type = this.T__21;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:14:7: ( ' ->' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:14:9: ' ->'
            this.match(" ->"); 




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__21",

    // $ANTLR start T__22
    mT__22: function()  {
        try {
            var _type = this.T__22;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:15:7: ( '(' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:15:9: '('
            this.match('('); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__22",

    // $ANTLR start T__23
    mT__23: function()  {
        try {
            var _type = this.T__23;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:16:7: ( ')' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:16:9: ')'
            this.match(')'); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__23",

    // $ANTLR start T__24
    mT__24: function()  {
        try {
            var _type = this.T__24;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:17:7: ( ',' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:17:9: ','
            this.match(','); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "T__24",

    // $ANTLR start ID
    mID: function()  {
        try {
            var _type = this.ID;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:21:5: ( ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' )* )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:21:7: ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' )*
            if ( (this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                this.input.consume();

            }
            else {
                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                this.recover(mse);
                throw mse;}

            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:21:30: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' )*
            loop1:
            do {
                var alt1=2;
                var LA1_0 = this.input.LA(1);

                if ( (LA1_0=='-'||(LA1_0>='0' && LA1_0<=':')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:
                    if ( this.input.LA(1)=='-'||(this.input.LA(1)>='0' && this.input.LA(1)<=':')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    break loop1;
                }
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "ID",

    // $ANTLR start SERVLETID
    mSERVLETID: function()  {
        try {
            var _type = this.SERVLETID;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:24:12: ( '/' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' | '/' )* )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:24:14: '/' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' | '/' )*
            this.match('/'); 
            if ( (this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                this.input.consume();

            }
            else {
                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                this.recover(mse);
                throw mse;}

            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:24:40: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' | '/' )*
            loop2:
            do {
                var alt2=2;
                var LA2_0 = this.input.LA(1);

                if ( (LA2_0=='-'||(LA2_0>='/' && LA2_0<=':')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:
                    if ( this.input.LA(1)=='-'||(this.input.LA(1)>='/' && this.input.LA(1)<=':')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    break loop2;
                }
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "SERVLETID",

    // $ANTLR start ATID
    mATID: function()  {
        try {
            var _type = this.ATID;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:27:6: ( '@' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:27:8: '@' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            this.match('@'); 
            if ( (this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                this.input.consume();

            }
            else {
                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                this.recover(mse);
                throw mse;}

            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:27:34: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop3:
            do {
                var alt3=2;
                var LA3_0 = this.input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:
                    if ( (this.input.LA(1)>='0' && this.input.LA(1)<='9')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    break loop3;
                }
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "ATID",

    // $ANTLR start WS
    mWS: function()  {
        try {
            var _type = this.WS;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:30:5: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:30:7: ( ' ' | '\\t' | '\\r' | '\\n' )+
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:30:7: ( ' ' | '\\t' | '\\r' | '\\n' )+
            var cnt4=0;
            loop4:
            do {
                var alt4=2;
                var LA4_0 = this.input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||LA4_0=='\r'||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:
                    if ( (this.input.LA(1)>='\t' && this.input.LA(1)<='\n')||this.input.LA(1)=='\r'||this.input.LA(1)==' ' ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    if ( cnt4 >= 1 ) {
                        break loop4;
                    }
                        var eee = new org.antlr.runtime.EarlyExitException(4, this.input);
                        throw eee;
                }
                cnt4++;
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "WS",

    // $ANTLR start FUNCTIONID
    mFUNCTIONID: function()  {
        try {
            var _type = this.FUNCTIONID;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:33:13: ( '#' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:33:15: '#' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            this.match('#'); 
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:33:18: ( 'a' .. 'z' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:33:19: 'a' .. 'z'
            this.matchRange('a','z'); 



            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:33:28: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop5:
            do {
                var alt5=2;
                var LA5_0 = this.input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')||(LA5_0>='A' && LA5_0<='Z')||LA5_0=='_'||(LA5_0>='a' && LA5_0<='z')) ) {
                    alt5=1;
                }


                switch (alt5) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:
                    if ( (this.input.LA(1)>='0' && this.input.LA(1)<='9')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    break loop5;
                }
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "FUNCTIONID",

    // $ANTLR start DOTFUNCTIONID
    mDOTFUNCTIONID: function()  {
        try {
            var _type = this.DOTFUNCTIONID;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:36:16: ( '.' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:36:18: '.' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            this.match('.'); 
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:36:21: ( 'a' .. 'z' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:36:22: 'a' .. 'z'
            this.matchRange('a','z'); 



            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:36:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop6:
            do {
                var alt6=2;
                var LA6_0 = this.input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')||(LA6_0>='A' && LA6_0<='Z')||LA6_0=='_'||(LA6_0>='a' && LA6_0<='z')) ) {
                    alt6=1;
                }


                switch (alt6) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:
                    if ( (this.input.LA(1)>='0' && this.input.LA(1)<='9')||(this.input.LA(1)>='A' && this.input.LA(1)<='Z')||this.input.LA(1)=='_'||(this.input.LA(1)>='a' && this.input.LA(1)<='z') ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    break loop6;
                }
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "DOTFUNCTIONID",

    // $ANTLR start COMMENT
    mCOMMENT: function()  {
        try {
            var _type = this.COMMENT;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:39:9: ( '//' (~ ( '\\n' ) )* '\\n' )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:39:11: '//' (~ ( '\\n' ) )* '\\n'
            this.match("//"); 

            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:39:16: (~ ( '\\n' ) )*
            loop7:
            do {
                var alt7=2;
                var LA7_0 = this.input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:39:18: ~ ( '\\n' )
                    if ( (this.input.LA(1)>='\u0000' && this.input.LA(1)<='\t')||(this.input.LA(1)>='\u000B' && this.input.LA(1)<='\uFFFF') ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;

                default :
                    break loop7;
                }
            } while (true);

            this.match('\n'); 



            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "COMMENT",

    // $ANTLR start INT
    mINT: function()  {
        try {
            var _type = this.INT;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:42:5: ( ( '0' .. '9' )+ )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:42:7: ( '0' .. '9' )+
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:42:7: ( '0' .. '9' )+
            var cnt8=0;
            loop8:
            do {
                var alt8=2;
                var LA8_0 = this.input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:42:7: '0' .. '9'
                    this.matchRange('0','9'); 


                    break;

                default :
                    if ( cnt8 >= 1 ) {
                        break loop8;
                    }
                        var eee = new org.antlr.runtime.EarlyExitException(8, this.input);
                        throw eee;
                }
                cnt8++;
            } while (true);




            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "INT",

    // $ANTLR start STRING
    mSTRING: function()  {
        try {
            var _type = this.STRING;
            var _channel = org.antlr.runtime.BaseRecognizer.DEFAULT_TOKEN_CHANNEL;
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:46:5: ( '\"' (~ ( '\\\\' | '\"' ) )* '\"' | '\\'' (~ ( '\\\\' | '\\'' ) )* '\\'' )
            var alt11=2;
            var LA11_0 = this.input.LA(1);

            if ( (LA11_0=='\"') ) {
                alt11=1;
            }
            else if ( (LA11_0=='\'') ) {
                alt11=2;
            }
            else {
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 11, 0, this.input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:46:8: '\"' (~ ( '\\\\' | '\"' ) )* '\"'
                    this.match('\"'); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:46:12: (~ ( '\\\\' | '\"' ) )*
                    loop9:
                    do {
                        var alt9=2;
                        var LA9_0 = this.input.LA(1);

                        if ( ((LA9_0>='\u0000' && LA9_0<='!')||(LA9_0>='#' && LA9_0<='[')||(LA9_0>=']' && LA9_0<='\uFFFF')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:46:14: ~ ( '\\\\' | '\"' )
                            if ( (this.input.LA(1)>='\u0000' && this.input.LA(1)<='!')||(this.input.LA(1)>='#' && this.input.LA(1)<='[')||(this.input.LA(1)>=']' && this.input.LA(1)<='\uFFFF') ) {
                                this.input.consume();

                            }
                            else {
                                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                                this.recover(mse);
                                throw mse;}



                            break;

                        default :
                            break loop9;
                        }
                    } while (true);

                    this.match('\"'); 


                    break;
                case 2 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:47:8: '\\'' (~ ( '\\\\' | '\\'' ) )* '\\''
                    this.match('\''); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:47:13: (~ ( '\\\\' | '\\'' ) )*
                    loop10:
                    do {
                        var alt10=2;
                        var LA10_0 = this.input.LA(1);

                        if ( ((LA10_0>='\u0000' && LA10_0<='&')||(LA10_0>='(' && LA10_0<='[')||(LA10_0>=']' && LA10_0<='\uFFFF')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                        case 1 :
                            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:47:15: ~ ( '\\\\' | '\\'' )
                            if ( (this.input.LA(1)>='\u0000' && this.input.LA(1)<='&')||(this.input.LA(1)>='(' && this.input.LA(1)<='[')||(this.input.LA(1)>=']' && this.input.LA(1)<='\uFFFF') ) {
                                this.input.consume();

                            }
                            else {
                                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                                this.recover(mse);
                                throw mse;}



                            break;

                        default :
                            break loop10;
                        }
                    } while (true);

                    this.match('\''); 


                    break;

            }
            this.state.type = _type;
            this.state.channel = _channel;
        }
        finally {
        }
    },
    // $ANTLR end "STRING",

    // $ANTLR start HEX_DIGIT
    mHEX_DIGIT: function()  {
        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:51:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:51:13: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            if ( (this.input.LA(1)>='0' && this.input.LA(1)<='9')||(this.input.LA(1)>='A' && this.input.LA(1)<='F')||(this.input.LA(1)>='a' && this.input.LA(1)<='f') ) {
                this.input.consume();

            }
            else {
                var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                this.recover(mse);
                throw mse;}




        }
        finally {
        }
    },
    // $ANTLR end "HEX_DIGIT",

    // $ANTLR start ESC_SEQ
    mESC_SEQ: function()  {
        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:55:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            var alt12=3;
            var LA12_0 = this.input.LA(1);

            if ( (LA12_0=='\\') ) {
                switch ( this.input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    alt12=1;
                    break;
                case 'u':
                    alt12=2;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    alt12=3;
                    break;
                default:
                    var nvae =
                        new org.antlr.runtime.NoViableAltException("", 12, 1, this.input);

                    throw nvae;
                }

            }
            else {
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 12, 0, this.input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:55:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    this.match('\\'); 
                    if ( this.input.LA(1)=='\"'||this.input.LA(1)=='\''||this.input.LA(1)=='\\'||this.input.LA(1)=='b'||this.input.LA(1)=='f'||this.input.LA(1)=='n'||this.input.LA(1)=='r'||this.input.LA(1)=='t' ) {
                        this.input.consume();

                    }
                    else {
                        var mse = new org.antlr.runtime.MismatchedSetException(null,this.input);
                        this.recover(mse);
                        throw mse;}



                    break;
                case 2 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:56:9: UNICODE_ESC
                    this.mUNICODE_ESC(); 


                    break;
                case 3 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:57:9: OCTAL_ESC
                    this.mOCTAL_ESC(); 


                    break;

            }
        }
        finally {
        }
    },
    // $ANTLR end "ESC_SEQ",

    // $ANTLR start OCTAL_ESC
    mOCTAL_ESC: function()  {
        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            var alt13=3;
            var LA13_0 = this.input.LA(1);

            if ( (LA13_0=='\\') ) {
                var LA13_1 = this.input.LA(2);

                if ( ((LA13_1>='0' && LA13_1<='3')) ) {
                    var LA13_2 = this.input.LA(3);

                    if ( ((LA13_2>='0' && LA13_2<='7')) ) {
                        var LA13_5 = this.input.LA(4);

                        if ( ((LA13_5>='0' && LA13_5<='7')) ) {
                            alt13=1;
                        }
                        else {
                            alt13=2;}
                    }
                    else {
                        alt13=3;}
                }
                else if ( ((LA13_1>='4' && LA13_1<='7')) ) {
                    var LA13_3 = this.input.LA(3);

                    if ( ((LA13_3>='0' && LA13_3<='7')) ) {
                        alt13=2;
                    }
                    else {
                        alt13=3;}
                }
                else {
                    var nvae =
                        new org.antlr.runtime.NoViableAltException("", 13, 1, this.input);

                    throw nvae;
                }
            }
            else {
                var nvae =
                    new org.antlr.runtime.NoViableAltException("", 13, 0, this.input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    this.match('\\'); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:14: ( '0' .. '3' )
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:15: '0' .. '3'
                    this.matchRange('0','3'); 



                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:25: ( '0' .. '7' )
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:26: '0' .. '7'
                    this.matchRange('0','7'); 



                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:36: ( '0' .. '7' )
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:62:37: '0' .. '7'
                    this.matchRange('0','7'); 





                    break;
                case 2 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:63:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    this.match('\\'); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:63:14: ( '0' .. '7' )
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:63:15: '0' .. '7'
                    this.matchRange('0','7'); 



                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:63:25: ( '0' .. '7' )
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:63:26: '0' .. '7'
                    this.matchRange('0','7'); 





                    break;
                case 3 :
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:64:9: '\\\\' ( '0' .. '7' )
                    this.match('\\'); 
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:64:14: ( '0' .. '7' )
                    // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:64:15: '0' .. '7'
                    this.matchRange('0','7'); 





                    break;

            }
        }
        finally {
        }
    },
    // $ANTLR end "OCTAL_ESC",

    // $ANTLR start UNICODE_ESC
    mUNICODE_ESC: function()  {
        try {
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:69:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:69:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            this.match('\\'); 
            this.match('u'); 
            this.mHEX_DIGIT(); 
            this.mHEX_DIGIT(); 
            this.mHEX_DIGIT(); 
            this.mHEX_DIGIT(); 



        }
        finally {
        }
    },
    // $ANTLR end "UNICODE_ESC",

    mTokens: function() {
        // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:8: ( T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | ID | SERVLETID | ATID | WS | FUNCTIONID | DOTFUNCTIONID | COMMENT | INT | STRING )
        var alt14=17;
        alt14 = this.dfa14.predict(this.input);
        switch (alt14) {
            case 1 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:10: T__17
                this.mT__17(); 


                break;
            case 2 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:16: T__18
                this.mT__18(); 


                break;
            case 3 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:22: T__19
                this.mT__19(); 


                break;
            case 4 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:28: T__20
                this.mT__20(); 


                break;
            case 5 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:34: T__21
                this.mT__21(); 


                break;
            case 6 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:40: T__22
                this.mT__22(); 


                break;
            case 7 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:46: T__23
                this.mT__23(); 


                break;
            case 8 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:52: T__24
                this.mT__24(); 


                break;
            case 9 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:58: ID
                this.mID(); 


                break;
            case 10 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:61: SERVLETID
                this.mSERVLETID(); 


                break;
            case 11 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:71: ATID
                this.mATID(); 


                break;
            case 12 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:76: WS
                this.mWS(); 


                break;
            case 13 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:79: FUNCTIONID
                this.mFUNCTIONID(); 


                break;
            case 14 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:90: DOTFUNCTIONID
                this.mDOTFUNCTIONID(); 


                break;
            case 15 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:104: COMMENT
                this.mCOMMENT(); 


                break;
            case 16 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:112: INT
                this.mINT(); 


                break;
            case 17 :
                // \\\\2-research-pw\\share\\levrage\\js\\resolverv2js.g:1:116: STRING
                this.mSTRING(); 


                break;

        }

    }

}, true); // important to pass true to overwrite default implementations

org.antlr.lang.augmentObject(resolverv2jsLexer, {
    DFA14_eotS:
        "\u0005\uffff\u0001\u000c\u000e\uffff",
    DFA14_eofS:
        "\u0014\uffff",
    DFA14_minS:
        "\u0001\u0009\u0004\uffff\u0001\u002d\u0004\uffff\u0001\u002f\u0009"+
    "\uffff",
    DFA14_maxS:
        "\u0001\u007d\u0004\uffff\u0001\u002d\u0004\uffff\u0001\u007a\u0009"+
    "\uffff",
    DFA14_acceptS:
        "\u0001\uffff\u0001\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0001"+
    "\uffff\u0001\u0006\u0001\u0007\u0001\u0008\u0001\u0009\u0001\uffff\u0001"+
    "\u000b\u0001\u000c\u0001\u000d\u0001\u000e\u0001\u0010\u0001\u0011\u0001"+
    "\u0005\u0001\u000a\u0001\u000f",
    DFA14_specialS:
        "\u0014\uffff}>",
    DFA14_transitionS: [
            "\u0002\u000c\u0002\uffff\u0001\u000c\u0012\uffff\u0001\u0005"+
            "\u0001\uffff\u0001\u0010\u0001\u000d\u0003\uffff\u0001\u0010"+
            "\u0001\u0006\u0001\u0007\u0002\uffff\u0001\u0008\u0001\uffff"+
            "\u0001\u000e\u0001\u000a\u000a\u000f\u0001\uffff\u0001\u0001"+
            "\u0001\uffff\u0001\u0002\u0002\uffff\u0001\u000b\u001a\u0009"+
            "\u0004\uffff\u0001\u0009\u0001\uffff\u001a\u0009\u0001\u0003"+
            "\u0001\uffff\u0001\u0004",
            "",
            "",
            "",
            "",
            "\u0001\u0011",
            "",
            "",
            "",
            "",
            "\u0001\u0013\u0011\uffff\u001a\u0012\u0004\uffff\u0001\u0012"+
            "\u0001\uffff\u001a\u0012",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    ]
});

org.antlr.lang.augmentObject(resolverv2jsLexer, {
    DFA14_eot:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsLexer.DFA14_eotS),
    DFA14_eof:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsLexer.DFA14_eofS),
    DFA14_min:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(resolverv2jsLexer.DFA14_minS),
    DFA14_max:
        org.antlr.runtime.DFA.unpackEncodedStringToUnsignedChars(resolverv2jsLexer.DFA14_maxS),
    DFA14_accept:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsLexer.DFA14_acceptS),
    DFA14_special:
        org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsLexer.DFA14_specialS),
    DFA14_transition: (function() {
        var a = [],
            i,
            numStates = resolverv2jsLexer.DFA14_transitionS.length;
        for (i=0; i<numStates; i++) {
            a.push(org.antlr.runtime.DFA.unpackEncodedString(resolverv2jsLexer.DFA14_transitionS[i]));
        }
        return a;
    })()
});

resolverv2jsLexer.DFA14 = function(recognizer) {
    this.recognizer = recognizer;
    this.decisionNumber = 14;
    this.eot = resolverv2jsLexer.DFA14_eot;
    this.eof = resolverv2jsLexer.DFA14_eof;
    this.min = resolverv2jsLexer.DFA14_min;
    this.max = resolverv2jsLexer.DFA14_max;
    this.accept = resolverv2jsLexer.DFA14_accept;
    this.special = resolverv2jsLexer.DFA14_special;
    this.transition = resolverv2jsLexer.DFA14_transition;
};

org.antlr.lang.extend(resolverv2jsLexer.DFA14, org.antlr.runtime.DFA, {
    getDescription: function() {
        return "1:1: Tokens : ( T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | ID | SERVLETID | ATID | WS | FUNCTIONID | DOTFUNCTIONID | COMMENT | INT | STRING );";
    },
    dummy: null
});
 
})();