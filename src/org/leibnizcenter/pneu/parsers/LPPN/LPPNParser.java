// Generated from /home/giovanni/dev/pneu/antlr4/LPPN.g4 by ANTLR 4.5
package org.leibnizcenter.pneu.parsers.LPPN;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LPPNParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NEG=1, NAF=2, AND=3, OR=4, XOR=5, SEQ=6, PAR=7, IFP=8, THENP=9, IFT=10, 
		CAUSEDBY=11, CAUSES=12, DOT=13, INTEGER=14, IDENTIFIER=15, VARIABLE=16, 
		SINGLE_LINE_COMMENT=17, MULTILINE_COMMENT=18, SPACES=19;
	public static final int
		RULE_program = 0, RULE_statement_list = 1, RULE_statement = 2, RULE_head = 3, 
		RULE_antecedent = 4, RULE_consequent = 5, RULE_body = 6, RULE_bterm = 7, 
		RULE_term = 8, RULE_atom = 9;
	public static final String[] ruleNames = {
		"program", "statement_list", "statement", "head", "antecedent", "consequent", 
		"body", "bterm", "term", "atom"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'naf'", null, null, "'xor'", null, null, "'<|'", "'|>'", 
		"'<-'", "'<='", "'=>'", "'.'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "NEG", "NAF", "AND", "OR", "XOR", "SEQ", "PAR", "IFP", "THENP", 
		"IFT", "CAUSEDBY", "CAUSES", "DOT", "INTEGER", "IDENTIFIER", "VARIABLE", 
		"SINGLE_LINE_COMMENT", "MULTILINE_COMMENT", "SPACES"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	@NotNull
	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "LPPN.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LPPNParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LPPNParser.EOF, 0); }
		public Statement_listContext statement_list() {
			return getRuleContext(Statement_listContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEG) | (1L << NAF) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(20); 
				statement_list();
				}
			}

			setState(23); 
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Statement_listContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode DOT() { return getToken(LPPNParser.DOT, 0); }
		public Statement_listContext statement_list() {
			return getRuleContext(Statement_listContext.class,0);
		}
		public Statement_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterStatement_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitStatement_list(this);
		}
	}

	public final Statement_listContext statement_list() throws RecognitionException {
		Statement_listContext _localctx = new Statement_listContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25); 
			statement();
			setState(26); 
			match(DOT);
			setState(28);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEG) | (1L << NAF) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(27); 
				statement_list();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public HeadContext head() {
			return getRuleContext(HeadContext.class,0);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode IFP() { return getToken(LPPNParser.IFP, 0); }
		public TerminalNode IFT() { return getToken(LPPNParser.IFT, 0); }
		public TerminalNode THENP() { return getToken(LPPNParser.THENP, 0); }
		public ConsequentContext consequent() {
			return getRuleContext(ConsequentContext.class,0);
		}
		public TerminalNode CAUSEDBY() { return getToken(LPPNParser.CAUSEDBY, 0); }
		public AntecedentContext antecedent() {
			return getRuleContext(AntecedentContext.class,0);
		}
		public TerminalNode CAUSES() { return getToken(LPPNParser.CAUSES, 0); }
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statement);
		int _la;
		try {
			setState(47);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(30); 
				head();
				setState(33);
				_la = _input.LA(1);
				if (_la==IFP || _la==IFT) {
					{
					setState(31);
					_la = _input.LA(1);
					if ( !(_la==IFP || _la==IFT) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					setState(32); 
					body();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(35); 
				body();
				setState(36); 
				match(THENP);
				setState(37); 
				head();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(39); 
				consequent();
				setState(40); 
				match(CAUSEDBY);
				setState(41); 
				antecedent();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(43); 
				antecedent();
				setState(44); 
				match(CAUSES);
				setState(45); 
				consequent();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public HeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_head; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitHead(this);
		}
	}

	public final HeadContext head() throws RecognitionException {
		HeadContext _localctx = new HeadContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_head);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49); 
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AntecedentContext extends ParserRuleContext {
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public AntecedentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_antecedent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterAntecedent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitAntecedent(this);
		}
	}

	public final AntecedentContext antecedent() throws RecognitionException {
		AntecedentContext _localctx = new AntecedentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_antecedent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51); 
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConsequentContext extends ParserRuleContext {
		public HeadContext head() {
			return getRuleContext(HeadContext.class,0);
		}
		public ConsequentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_consequent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterConsequent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitConsequent(this);
		}
	}

	public final ConsequentContext consequent() throws RecognitionException {
		ConsequentContext _localctx = new ConsequentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_consequent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53); 
			head();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BodyContext extends ParserRuleContext {
		public List<BtermContext> bterm() {
			return getRuleContexts(BtermContext.class);
		}
		public BtermContext bterm(int i) {
			return getRuleContext(BtermContext.class,i);
		}
		public TerminalNode AND() { return getToken(LPPNParser.AND, 0); }
		public TerminalNode OR() { return getToken(LPPNParser.OR, 0); }
		public TerminalNode XOR() { return getToken(LPPNParser.XOR, 0); }
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitBody(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_body);
		try {
			setState(68);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(55); 
				bterm();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(56); 
				bterm();
				setState(57); 
				match(AND);
				setState(58); 
				bterm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(60); 
				bterm();
				setState(61); 
				match(OR);
				setState(62); 
				bterm();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(64); 
				bterm();
				setState(65); 
				match(XOR);
				setState(66); 
				bterm();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BtermContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode NAF() { return getToken(LPPNParser.NAF, 0); }
		public BtermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterBterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitBterm(this);
		}
	}

	public final BtermContext bterm() throws RecognitionException {
		BtermContext _localctx = new BtermContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_bterm);
		try {
			setState(73);
			switch (_input.LA(1)) {
			case NEG:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(70); 
				term();
				}
				break;
			case NAF:
				enterOuterAlt(_localctx, 2);
				{
				setState(71); 
				match(NAF);
				setState(72); 
				term();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public TerminalNode NEG() { return getToken(LPPNParser.NEG, 0); }
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_term);
		try {
			setState(78);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(75); 
				atom();
				}
				break;
			case NEG:
				enterOuterAlt(_localctx, 2);
				{
				setState(76); 
				match(NEG);
				setState(77); 
				atom();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(LPPNParser.IDENTIFIER, 0); }
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LPPNListener ) ((LPPNListener)listener).exitAtom(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_atom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80); 
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\25U\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\5\2\30\n\2\3\2\3\2\3\3\3\3\3\3\5\3\37\n\3\3\4\3\4\3\4\5\4$\n\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\62\n\4\3\5\3\5\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bG\n"+
		"\b\3\t\3\t\3\t\5\tL\n\t\3\n\3\n\3\n\5\nQ\n\n\3\13\3\13\3\13\2\2\f\2\4"+
		"\6\b\n\f\16\20\22\24\2\3\4\2\n\n\f\fU\2\27\3\2\2\2\4\33\3\2\2\2\6\61\3"+
		"\2\2\2\b\63\3\2\2\2\n\65\3\2\2\2\f\67\3\2\2\2\16F\3\2\2\2\20K\3\2\2\2"+
		"\22P\3\2\2\2\24R\3\2\2\2\26\30\5\4\3\2\27\26\3\2\2\2\27\30\3\2\2\2\30"+
		"\31\3\2\2\2\31\32\7\2\2\3\32\3\3\2\2\2\33\34\5\6\4\2\34\36\7\17\2\2\35"+
		"\37\5\4\3\2\36\35\3\2\2\2\36\37\3\2\2\2\37\5\3\2\2\2 #\5\b\5\2!\"\t\2"+
		"\2\2\"$\5\16\b\2#!\3\2\2\2#$\3\2\2\2$\62\3\2\2\2%&\5\16\b\2&\'\7\13\2"+
		"\2\'(\5\b\5\2(\62\3\2\2\2)*\5\f\7\2*+\7\r\2\2+,\5\n\6\2,\62\3\2\2\2-."+
		"\5\n\6\2./\7\16\2\2/\60\5\f\7\2\60\62\3\2\2\2\61 \3\2\2\2\61%\3\2\2\2"+
		"\61)\3\2\2\2\61-\3\2\2\2\62\7\3\2\2\2\63\64\5\22\n\2\64\t\3\2\2\2\65\66"+
		"\5\16\b\2\66\13\3\2\2\2\678\5\b\5\28\r\3\2\2\29G\5\20\t\2:;\5\20\t\2;"+
		"<\7\5\2\2<=\5\20\t\2=G\3\2\2\2>?\5\20\t\2?@\7\6\2\2@A\5\20\t\2AG\3\2\2"+
		"\2BC\5\20\t\2CD\7\7\2\2DE\5\20\t\2EG\3\2\2\2F9\3\2\2\2F:\3\2\2\2F>\3\2"+
		"\2\2FB\3\2\2\2G\17\3\2\2\2HL\5\22\n\2IJ\7\4\2\2JL\5\22\n\2KH\3\2\2\2K"+
		"I\3\2\2\2L\21\3\2\2\2MQ\5\24\13\2NO\7\3\2\2OQ\5\24\13\2PM\3\2\2\2PN\3"+
		"\2\2\2Q\23\3\2\2\2RS\7\21\2\2S\25\3\2\2\2\t\27\36#\61FKP";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}