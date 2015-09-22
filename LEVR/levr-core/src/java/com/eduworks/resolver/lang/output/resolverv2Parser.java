// $ANTLR 3.1.1 src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g 2014-04-17 15:57:20

package com.eduworks.resolver.lang.output;

import java.util.Iterator;
import java.util.Stack;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.json.JSONException;
import org.json.JSONObject;

import com.eduworks.lang.EwMap;
import com.eduworks.lang.json.impl.EwJsonObject;

public class resolverv2Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ID", "SERVLETID", "ATID", "WS", "FUNCTIONID", "DOTFUNCTIONID", "COMMENT", "INT", "STRING", "HEX_DIGIT", "UNICODE_ESC", "OCTAL_ESC", "ESC_SEQ", "';'", "'='", "'{'", "'}'", "' ->'", "'('", "')'", "','"
    };
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
    public static final int ESC_SEQ=16;
    public static final int WS=7;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int DOTFUNCTIONID=9;
    public static final int COMMENT=10;
    public static final int STRING=12;

    // delegates
    // delegators


        public resolverv2Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public resolverv2Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return resolverv2Parser.tokenNames; }
    public String getGrammarFileName() { return "src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g"; }


    	public boolean debug = false;
    	public EwMap<String,JSONObject> ids = new EwMap<String,JSONObject>();
    	public EwMap<String,JSONObject> servlets = new EwMap<String,JSONObject>();
    	public EwMap<String,JSONObject> functions = new EwMap<String,JSONObject>();
    	public JSONObject obj = new EwJsonObject();
    	public Stack<JSONObject> stk = new Stack<JSONObject>();



    // $ANTLR start "parse"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:78:1: parse : ( ( decl ';' ( WS )? ) ( parse )? | COMMENT ( WS )? ( parse )? | WS ( parse )? | EOF );
    public final void parse() throws RecognitionException {
        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:3: ( ( decl ';' ( WS )? ) ( parse )? | COMMENT ( WS )? ( parse )? | WS ( parse )? | EOF )
            int alt6=4;
            switch ( input.LA(1) ) {
            case ID:
            case SERVLETID:
            case FUNCTIONID:
                {
                alt6=1;
                }
                break;
            case COMMENT:
                {
                alt6=2;
                }
                break;
            case WS:
                {
                alt6=3;
                }
                break;
            case EOF:
                {
                alt6=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:5: ( decl ';' ( WS )? ) ( parse )?
                    {
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:5: ( decl ';' ( WS )? )
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:6: decl ';' ( WS )?
                    {
                    pushFollow(FOLLOW_decl_in_parse545);
                    decl();

                    state._fsp--;

                    match(input,17,FOLLOW_17_in_parse546); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:13: ( WS )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==WS) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:13: WS
                            {
                            match(input,WS,FOLLOW_WS_in_parse547); 

                            }
                            break;

                    }


                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:18: ( parse )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==EOF||(LA2_0>=ID && LA2_0<=SERVLETID)||(LA2_0>=WS && LA2_0<=FUNCTIONID)||LA2_0==COMMENT) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:79:18: parse
                            {
                            pushFollow(FOLLOW_parse_in_parse551);
                            parse();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:80:5: COMMENT ( WS )? ( parse )?
                    {
                    match(input,COMMENT,FOLLOW_COMMENT_in_parse558); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:80:13: ( WS )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==WS) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:80:13: WS
                            {
                            match(input,WS,FOLLOW_WS_in_parse560); 

                            }
                            break;

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:80:17: ( parse )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==EOF||(LA4_0>=ID && LA4_0<=SERVLETID)||(LA4_0>=WS && LA4_0<=FUNCTIONID)||LA4_0==COMMENT) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:80:17: parse
                            {
                            pushFollow(FOLLOW_parse_in_parse563);
                            parse();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:81:5: WS ( parse )?
                    {
                    match(input,WS,FOLLOW_WS_in_parse570); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:81:8: ( parse )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==EOF||(LA5_0>=ID && LA5_0<=SERVLETID)||(LA5_0>=WS && LA5_0<=FUNCTIONID)||LA5_0==COMMENT) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:81:8: parse
                            {
                            pushFollow(FOLLOW_parse_in_parse572);
                            parse();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:82:5: EOF
                    {
                    match(input,EOF,FOLLOW_EOF_in_parse579); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "parse"


    // $ANTLR start "decl"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:85:1: decl : (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )? | s= SERVLETID ( WS )? ' ->' ( WS )? i= ID | s= SERVLETID ( WS )? '=' ( WS )? i= ID | s= FUNCTIONID ( WS )? ' ->' ( WS )? i= ID | s= FUNCTIONID ( WS )? '=' ( WS )? i= ID );
    public final void decl() throws RecognitionException {
        Token i=null;
        Token x=null;
        Token y=null;
        Token s=null;

        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:86:2: (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )? | s= SERVLETID ( WS )? ' ->' ( WS )? i= ID | s= SERVLETID ( WS )? '=' ( WS )? i= ID | s= FUNCTIONID ( WS )? ' ->' ( WS )? i= ID | s= FUNCTIONID ( WS )? '=' ( WS )? i= ID )
            int alt21=6;
            alt21 = dfa21.predict(input);
            switch (alt21) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:86:4: i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' )
                    {
                    i=(Token)match(input,ID,FOLLOW_ID_in_decl592); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:86:9: ( WS )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==WS) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:86:9: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl594); 

                            }
                            break;

                    }

                    match(input,18,FOLLOW_18_in_decl596); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:86:15: ( WS )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==WS) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:86:15: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl597); 

                            }
                            break;

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:87:3: ( functioncall | '{' param '}' )
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==FUNCTIONID) ) {
                        alt9=1;
                    }
                    else if ( (LA9_0==19) ) {
                        alt9=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 0, input);

                        throw nvae;
                    }
                    switch (alt9) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:87:5: functioncall
                            {
                            pushFollow(FOLLOW_functioncall_in_decl604);
                            functioncall();

                            state._fsp--;

                            ids.put(i.getText(),obj);obj = new EwJsonObject();

                            }
                            break;
                        case 2 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:88:5: '{' param '}'
                            {
                            match(input,19,FOLLOW_19_in_decl615); 
                            pushFollow(FOLLOW_param_in_decl616);
                            param();

                            state._fsp--;

                            match(input,20,FOLLOW_20_in_decl617); 
                            ids.put(i.getText(),obj);obj = new EwJsonObject();

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:90:4: x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )?
                    {
                    x=(Token)match(input,ID,FOLLOW_ID_in_decl633); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:90:9: ( WS )?
                    int alt10=2;
                    int LA10_0 = input.LA(1);

                    if ( (LA10_0==WS) ) {
                        alt10=1;
                    }
                    switch (alt10) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:90:9: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl635); 

                            }
                            break;

                    }

                    match(input,18,FOLLOW_18_in_decl637); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:90:15: ( WS )?
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==WS) ) {
                        alt11=1;
                    }
                    switch (alt11) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:90:15: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl638); 

                            }
                            break;

                    }

                    y=(Token)match(input,ID,FOLLOW_ID_in_decl643); 
                    obj=ids.get(y.getText());
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:91:3: ( dotfunctioncall )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0==DOTFUNCTIONID) ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:91:3: dotfunctioncall
                            {
                            pushFollow(FOLLOW_dotfunctioncall_in_decl653);
                            dotfunctioncall();

                            state._fsp--;


                            }
                            break;

                    }

                    ids.put(x.getText(),obj);obj = new EwJsonObject();

                    }
                    break;
                case 3 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:92:4: s= SERVLETID ( WS )? ' ->' ( WS )? i= ID
                    {
                    s=(Token)match(input,SERVLETID,FOLLOW_SERVLETID_in_decl666); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:92:16: ( WS )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==WS) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:92:16: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl668); 

                            }
                            break;

                    }

                    match(input,21,FOLLOW_21_in_decl671); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:92:26: ( WS )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==WS) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:92:26: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl673); 

                            }
                            break;

                    }

                    i=(Token)match(input,ID,FOLLOW_ID_in_decl678); 
                    servlets.put(s.getText(),ids.get(i.getText()));

                    }
                    break;
                case 4 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:93:4: s= SERVLETID ( WS )? '=' ( WS )? i= ID
                    {
                    s=(Token)match(input,SERVLETID,FOLLOW_SERVLETID_in_decl689); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:93:16: ( WS )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==WS) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:93:16: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl691); 

                            }
                            break;

                    }

                    match(input,18,FOLLOW_18_in_decl694); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:93:24: ( WS )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==WS) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:93:24: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl696); 

                            }
                            break;

                    }

                    i=(Token)match(input,ID,FOLLOW_ID_in_decl701); 
                    servlets.put(s.getText(),ids.get(i.getText()));

                    }
                    break;
                case 5 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:94:4: s= FUNCTIONID ( WS )? ' ->' ( WS )? i= ID
                    {
                    s=(Token)match(input,FUNCTIONID,FOLLOW_FUNCTIONID_in_decl712); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:94:17: ( WS )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0==WS) ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:94:17: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl714); 

                            }
                            break;

                    }

                    match(input,21,FOLLOW_21_in_decl717); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:94:27: ( WS )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==WS) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:94:27: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl719); 

                            }
                            break;

                    }

                    i=(Token)match(input,ID,FOLLOW_ID_in_decl724); 
                    functions.put(s.getText(),ids.get(i.getText()));

                    }
                    break;
                case 6 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:95:4: s= FUNCTIONID ( WS )? '=' ( WS )? i= ID
                    {
                    s=(Token)match(input,FUNCTIONID,FOLLOW_FUNCTIONID_in_decl734); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:95:17: ( WS )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==WS) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:95:17: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl736); 

                            }
                            break;

                    }

                    match(input,18,FOLLOW_18_in_decl739); 
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:95:25: ( WS )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==WS) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:95:25: WS
                            {
                            match(input,WS,FOLLOW_WS_in_decl741); 

                            }
                            break;

                    }

                    i=(Token)match(input,ID,FOLLOW_ID_in_decl746); 
                    functions.put(s.getText(),ids.get(i.getText()));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "decl"


    // $ANTLR start "functioncall"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:99:1: functioncall : x= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? ;
    public final void functioncall() throws RecognitionException {
        Token x=null;

        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:100:2: (x= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:100:4: x= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )?
            {
            x=(Token)match(input,FUNCTIONID,FOLLOW_FUNCTIONID_in_functioncall764); 
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:100:17: ( WS )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==WS) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:100:17: WS
                    {
                    match(input,WS,FOLLOW_WS_in_functioncall766); 

                    }
                    break;

            }

            match(input,22,FOLLOW_22_in_functioncall768); 
            try{obj.put("function",x.getText().substring(1));}catch(JSONException e){}
            pushFollow(FOLLOW_param_in_functioncall777);
            param();

            state._fsp--;

            match(input,23,FOLLOW_23_in_functioncall778); 
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:101:12: ( dotfunctioncall )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==DOTFUNCTIONID) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:101:12: dotfunctioncall
                    {
                    pushFollow(FOLLOW_dotfunctioncall_in_functioncall780);
                    dotfunctioncall();

                    state._fsp--;


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
        }
        return ;
    }
    // $ANTLR end "functioncall"


    // $ANTLR start "dotfunctioncall"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:104:1: dotfunctioncall : y= DOTFUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? ;
    public final void dotfunctioncall() throws RecognitionException {
        Token y=null;

        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:105:2: (y= DOTFUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? )
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:105:4: y= DOTFUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )?
            {
            y=(Token)match(input,DOTFUNCTIONID,FOLLOW_DOTFUNCTIONID_in_dotfunctioncall795); 
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:105:20: ( WS )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==WS) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:105:20: WS
                    {
                    match(input,WS,FOLLOW_WS_in_dotfunctioncall797); 

                    }
                    break;

            }

            match(input,22,FOLLOW_22_in_dotfunctioncall799); 
            try{stk.push(obj);obj = new EwJsonObject();obj.put("function",y.getText().substring(1));}catch(JSONException e){}
            pushFollow(FOLLOW_param_in_dotfunctioncall808);
            param();

            state._fsp--;

            try{obj.put("obj",stk.pop());}catch(JSONException e){}
            match(input,23,FOLLOW_23_in_dotfunctioncall819); 
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:107:7: ( dotfunctioncall )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==DOTFUNCTIONID) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:107:7: dotfunctioncall
                    {
                    pushFollow(FOLLOW_dotfunctioncall_in_dotfunctioncall821);
                    dotfunctioncall();

                    state._fsp--;


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
        }
        return ;
    }
    // $ANTLR end "dotfunctioncall"


    // $ANTLR start "param"
    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:110:1: param : ( ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? ) ( ( WS )? ',' param )? ( WS )? | ( WS )? x= ATID ( ( WS )? ',' param )? ( WS )? );
    public final void param() throws RecognitionException {
        Token x=null;
        Token y=null;

        try {
            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:2: ( ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? ) ( ( WS )? ',' param )? ( WS )? | ( WS )? x= ATID ( ( WS )? ',' param )? ( WS )? )
            int alt42=2;
            switch ( input.LA(1) ) {
            case WS:
                {
                int LA42_1 = input.LA(2);

                if ( (LA42_1==ID||LA42_1==WS||LA42_1==20||(LA42_1>=23 && LA42_1<=24)) ) {
                    alt42=1;
                }
                else if ( (LA42_1==ATID) ) {
                    alt42=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 42, 1, input);

                    throw nvae;
                }
                }
                break;
            case ID:
            case 20:
            case 23:
            case 24:
                {
                alt42=1;
                }
                break;
            case ATID:
                {
                alt42=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }

            switch (alt42) {
                case 1 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:4: ( WS )? (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? ) ( ( WS )? ',' param )? ( WS )?
                    {
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:4: ( WS )?
                    int alt26=2;
                    int LA26_0 = input.LA(1);

                    if ( (LA26_0==WS) ) {
                        alt26=1;
                    }
                    switch (alt26) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:4: WS
                            {
                            match(input,WS,FOLLOW_WS_in_param838); 

                            }
                            break;

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:8: (x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? ) | x= ID | ( WS )? )
                    int alt34=3;
                    int LA34_0 = input.LA(1);

                    if ( (LA34_0==ID) ) {
                        switch ( input.LA(2) ) {
                        case WS:
                            {
                            int LA34_3 = input.LA(3);

                            if ( (LA34_3==WS||LA34_3==20||(LA34_3>=23 && LA34_3<=24)) ) {
                                alt34=2;
                            }
                            else if ( (LA34_3==18) ) {
                                alt34=1;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 34, 3, input);

                                throw nvae;
                            }
                            }
                            break;
                        case 20:
                        case 23:
                        case 24:
                            {
                            alt34=2;
                            }
                            break;
                        case 18:
                            {
                            alt34=1;
                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 34, 1, input);

                            throw nvae;
                        }

                    }
                    else if ( (LA34_0==WS||LA34_0==20||(LA34_0>=23 && LA34_0<=24)) ) {
                        alt34=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 34, 0, input);

                        throw nvae;
                    }
                    switch (alt34) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:9: x= ID ( WS )? '=' ( WS )? (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? )
                            {
                            x=(Token)match(input,ID,FOLLOW_ID_in_param844); 
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:14: ( WS )?
                            int alt27=2;
                            int LA27_0 = input.LA(1);

                            if ( (LA27_0==WS) ) {
                                alt27=1;
                            }
                            switch (alt27) {
                                case 1 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:14: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_param846); 

                                    }
                                    break;

                            }

                            match(input,18,FOLLOW_18_in_param849); 
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:22: ( WS )?
                            int alt28=2;
                            int LA28_0 = input.LA(1);

                            if ( (LA28_0==WS) ) {
                                alt28=1;
                            }
                            switch (alt28) {
                                case 1 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:111:22: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_param851); 

                                    }
                                    break;

                            }

                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:112:5: (y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )? | y= STRING | y= INT | y= ID ( dotfunctioncall )? )
                            int alt32=4;
                            switch ( input.LA(1) ) {
                            case FUNCTIONID:
                                {
                                alt32=1;
                                }
                                break;
                            case STRING:
                                {
                                alt32=2;
                                }
                                break;
                            case INT:
                                {
                                alt32=3;
                                }
                                break;
                            case ID:
                                {
                                alt32=4;
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 32, 0, input);

                                throw nvae;
                            }

                            switch (alt32) {
                                case 1 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:113:6: y= FUNCTIONID ( WS )? '(' param ')' ( dotfunctioncall )?
                                    {
                                    y=(Token)match(input,FUNCTIONID,FOLLOW_FUNCTIONID_in_param867); 
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:113:19: ( WS )?
                                    int alt29=2;
                                    int LA29_0 = input.LA(1);

                                    if ( (LA29_0==WS) ) {
                                        alt29=1;
                                    }
                                    switch (alt29) {
                                        case 1 :
                                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:113:19: WS
                                            {
                                            match(input,WS,FOLLOW_WS_in_param869); 

                                            }
                                            break;

                                    }

                                    match(input,22,FOLLOW_22_in_param871); 
                                    try{stk.push(obj);obj = new EwJsonObject();obj.put("function",y.getText().substring(1));}catch(JSONException e){}
                                    pushFollow(FOLLOW_param_in_param882);
                                    param();

                                    state._fsp--;

                                    match(input,23,FOLLOW_23_in_param894); 
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:115:10: ( dotfunctioncall )?
                                    int alt30=2;
                                    int LA30_0 = input.LA(1);

                                    if ( (LA30_0==DOTFUNCTIONID) ) {
                                        alt30=1;
                                    }
                                    switch (alt30) {
                                        case 1 :
                                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:115:10: dotfunctioncall
                                            {
                                            pushFollow(FOLLOW_dotfunctioncall_in_param896);
                                            dotfunctioncall();

                                            state._fsp--;


                                            }
                                            break;

                                    }

                                    try{JSONObject jo = obj;obj=stk.pop();obj.put(x.getText(),jo);}catch(JSONException e){}

                                    }
                                    break;
                                case 2 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:116:6: y= STRING
                                    {
                                    y=(Token)match(input,STRING,FOLLOW_STRING_in_param910); 
                                    try{obj.put(x.getText(),y.getText().substring(1,y.getText().length()-1));}catch(JSONException e){}

                                    }
                                    break;
                                case 3 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:117:6: y= INT
                                    {
                                    y=(Token)match(input,INT,FOLLOW_INT_in_param925); 
                                    try{obj.put(x.getText(),y.getText().substring(1,y.getText().length()-1));}catch(JSONException e){}

                                    }
                                    break;
                                case 4 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:119:6: y= ID ( dotfunctioncall )?
                                    {
                                    y=(Token)match(input,ID,FOLLOW_ID_in_param949); 
                                    stk.push(obj);obj = ids.get(y.getText());
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:120:6: ( dotfunctioncall )?
                                    int alt31=2;
                                    int LA31_0 = input.LA(1);

                                    if ( (LA31_0==DOTFUNCTIONID) ) {
                                        alt31=1;
                                    }
                                    switch (alt31) {
                                        case 1 :
                                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:120:6: dotfunctioncall
                                            {
                                            pushFollow(FOLLOW_dotfunctioncall_in_param962);
                                            dotfunctioncall();

                                            state._fsp--;


                                            }
                                            break;

                                    }

                                    try{JSONObject jo = obj;obj=stk.pop();obj.put(x.getText(),jo);}catch(JSONException e){}

                                    }
                                    break;

                            }


                            }
                            break;
                        case 2 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:122:4: x= ID
                            {
                            x=(Token)match(input,ID,FOLLOW_ID_in_param979); 
                            				try{JSONObject jo = ids.get(x.getText());
                            						Iterator<String> it = jo.keys();
                            						while(it.hasNext())
                            						{
                            							String s = it.next();
                            							obj.put(s,jo.get(s));
                            						}}catch(JSONException e){}
                            			

                            }
                            break;
                        case 3 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:130:5: ( WS )?
                            {
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:130:5: ( WS )?
                            int alt33=2;
                            int LA33_0 = input.LA(1);

                            if ( (LA33_0==WS) ) {
                                alt33=1;
                            }
                            switch (alt33) {
                                case 1 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:130:5: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_param986); 

                                    }
                                    break;

                            }


                            }
                            break;

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:132:2: ( ( WS )? ',' param )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( (LA36_0==WS) ) {
                        int LA36_1 = input.LA(2);

                        if ( (LA36_1==24) ) {
                            alt36=1;
                        }
                    }
                    else if ( (LA36_0==24) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:132:4: ( WS )? ',' param
                            {
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:132:4: ( WS )?
                            int alt35=2;
                            int LA35_0 = input.LA(1);

                            if ( (LA35_0==WS) ) {
                                alt35=1;
                            }
                            switch (alt35) {
                                case 1 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:132:4: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_param996); 

                                    }
                                    break;

                            }

                            match(input,24,FOLLOW_24_in_param999); 
                            pushFollow(FOLLOW_param_in_param1001);
                            param();

                            state._fsp--;


                            }
                            break;

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:132:20: ( WS )?
                    int alt37=2;
                    int LA37_0 = input.LA(1);

                    if ( (LA37_0==WS) ) {
                        alt37=1;
                    }
                    switch (alt37) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:132:20: WS
                            {
                            match(input,WS,FOLLOW_WS_in_param1005); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:133:4: ( WS )? x= ATID ( ( WS )? ',' param )? ( WS )?
                    {
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:133:4: ( WS )?
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( (LA38_0==WS) ) {
                        alt38=1;
                    }
                    switch (alt38) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:133:4: WS
                            {
                            match(input,WS,FOLLOW_WS_in_param1011); 

                            }
                            break;

                    }

                    x=(Token)match(input,ATID,FOLLOW_ATID_in_param1016); 
                    try{obj.put(x.getText().substring(1),x.getText());}catch(JSONException e){}
                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:134:2: ( ( WS )? ',' param )?
                    int alt40=2;
                    int LA40_0 = input.LA(1);

                    if ( (LA40_0==WS) ) {
                        int LA40_1 = input.LA(2);

                        if ( (LA40_1==24) ) {
                            alt40=1;
                        }
                    }
                    else if ( (LA40_0==24) ) {
                        alt40=1;
                    }
                    switch (alt40) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:134:4: ( WS )? ',' param
                            {
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:134:4: ( WS )?
                            int alt39=2;
                            int LA39_0 = input.LA(1);

                            if ( (LA39_0==WS) ) {
                                alt39=1;
                            }
                            switch (alt39) {
                                case 1 :
                                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:134:4: WS
                                    {
                                    match(input,WS,FOLLOW_WS_in_param1026); 

                                    }
                                    break;

                            }

                            match(input,24,FOLLOW_24_in_param1029); 
                            pushFollow(FOLLOW_param_in_param1031);
                            param();

                            state._fsp--;


                            }
                            break;

                    }

                    // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:134:20: ( WS )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==WS) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // src\\java\\com\\eduworks\\resolver\\lang\\resolverv2.g:134:20: WS
                            {
                            match(input,WS,FOLLOW_WS_in_param1035); 

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return ;
    }
    // $ANTLR end "param"

    // Delegated rules


    protected DFA21 dfa21 = new DFA21(this);
    static final String DFA21_eotS =
        "\17\uffff";
    static final String DFA21_eofS =
        "\17\uffff";
    static final String DFA21_minS =
        "\1\4\3\7\1\22\1\4\1\22\2\uffff\1\22\2\uffff\1\4\2\uffff";
    static final String DFA21_maxS =
        "\1\10\1\22\2\25\1\22\1\23\1\25\2\uffff\1\25\2\uffff\1\23\2\uffff";
    static final String DFA21_acceptS =
        "\7\uffff\1\3\1\4\1\uffff\1\6\1\5\1\uffff\1\2\1\1";
    static final String DFA21_specialS =
        "\17\uffff}>";
    static final String[] DFA21_transitionS = {
            "\1\1\1\2\2\uffff\1\3",
            "\1\4\12\uffff\1\5",
            "\1\6\12\uffff\1\10\2\uffff\1\7",
            "\1\11\12\uffff\1\12\2\uffff\1\13",
            "\1\5",
            "\1\15\2\uffff\1\14\1\16\12\uffff\1\16",
            "\1\10\2\uffff\1\7",
            "",
            "",
            "\1\12\2\uffff\1\13",
            "",
            "",
            "\1\15\3\uffff\1\16\12\uffff\1\16",
            "",
            ""
    };

    static final short[] DFA21_eot = DFA.unpackEncodedString(DFA21_eotS);
    static final short[] DFA21_eof = DFA.unpackEncodedString(DFA21_eofS);
    static final char[] DFA21_min = DFA.unpackEncodedStringToUnsignedChars(DFA21_minS);
    static final char[] DFA21_max = DFA.unpackEncodedStringToUnsignedChars(DFA21_maxS);
    static final short[] DFA21_accept = DFA.unpackEncodedString(DFA21_acceptS);
    static final short[] DFA21_special = DFA.unpackEncodedString(DFA21_specialS);
    static final short[][] DFA21_transition;

    static {
        int numStates = DFA21_transitionS.length;
        DFA21_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA21_transition[i] = DFA.unpackEncodedString(DFA21_transitionS[i]);
        }
    }

    class DFA21 extends DFA {

        public DFA21(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 21;
            this.eot = DFA21_eot;
            this.eof = DFA21_eof;
            this.min = DFA21_min;
            this.max = DFA21_max;
            this.accept = DFA21_accept;
            this.special = DFA21_special;
            this.transition = DFA21_transition;
        }
        public String getDescription() {
            return "85:1: decl : (i= ID ( WS )? '=' ( WS )? ( functioncall | '{' param '}' ) | x= ID ( WS )? '=' ( WS )? y= ID ( dotfunctioncall )? | s= SERVLETID ( WS )? ' ->' ( WS )? i= ID | s= SERVLETID ( WS )? '=' ( WS )? i= ID | s= FUNCTIONID ( WS )? ' ->' ( WS )? i= ID | s= FUNCTIONID ( WS )? '=' ( WS )? i= ID );";
        }
    }
 

    public static final BitSet FOLLOW_decl_in_parse545 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_parse546 = new BitSet(new long[]{0x00000000000005B2L});
    public static final BitSet FOLLOW_WS_in_parse547 = new BitSet(new long[]{0x00000000000005B2L});
    public static final BitSet FOLLOW_parse_in_parse551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_COMMENT_in_parse558 = new BitSet(new long[]{0x00000000000005B2L});
    public static final BitSet FOLLOW_WS_in_parse560 = new BitSet(new long[]{0x00000000000005B2L});
    public static final BitSet FOLLOW_parse_in_parse563 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_parse570 = new BitSet(new long[]{0x00000000000005B2L});
    public static final BitSet FOLLOW_parse_in_parse572 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EOF_in_parse579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_decl592 = new BitSet(new long[]{0x0000000000040080L});
    public static final BitSet FOLLOW_WS_in_decl594 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_decl596 = new BitSet(new long[]{0x0000000000080180L});
    public static final BitSet FOLLOW_WS_in_decl597 = new BitSet(new long[]{0x0000000000080100L});
    public static final BitSet FOLLOW_functioncall_in_decl604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_decl615 = new BitSet(new long[]{0x00000000011000D0L});
    public static final BitSet FOLLOW_param_in_decl616 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_decl617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_decl633 = new BitSet(new long[]{0x0000000000040080L});
    public static final BitSet FOLLOW_WS_in_decl635 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_decl637 = new BitSet(new long[]{0x0000000000000090L});
    public static final BitSet FOLLOW_WS_in_decl638 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_decl643 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_dotfunctioncall_in_decl653 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SERVLETID_in_decl666 = new BitSet(new long[]{0x0000000000200080L});
    public static final BitSet FOLLOW_WS_in_decl668 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_decl671 = new BitSet(new long[]{0x0000000000000090L});
    public static final BitSet FOLLOW_WS_in_decl673 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_decl678 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SERVLETID_in_decl689 = new BitSet(new long[]{0x0000000000040080L});
    public static final BitSet FOLLOW_WS_in_decl691 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_decl694 = new BitSet(new long[]{0x0000000000000090L});
    public static final BitSet FOLLOW_WS_in_decl696 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_decl701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTIONID_in_decl712 = new BitSet(new long[]{0x0000000000200080L});
    public static final BitSet FOLLOW_WS_in_decl714 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_decl717 = new BitSet(new long[]{0x0000000000000090L});
    public static final BitSet FOLLOW_WS_in_decl719 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_decl724 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTIONID_in_decl734 = new BitSet(new long[]{0x0000000000040080L});
    public static final BitSet FOLLOW_WS_in_decl736 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_decl739 = new BitSet(new long[]{0x0000000000000090L});
    public static final BitSet FOLLOW_WS_in_decl741 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_decl746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTIONID_in_functioncall764 = new BitSet(new long[]{0x0000000000400080L});
    public static final BitSet FOLLOW_WS_in_functioncall766 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_functioncall768 = new BitSet(new long[]{0x00000000018000D0L});
    public static final BitSet FOLLOW_param_in_functioncall777 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_functioncall778 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_dotfunctioncall_in_functioncall780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOTFUNCTIONID_in_dotfunctioncall795 = new BitSet(new long[]{0x0000000000400080L});
    public static final BitSet FOLLOW_WS_in_dotfunctioncall797 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_dotfunctioncall799 = new BitSet(new long[]{0x00000000018000D0L});
    public static final BitSet FOLLOW_param_in_dotfunctioncall808 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_dotfunctioncall819 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_dotfunctioncall_in_dotfunctioncall821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_param838 = new BitSet(new long[]{0x0000000001000092L});
    public static final BitSet FOLLOW_ID_in_param844 = new BitSet(new long[]{0x0000000000040080L});
    public static final BitSet FOLLOW_WS_in_param846 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_param849 = new BitSet(new long[]{0x0000000000001990L});
    public static final BitSet FOLLOW_WS_in_param851 = new BitSet(new long[]{0x0000000000001910L});
    public static final BitSet FOLLOW_FUNCTIONID_in_param867 = new BitSet(new long[]{0x0000000000400080L});
    public static final BitSet FOLLOW_WS_in_param869 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_param871 = new BitSet(new long[]{0x00000000018000D0L});
    public static final BitSet FOLLOW_param_in_param882 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_param894 = new BitSet(new long[]{0x0000000001000282L});
    public static final BitSet FOLLOW_dotfunctioncall_in_param896 = new BitSet(new long[]{0x0000000001000082L});
    public static final BitSet FOLLOW_STRING_in_param910 = new BitSet(new long[]{0x0000000001000082L});
    public static final BitSet FOLLOW_INT_in_param925 = new BitSet(new long[]{0x0000000001000082L});
    public static final BitSet FOLLOW_ID_in_param949 = new BitSet(new long[]{0x0000000001000282L});
    public static final BitSet FOLLOW_dotfunctioncall_in_param962 = new BitSet(new long[]{0x0000000001000082L});
    public static final BitSet FOLLOW_ID_in_param979 = new BitSet(new long[]{0x0000000001000082L});
    public static final BitSet FOLLOW_WS_in_param986 = new BitSet(new long[]{0x0000000001000082L});
    public static final BitSet FOLLOW_WS_in_param996 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_param999 = new BitSet(new long[]{0x00000000010000D0L});
    public static final BitSet FOLLOW_param_in_param1001 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_WS_in_param1005 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WS_in_param1011 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ATID_in_param1016 = new BitSet(new long[]{0x0000000001000082L});
    public static final BitSet FOLLOW_WS_in_param1026 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_param1029 = new BitSet(new long[]{0x00000000010000D0L});
    public static final BitSet FOLLOW_param_in_param1031 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_WS_in_param1035 = new BitSet(new long[]{0x0000000000000002L});

}