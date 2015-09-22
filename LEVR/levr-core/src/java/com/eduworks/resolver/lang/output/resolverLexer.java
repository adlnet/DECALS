// $ANTLR 3.4 src\\java\\com\\eduworks\\resolver\\lang\\resolver.g 2012-03-02 12:34:50

package com.eduworks.resolver.lang.output;



import org.antlr.runtime.CharStream;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class resolverLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__14=14;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__19=19;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__22=22;
    public static final int COMMENT=4;
    public static final int ESC_SEQ=5;
    public static final int FUNCTIONID=6;
    public static final int HEX_DIGIT=7;
    public static final int ID=8;
    public static final int INT=9;
    public static final int OCTAL_ESC=10;
    public static final int STRING=11;
    public static final int UNICODE_ESC=12;
    public static final int WS=13;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public resolverLexer() {} 
    public resolverLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public resolverLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "src\\java\\com\\eduworks\\resolver\\lang\\resolver.g"; }

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:7:7: ( '(' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:7:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:8:7: ( ')' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:8:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:9:7: ( ',' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:9:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:10:7: ( '->' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:10:9: '->'
            {
            match("->"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:11:7: ( '/' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:11:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:12:7: ( ';' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:12:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:13:7: ( '=' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:13:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:14:7: ( '{' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:14:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:15:7: ( '}' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:15:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:25:5: ( ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '[' ) ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' )* | ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' | '-' )* ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' ) ) )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:25:7: ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '[' ) ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' )* | ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' | '-' )* ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' ) )
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= '[')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:25:34: ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' )* | ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' | '-' )* ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' ) )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=2;
            }
            else {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:25:35: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' )*
                    {
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:25:35: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' )*
                    loop1:
                    do {
                        int alt1=2;
                        int LA1_0 = input.LA(1);

                        if ( (LA1_0=='.'||(LA1_0 >= '0' && LA1_0 <= ':')||(LA1_0 >= 'A' && LA1_0 <= '[')||LA1_0==']'||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                            alt1=1;
                        }


                        switch (alt1) {
                    	case 1 :
                    	    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
                    	    {
                    	    if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= '[')||input.LA(1)==']'||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop1;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:25:85: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' | '-' )* ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' )
                    {
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:25:85: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | ':' | '.' | '[' | ']' | '-' )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0=='.'||(LA2_0 >= '0' && LA2_0 <= ':')||(LA2_0 >= 'A' && LA2_0 <= '[')||LA2_0==']'||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
                            int LA2_1 = input.LA(2);

                            if ( ((LA2_1 >= '-' && LA2_1 <= '.')||(LA2_1 >= '0' && LA2_1 <= ':')||(LA2_1 >= 'A' && LA2_1 <= '[')||LA2_1==']'||LA2_1=='_'||(LA2_1 >= 'a' && LA2_1 <= 'z')) ) {
                                alt2=1;
                            }


                        }
                        else if ( (LA2_0=='-') ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
                    	    {
                    	    if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= '[')||input.LA(1)==']'||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                    	        input.consume();
                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop2;
                        }
                    } while (true);


                    if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= '[')||input.LA(1)==']'||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:28:5: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:28:7: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:28:7: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '\t' && LA4_0 <= '\n')||LA4_0=='\r'||LA4_0==' ') ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
            	    {
            	    if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "FUNCTIONID"
    public final void mFUNCTIONID() throws RecognitionException {
        try {
            int _type = FUNCTIONID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:31:13: ( '#' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:31:15: '#' ( 'a' .. 'z' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            match('#'); 

            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:31:28: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '0' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FUNCTIONID"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:34:9: ( '//' (~ ( '\\n' ) )* '\\n' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:34:11: '//' (~ ( '\\n' ) )* '\\n'
            {
            match("//"); 



            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:34:16: (~ ( '\\n' ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0 >= '\u0000' && LA6_0 <= '\t')||(LA6_0 >= '\u000B' && LA6_0 <= '\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:37:5: ( ( '0' .. '9' )+ )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:37:7: ( '0' .. '9' )+
            {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:37:7: ( '0' .. '9' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:41:5: ( '\"' (~ ( '\\\\' | '\"' ) )* '\"' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:41:8: '\"' (~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 

            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:41:12: (~ ( '\\\\' | '\"' ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0 >= '\u0000' && LA8_0 <= '!')||(LA8_0 >= '#' && LA8_0 <= '[')||(LA8_0 >= ']' && LA8_0 <= '\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:46:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:50:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt9=3;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='\\') ) {
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
                    alt9=1;
                    }
                    break;
                case 'u':
                    {
                    alt9=2;
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
                    alt9=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:50:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 

                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:51:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:52:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:57:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt10=3;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='\\') ) {
                int LA10_1 = input.LA(2);

                if ( ((LA10_1 >= '0' && LA10_1 <= '3')) ) {
                    int LA10_2 = input.LA(3);

                    if ( ((LA10_2 >= '0' && LA10_2 <= '7')) ) {
                        int LA10_4 = input.LA(4);

                        if ( ((LA10_4 >= '0' && LA10_4 <= '7')) ) {
                            alt10=1;
                        }
                        else {
                            alt10=2;
                        }
                    }
                    else {
                        alt10=3;
                    }
                }
                else if ( ((LA10_1 >= '4' && LA10_1 <= '7')) ) {
                    int LA10_3 = input.LA(3);

                    if ( ((LA10_3 >= '0' && LA10_3 <= '7')) ) {
                        alt10=2;
                    }
                    else {
                        alt10=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 10, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:57:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '3') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:58:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 3 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:59:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:64:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:64:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
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
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:8: ( T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | ID | WS | FUNCTIONID | COMMENT | INT | STRING )
        int alt11=15;
        switch ( input.LA(1) ) {
        case '(':
            {
            alt11=1;
            }
            break;
        case ')':
            {
            alt11=2;
            }
            break;
        case ',':
            {
            alt11=3;
            }
            break;
        case '-':
            {
            alt11=4;
            }
            break;
        case '/':
            {
            int LA11_5 = input.LA(2);

            if ( (LA11_5=='/') ) {
                alt11=13;
            }
            else {
                alt11=5;
            }
            }
            break;
        case ';':
            {
            alt11=6;
            }
            break;
        case '=':
            {
            alt11=7;
            }
            break;
        case '{':
            {
            alt11=8;
            }
            break;
        case '}':
            {
            alt11=9;
            }
            break;
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '[':
        case '_':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt11=10;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt11=11;
            }
            break;
        case '#':
            {
            alt11=12;
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
        case '8':
        case '9':
            {
            alt11=14;
            }
            break;
        case '\"':
            {
            alt11=15;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 11, 0, input);

            throw nvae;

        }

        switch (alt11) {
            case 1 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:10: T__14
                {
                mT__14(); 


                }
                break;
            case 2 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:16: T__15
                {
                mT__15(); 


                }
                break;
            case 3 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:22: T__16
                {
                mT__16(); 


                }
                break;
            case 4 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:28: T__17
                {
                mT__17(); 


                }
                break;
            case 5 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:34: T__18
                {
                mT__18(); 


                }
                break;
            case 6 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:40: T__19
                {
                mT__19(); 


                }
                break;
            case 7 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:46: T__20
                {
                mT__20(); 


                }
                break;
            case 8 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:52: T__21
                {
                mT__21(); 


                }
                break;
            case 9 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:58: T__22
                {
                mT__22(); 


                }
                break;
            case 10 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:64: ID
                {
                mID(); 


                }
                break;
            case 11 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:67: WS
                {
                mWS(); 


                }
                break;
            case 12 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:70: FUNCTIONID
                {
                mFUNCTIONID(); 


                }
                break;
            case 13 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:81: COMMENT
                {
                mCOMMENT(); 


                }
                break;
            case 14 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:89: INT
                {
                mINT(); 


                }
                break;
            case 15 :
                // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:1:93: STRING
                {
                mSTRING(); 


                }
                break;

        }

    }


 

}