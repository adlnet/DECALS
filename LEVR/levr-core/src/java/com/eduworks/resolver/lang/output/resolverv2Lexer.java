// $ANTLR 3.1.1 src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g 2014-04-17 15:57:21

package com.eduworks.resolver.lang.output;



import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class resolverv2Lexer extends Lexer {
    public static final int SERVLETID=5;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int FUNCTIONID=8;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int UNICODE_ESC=14;
    public static final int T__20=20;
    public static final int OCTAL_ESC=15;
    public static final int HEX_DIGIT=13;
    public static final int INT=11;
    public static final int ATID=6;
    public static final int ID=4;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int WS=7;
    public static final int ESC_SEQ=16;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int DOTFUNCTIONID=9;
    public static final int COMMENT=10;
    public static final int STRING=12;

    // delegates
    // delegators

    public resolverv2Lexer() {;} 
    public resolverv2Lexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public resolverv2Lexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g"; }

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:8:7: ( ';' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:8:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:9:7: ( '=' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:9:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:10:7: ( '{' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:10:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:11:7: ( '}' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:11:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:12:7: ( ' ->' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:12:9: ' ->'
            {
            match(" ->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:13:7: ( '(' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:13:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:14:7: ( ')' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:14:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:15:7: ( ',' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:15:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:27:5: ( ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' )* )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:27:7: ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:27:30: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='-'||(LA1_0>='0' && LA1_0<=':')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<=':')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "SERVLETID"
    public final void mSERVLETID() throws RecognitionException {
        try {
            int _type = SERVLETID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:30:12: ( '/' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' | '/' )* )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:30:14: '/' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' | '/' )*
            {
            match('/'); 
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:30:40: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' | ':' | '/' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='-'||(LA2_0>='/' && LA2_0<=':')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='/' && input.LA(1)<=':')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SERVLETID"

    // $ANTLR start "ATID"
    public final void mATID() throws RecognitionException {
        try {
            int _type = ATID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:33:6: ( '@' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:33:8: '@' ( 'A' .. 'Z' | 'a' .. 'z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            match('@'); 
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:33:34: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ATID"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:36:5: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:36:7: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:36:7: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\t' && LA4_0<='\n')||LA4_0=='\r'||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "FUNCTIONID"
    public final void mFUNCTIONID() throws RecognitionException {
        try {
            int _type = FUNCTIONID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:39:13: ( '#' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:39:15: '#' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            match('#'); 
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:39:18: ( 'a' .. 'z' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:39:19: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:39:28: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')||(LA5_0>='A' && LA5_0<='Z')||LA5_0=='_'||(LA5_0>='a' && LA5_0<='z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FUNCTIONID"

    // $ANTLR start "DOTFUNCTIONID"
    public final void mDOTFUNCTIONID() throws RecognitionException {
        try {
            int _type = DOTFUNCTIONID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:42:16: ( '.' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:42:18: '.' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            match('.'); 
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:42:21: ( 'a' .. 'z' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:42:22: 'a' .. 'z'
            {
            matchRange('a','z'); 

            }

            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:42:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')||(LA6_0>='A' && LA6_0<='Z')||LA6_0=='_'||(LA6_0>='a' && LA6_0<='z')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOTFUNCTIONID"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:45:9: ( '//' (~ ( '\\n' ) )* '\\n' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:45:11: '//' (~ ( '\\n' ) )* '\\n'
            {
            match("//"); 

            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:45:16: (~ ( '\\n' ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:45:18: ~ ( '\\n' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:48:5: ( ( '0' .. '9' )+ )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:48:7: ( '0' .. '9' )+
            {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:48:7: ( '0' .. '9' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:48:7: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:52:5: ( '\"' (~ ( '\"' ) )* '\"' | '\\'' (~ ( '\\'' ) )* '\\'' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\"') ) {
                alt11=1;
            }
            else if ( (LA11_0=='\'') ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:52:8: '\"' (~ ( '\"' ) )* '\"'
                    {
                    match('\"'); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:52:12: (~ ( '\"' ) )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>='\u0000' && LA9_0<='!')||(LA9_0>='#' && LA9_0<='\uFFFF')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:52:14: ~ ( '\"' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:53:8: '\\'' (~ ( '\\'' ) )* '\\''
                    {
                    match('\''); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:53:13: (~ ( '\\'' ) )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0>='\u0000' && LA10_0<='&')||(LA10_0>='(' && LA10_0<='\uFFFF')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:53:15: ~ ( '\\'' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:57:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:57:13: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:61:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt12=3;
            int LA12_0 = input.LA(1);

            if ( (LA12_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt12=1;
                    }
                    break;
                case 'u':
                    {
                    alt12=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt12=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:61:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 
                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:62:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 

                    }
                    break;
                case 3 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:63:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt13=3;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='\\') ) {
                int LA13_1 = input.LA(2);

                if ( ((LA13_1>='0' && LA13_1<='3')) ) {
                    int LA13_2 = input.LA(3);

                    if ( ((LA13_2>='0' && LA13_2<='7')) ) {
                        int LA13_5 = input.LA(4);

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
                    int LA13_3 = input.LA(3);

                    if ( ((LA13_3>='0' && LA13_3<='7')) ) {
                        alt13=2;
                    }
                    else {
                        alt13=3;}
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:14: ( '0' .. '3' )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:15: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:25: ( '0' .. '7' )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:36: ( '0' .. '7' )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:68:37: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:69:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:69:14: ( '0' .. '7' )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:69:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:69:25: ( '0' .. '7' )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:69:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:70:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:70:14: ( '0' .. '7' )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:70:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:75:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:75:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 
            match('u'); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:8: ( T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | ID | SERVLETID | ATID | WS | FUNCTIONID | DOTFUNCTIONID | COMMENT | INT | STRING )
        int alt14=17;
        alt14 = dfa14.predict(input);
        switch (alt14) {
            case 1 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:10: T__17
                {
                mT__17(); 

                }
                break;
            case 2 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:16: T__18
                {
                mT__18(); 

                }
                break;
            case 3 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:22: T__19
                {
                mT__19(); 

                }
                break;
            case 4 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:28: T__20
                {
                mT__20(); 

                }
                break;
            case 5 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:34: T__21
                {
                mT__21(); 

                }
                break;
            case 6 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:40: T__22
                {
                mT__22(); 

                }
                break;
            case 7 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:46: T__23
                {
                mT__23(); 

                }
                break;
            case 8 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:52: T__24
                {
                mT__24(); 

                }
                break;
            case 9 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:58: ID
                {
                mID(); 

                }
                break;
            case 10 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:61: SERVLETID
                {
                mSERVLETID(); 

                }
                break;
            case 11 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:71: ATID
                {
                mATID(); 

                }
                break;
            case 12 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:76: WS
                {
                mWS(); 

                }
                break;
            case 13 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:79: FUNCTIONID
                {
                mFUNCTIONID(); 

                }
                break;
            case 14 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:90: DOTFUNCTIONID
                {
                mDOTFUNCTIONID(); 

                }
                break;
            case 15 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:104: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 16 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:112: INT
                {
                mINT(); 

                }
                break;
            case 17 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:1:116: STRING
                {
                mSTRING(); 

                }
                break;

        }

    }


    protected DFA14 dfa14 = new DFA14(this);
    static final String DFA14_eotS =
        "\5\uffff\1\14\16\uffff";
    static final String DFA14_eofS =
        "\24\uffff";
    static final String DFA14_minS =
        "\1\11\4\uffff\1\55\4\uffff\1\57\11\uffff";
    static final String DFA14_maxS =
        "\1\175\4\uffff\1\55\4\uffff\1\172\11\uffff";
    static final String DFA14_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\uffff\1\6\1\7\1\10\1\11\1\uffff\1\13"+
        "\1\14\1\15\1\16\1\20\1\21\1\5\1\12\1\17";
    static final String DFA14_specialS =
        "\24\uffff}>";
    static final String[] DFA14_transitionS = {
            "\2\14\2\uffff\1\14\22\uffff\1\5\1\uffff\1\20\1\15\3\uffff\1"+
            "\20\1\6\1\7\2\uffff\1\10\1\uffff\1\16\1\12\12\17\1\uffff\1\1"+
            "\1\uffff\1\2\2\uffff\1\13\32\11\4\uffff\1\11\1\uffff\32\11\1"+
            "\3\1\uffff\1\4",
            "",
            "",
            "",
            "",
            "\1\21",
            "",
            "",
            "",
            "",
            "\1\23\21\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA14_eot = DFA.unpackEncodedString(DFA14_eotS);
    static final short[] DFA14_eof = DFA.unpackEncodedString(DFA14_eofS);
    static final char[] DFA14_min = DFA.unpackEncodedStringToUnsignedChars(DFA14_minS);
    static final char[] DFA14_max = DFA.unpackEncodedStringToUnsignedChars(DFA14_maxS);
    static final short[] DFA14_accept = DFA.unpackEncodedString(DFA14_acceptS);
    static final short[] DFA14_special = DFA.unpackEncodedString(DFA14_specialS);
    static final short[][] DFA14_transition;

    static {
        int numStates = DFA14_transitionS.length;
        DFA14_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA14_transition[i] = DFA.unpackEncodedString(DFA14_transitionS[i]);
        }
    }

    class DFA14 extends DFA {

        public DFA14(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 14;
            this.eot = DFA14_eot;
            this.eof = DFA14_eof;
            this.min = DFA14_min;
            this.max = DFA14_max;
            this.accept = DFA14_accept;
            this.special = DFA14_special;
            this.transition = DFA14_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | ID | SERVLETID | ATID | WS | FUNCTIONID | DOTFUNCTIONID | COMMENT | INT | STRING );";
        }
    }
 

}