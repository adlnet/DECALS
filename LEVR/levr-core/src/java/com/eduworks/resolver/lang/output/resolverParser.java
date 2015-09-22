// $ANTLR 3.4 src\\java\\com\\eduworks\\resolver\\lang\\resolver.g 2012-03-02 12:34:50

package com.eduworks.resolver.lang.output;

import java.util.Iterator;
import java.util.Stack;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwMap;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class resolverParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMMENT", "ESC_SEQ", "FUNCTIONID", "HEX_DIGIT", "ID", "INT", "OCTAL_ESC", "STRING", "UNICODE_ESC", "WS", "'('", "')'", "','", "'->'", "'/'", "';'", "'='", "'{'", "'}'"
    };

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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public resolverParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public resolverParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return resolverParser.tokenNames; }
    public String getGrammarFileName() { return "src\\java\\com\\eduworks\\resolver\\lang\\resolver.g"; }


    	public boolean debug = false;
    	public EwMap<String,JSONObject> ids = new EwMap<String,JSONObject>();
    	public EwMap<String,JSONObject> servlets = new EwMap<String,JSONObject>();
    	public JSONObject obj = new JSONObject();
    	public Stack<JSONObject> stk = new Stack<JSONObject>();



    // $ANTLR start "parse"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:66:1: parse : ( ( decl ';' ( WS )? ) ( parse )? | COMMENT ( WS )? ( parse )? | EOF );
    public final void parse() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:3: ( ( decl ';' ( WS )? ) ( parse )? | COMMENT ( WS )? ( parse )? | EOF )
            int alt5=3;
            switch ( input.LA(1) ) {
            case ID:
            case 18:
                {
                alt5=1;
                }
                break;
            case COMMENT:
                {
                alt5=2;
                }
                break;
            case EOF:
                {
                alt5=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }

            switch (alt5) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:5: ( decl ';' ( WS )? ) ( parse )?
                    {
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:5: ( decl ';' ( WS )? )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:6: decl ';' ( WS )?
                    {
                    pushFollow(FOLLOW_decl_in_parse454);
                    decl();

                    state._fsp--;


                    match(input,19,FOLLOW_19_in_parse455); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:13: ( WS )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==WS) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:13: WS
                            {
                            match(input,WS,FOLLOW_WS_in_parse456); 

                            }
                            break;

                    }


                    }


                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:18: ( parse )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==EOF||LA2_0==COMMENT||LA2_0==ID||LA2_0==18) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:67:18: parse
                            {
                            pushFollow(FOLLOW_parse_in_parse460);
                            parse();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:68:5: COMMENT ( WS )? ( parse )?
                    {
                    match(input,COMMENT,FOLLOW_COMMENT_in_parse467); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:68:13: ( WS )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==WS) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:68:13: WS
                            {
                            match(input,WS,FOLLOW_WS_in_parse469); 

                            }
                            break;

                    }


                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:68:17: ( parse )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==EOF||LA4_0==COMMENT||LA4_0==ID||LA4_0==18) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:68:17: parse
                            {
                            pushFollow(FOLLOW_parse_in_parse472);
                            parse();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:69:5: EOF
                    {
                    match(input,EOF,FOLLOW_EOF_in_parse479); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "parse"



    // $ANTLR start "decl"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:72:1: decl : (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | '/' s= ID ( WS )? '->' ( WS )? i= ID );
    public final void decl() throws RecognitionException {
        Token i=null;
        Token s=null;

        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:73:2: (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | '/' s= ID ( WS )? '->' ( WS )? i= ID )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ID) ) {
                alt11=1;
            }
            else if ( (LA11_0==18) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:73:4: i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' )
                    {
                    i=(Token)match(input,ID,FOLLOW_ID_in_decl492); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:73:9: ( WS )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==WS) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:73:9: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl494); 

                            }
                            break;

                    }


                    match(input,20,FOLLOW_20_in_decl496); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:73:15: ( WS )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==WS) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:73:15: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl497); 

                            }
                            break;

                    }


                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:74:3: ( functioncall | '{' param '}' )
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==FUNCTIONID) ) {
                        alt8=1;
                    }
                    else if ( (LA8_0==21) ) {
                        alt8=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 0, input);

                        throw nvae;

                    }
                    switch (alt8) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:74:5: functioncall
                            {
                            pushFollow(FOLLOW_functioncall_in_decl504);
                            functioncall();

                            state._fsp--;


                            ids.put(i.getText(),obj);obj = new JSONObject();

                            }
                            break;
                        case 2 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:75:5: '{' param '}'
                            {
                            match(input,21,FOLLOW_21_in_decl515); 

                            pushFollow(FOLLOW_param_in_decl516);
                            param();

                            state._fsp--;


                            match(input,22,FOLLOW_22_in_decl517); 

                            ids.put(i.getText(),obj);obj = new JSONObject();

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:77:4: '/' s= ID ( WS )? '->' ( WS )? i= ID
                    {
                    match(input,18,FOLLOW_18_in_decl531); 

                    s=(Token)match(input,ID,FOLLOW_ID_in_decl534); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:77:12: ( WS )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==WS) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:77:12: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl536); 

                            }
                            break;

                    }


                    match(input,17,FOLLOW_17_in_decl539); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:77:21: ( WS )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==WS) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:77:21: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl541); 

                            }
                            break;

                    }


                    i=(Token)match(input,ID,FOLLOW_ID_in_decl546); 

                    servlets.put(s.getText(),ids.get(i.getText()));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "decl"



    // $ANTLR start "functioncall"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:81:1: functioncall : x= FUNCTIONID ( WS )? '(' param ')' ;
    public final void functioncall() throws RecognitionException {
        Token x=null;

        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:82:2: (x= FUNCTIONID ( WS )? '(' param ')' )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:82:4: x= FUNCTIONID ( WS )? '(' param ')'
            {
            x=(Token)match(input,FUNCTIONID,FOLLOW_FUNCTIONID_in_functioncall564); 

            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:82:17: ( WS )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==WS) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:82:17: WS
                    {
                    match(input,WS,FOLLOW_WS_in_functioncall566); 

                    }
                    break;

            }


            match(input,14,FOLLOW_14_in_functioncall568); 

            try{obj.put("function",x.getText().substring(1));}catch(JSONException e){}

            pushFollow(FOLLOW_param_in_functioncall574);
            param();

            state._fsp--;


            match(input,15,FOLLOW_15_in_functioncall575); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "functioncall"



    // $ANTLR start "param"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:84:1: param : ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' |y= STRING |y= INT |y= ID ) |x= ID ) ( ( WS )? ',' param )? ( WS )? ;
    public final void param() throws RecognitionException {
        Token x=null;
        Token y=null;

        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:2: ( ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' |y= STRING |y= INT |y= ID ) |x= ID ) ( ( WS )? ',' param )? ( WS )? )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:4: ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' |y= STRING |y= INT |y= ID ) |x= ID ) ( ( WS )? ',' param )? ( WS )?
            {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:4: ( WS )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==WS) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:4: WS
                    {
                    match(input,WS,FOLLOW_WS_in_param585); 

                    }
                    break;

            }


            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:8: (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' |y= STRING |y= INT |y= ID ) |x= ID )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==ID) ) {
                switch ( input.LA(2) ) {
                case WS:
                    {
                    int LA18_2 = input.LA(3);

                    if ( (LA18_2==20) ) {
                        alt18=1;
                    }
                    else if ( (LA18_2==WS||(LA18_2 >= 15 && LA18_2 <= 16)||LA18_2==22) ) {
                        alt18=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 2, input);

                        throw nvae;

                    }
                    }
                    break;
                case 20:
                    {
                    alt18=1;
                    }
                    break;
                case 15:
                case 16:
                case 22:
                    {
                    alt18=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }
            switch (alt18) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:9: x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' |y= STRING |y= INT |y= ID )
                    {
                    x=(Token)match(input,ID,FOLLOW_ID_in_param591); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:14: ( WS )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==WS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:14: WS
                            {
                            match(input,WS,FOLLOW_WS_in_param593); 

                            }
                            break;

                    }


                    match(input,20,FOLLOW_20_in_param596); 

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:22: ( WS )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==WS) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:85:22: WS
                            {
                            match(input,WS,FOLLOW_WS_in_param598); 

                            }
                            break;

                    }


                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:86:5: (y= FUNCTIONID ( WS )? '(' param ')' |y= STRING |y= INT |y= ID )
                    int alt17=4;
                    switch ( input.LA(1) ) {
                    case FUNCTIONID:
                        {
                        alt17=1;
                        }
                        break;
                    case STRING:
                        {
                        alt17=2;
                        }
                        break;
                    case INT:
                        {
                        alt17=3;
                        }
                        break;
                    case ID:
                        {
                        alt17=4;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 0, input);

                        throw nvae;

                    }

                    switch (alt17) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:87:6: y= FUNCTIONID ( WS )? '(' param ')'
                            {
                            y=(Token)match(input,FUNCTIONID,FOLLOW_FUNCTIONID_in_param614); 

                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:87:19: ( WS )?
                            int alt16=2;
                            int LA16_0 = input.LA(1);

                            if ( (LA16_0==WS) ) {
                                alt16=1;
                            }
                            switch (alt16) {
                                case 1 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:87:19: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_param616); 

                                    }
                                    break;

                            }


                            match(input,14,FOLLOW_14_in_param618); 

                            try{stk.push(obj);obj = new JSONObject();obj.put("function",y.getText().substring(1));}catch(JSONException e){}

                            pushFollow(FOLLOW_param_in_param629);
                            param();

                            state._fsp--;


                            try{JSONObject jo = obj;obj=stk.pop();obj.put(x.getText(),jo);}catch(JSONException e){}

                            match(input,15,FOLLOW_15_in_param642); 

                            }
                            break;
                        case 2 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:90:6: y= STRING
                            {
                            y=(Token)match(input,STRING,FOLLOW_STRING_in_param651); 

                            try{obj.put(x.getText(),y.getText().substring(1,y.getText().length()-1));}catch(JSONException e){}

                            }
                            break;
                        case 3 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:91:6: y= INT
                            {
                            y=(Token)match(input,INT,FOLLOW_INT_in_param666); 

                            try{obj.put(x.getText(),y.getText().substring(1,y.getText().length()-1));}catch(JSONException e){}

                            }
                            break;
                        case 4 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:92:6: y= ID
                            {
                            y=(Token)match(input,ID,FOLLOW_ID_in_param681); 

                            try{obj.put(x.getText(),ids.get(y.getText()));}catch(JSONException e){}

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:94:4: x= ID
                    {
                    x=(Token)match(input,ID,FOLLOW_ID_in_param699); 

                    				try{JSONObject jo = ids.get(x.getText());
                    						Iterator<String> it = jo.keys();
                    						while(it.hasNext())
                    						{
                    							String s = it.next();
                    							obj.put(s,jo.get(s));
                    						}}catch(JSONException e){}
                    			

                    }
                    break;

            }


            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:103:2: ( ( WS )? ',' param )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==WS) ) {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==16) ) {
                    alt20=1;
                }
            }
            else if ( (LA20_0==16) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:103:4: ( WS )? ',' param
                    {
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:103:4: ( WS )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==WS) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:103:4: WS
                            {
                            match(input,WS,FOLLOW_WS_in_param709); 

                            }
                            break;

                    }


                    match(input,16,FOLLOW_16_in_param712); 

                    pushFollow(FOLLOW_param_in_param714);
                    param();

                    state._fsp--;


                    }
                    break;

            }


            // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:103:20: ( WS )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==WS) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolver.g:103:20: WS
                    {
                    match(input,WS,FOLLOW_WS_in_param718); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "param"

    // Delegated rules


 

    public static final BitSet FOLLOW_decl_in_parse454 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_parse455 = new BitSet(new long[]{0x0000000000042112L});
    public static final BitSet FOLLOW_WS_in_parse456 = new BitSet(new long[]{0x0000000000040112L});
    public static final BitSet FOLLOW_parse_in_parse460 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMENT_in_parse467 = new BitSet(new long[]{0x0000000000042112L});
    public static final BitSet FOLLOW_WS_in_parse469 = new BitSet(new long[]{0x0000000000040112L});
    public static final BitSet FOLLOW_parse_in_parse472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EOF_in_parse479 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_decl492 = new BitSet(new long[]{0x0000000000102000L});
    public static final BitSet FOLLOW_WS_in_decl494 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_decl496 = new BitSet(new long[]{0x0000000000202040L});
    public static final BitSet FOLLOW_WS_in_decl497 = new BitSet(new long[]{0x0000000000200040L});
    public static final BitSet FOLLOW_functioncall_in_decl504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_decl515 = new BitSet(new long[]{0x0000000000002100L});
    public static final BitSet FOLLOW_param_in_decl516 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_decl517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_decl531 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_decl534 = new BitSet(new long[]{0x0000000000022000L});
    public static final BitSet FOLLOW_WS_in_decl536 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_decl539 = new BitSet(new long[]{0x0000000000002100L});
    public static final BitSet FOLLOW_WS_in_decl541 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_decl546 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTIONID_in_functioncall564 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_WS_in_functioncall566 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_functioncall568 = new BitSet(new long[]{0x0000000000002100L});
    public static final BitSet FOLLOW_param_in_functioncall574 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_functioncall575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_param585 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_ID_in_param591 = new BitSet(new long[]{0x0000000000102000L});
    public static final BitSet FOLLOW_WS_in_param593 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_param596 = new BitSet(new long[]{0x0000000000002B40L});
    public static final BitSet FOLLOW_WS_in_param598 = new BitSet(new long[]{0x0000000000000B40L});
    public static final BitSet FOLLOW_FUNCTIONID_in_param614 = new BitSet(new long[]{0x0000000000006000L});
    public static final BitSet FOLLOW_WS_in_param616 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_param618 = new BitSet(new long[]{0x0000000000002100L});
    public static final BitSet FOLLOW_param_in_param629 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_param642 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_STRING_in_param651 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_INT_in_param666 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_ID_in_param681 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_ID_in_param699 = new BitSet(new long[]{0x0000000000012002L});
    public static final BitSet FOLLOW_WS_in_param709 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_param712 = new BitSet(new long[]{0x0000000000002100L});
    public static final BitSet FOLLOW_param_in_param714 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_WS_in_param718 = new BitSet(new long[]{0x0000000000000002L});

}