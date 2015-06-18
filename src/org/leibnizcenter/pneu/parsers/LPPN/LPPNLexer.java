// Generated from /home/giovanni/dev/pneu/antlr4/LPPN.g4 by ANTLR 4.5
package org.leibnizcenter.pneu.parsers.LPPN;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LPPNLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NEG=1, NAF=2, AND=3, OR=4, XOR=5, SEQ=6, PAR=7, IFP=8, THENP=9, IFT=10, 
		CAUSEDBY=11, CAUSES=12, DOT=13, INTEGER=14, IDENTIFIER=15, VARIABLE=16, 
		SINGLE_LINE_COMMENT=17, MULTILINE_COMMENT=18, SPACES=19;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"NEG", "NAF", "AND", "OR", "XOR", "SEQ", "PAR", "IFP", "THENP", "IFT", 
		"CAUSEDBY", "CAUSES", "DOT", "INTEGER", "IDENTIFIER", "VARIABLE", "SINGLE_LINE_COMMENT", 
		"MULTILINE_COMMENT", "SPACES"
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


	public LPPNLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LPPN.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\25\u009a\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\2\3\2\5\2.\n\2\3\3\3\3\3\3\3\3\3\4"+
		"\3\4\3\4\3\4\3\4\5\49\n\4\3\5\3\5\3\5\3\5\5\5?\n\5\3\6\3\6\3\6\3\6\3\7"+
		"\3\7\3\7\3\7\5\7I\n\7\3\b\3\b\3\b\3\b\5\bO\n\b\3\t\3\t\3\t\3\n\3\n\3\n"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\7\17"+
		"e\n\17\f\17\16\17h\13\17\5\17j\n\17\3\20\3\20\7\20n\n\20\f\20\16\20q\13"+
		"\20\3\21\3\21\7\21u\n\21\f\21\16\21x\13\21\3\22\3\22\3\22\5\22}\n\22\3"+
		"\22\7\22\u0080\n\22\f\22\16\22\u0083\13\22\3\22\3\22\3\23\3\23\3\23\3"+
		"\23\7\23\u008b\n\23\f\23\16\23\u008e\13\23\3\23\3\23\3\23\5\23\u0093\n"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\24\3\u008c\2\25\3\3\5\4\7\5\t\6\13\7\r"+
		"\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25"+
		"\3\2\n\3\2\63;\3\2\62;\4\2aac|\4\2\62;c|\3\2C\\\6\2\62;C\\aac|\4\2\f\f"+
		"\17\17\5\2\13\r\17\17\"\"\u00a6\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\3-\3\2\2\2"+
		"\5/\3\2\2\2\78\3\2\2\2\t>\3\2\2\2\13@\3\2\2\2\rH\3\2\2\2\17N\3\2\2\2\21"+
		"P\3\2\2\2\23S\3\2\2\2\25V\3\2\2\2\27Y\3\2\2\2\31\\\3\2\2\2\33_\3\2\2\2"+
		"\35i\3\2\2\2\37k\3\2\2\2!r\3\2\2\2#|\3\2\2\2%\u0086\3\2\2\2\'\u0096\3"+
		"\2\2\2)*\7p\2\2*+\7q\2\2+.\7v\2\2,.\7/\2\2-)\3\2\2\2-,\3\2\2\2.\4\3\2"+
		"\2\2/\60\7p\2\2\60\61\7c\2\2\61\62\7h\2\2\62\6\3\2\2\2\63\64\7c\2\2\64"+
		"\65\7p\2\2\659\7f\2\2\66\67\7(\2\2\679\7(\2\28\63\3\2\2\28\66\3\2\2\2"+
		"9\b\3\2\2\2:;\7q\2\2;?\7t\2\2<=\7~\2\2=?\7~\2\2>:\3\2\2\2><\3\2\2\2?\n"+
		"\3\2\2\2@A\7z\2\2AB\7q\2\2BC\7t\2\2C\f\3\2\2\2DE\7u\2\2EF\7g\2\2FI\7s"+
		"\2\2GI\7(\2\2HD\3\2\2\2HG\3\2\2\2I\16\3\2\2\2JK\7r\2\2KL\7c\2\2LO\7t\2"+
		"\2MO\7~\2\2NJ\3\2\2\2NM\3\2\2\2O\20\3\2\2\2PQ\7>\2\2QR\7~\2\2R\22\3\2"+
		"\2\2ST\7~\2\2TU\7@\2\2U\24\3\2\2\2VW\7>\2\2WX\7/\2\2X\26\3\2\2\2YZ\7>"+
		"\2\2Z[\7?\2\2[\30\3\2\2\2\\]\7?\2\2]^\7@\2\2^\32\3\2\2\2_`\7\60\2\2`\34"+
		"\3\2\2\2aj\7\62\2\2bf\t\2\2\2ce\t\3\2\2dc\3\2\2\2eh\3\2\2\2fd\3\2\2\2"+
		"fg\3\2\2\2gj\3\2\2\2hf\3\2\2\2ia\3\2\2\2ib\3\2\2\2j\36\3\2\2\2ko\t\4\2"+
		"\2ln\t\5\2\2ml\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2p \3\2\2\2qo\3\2\2"+
		"\2rv\t\6\2\2su\t\7\2\2ts\3\2\2\2ux\3\2\2\2vt\3\2\2\2vw\3\2\2\2w\"\3\2"+
		"\2\2xv\3\2\2\2y}\7\'\2\2z{\7\61\2\2{}\7\61\2\2|y\3\2\2\2|z\3\2\2\2}\u0081"+
		"\3\2\2\2~\u0080\n\b\2\2\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2"+
		"\2\2\u0081\u0082\3\2\2\2\u0082\u0084\3\2\2\2\u0083\u0081\3\2\2\2\u0084"+
		"\u0085\b\22\2\2\u0085$\3\2\2\2\u0086\u0087\7\61\2\2\u0087\u0088\7,\2\2"+
		"\u0088\u008c\3\2\2\2\u0089\u008b\13\2\2\2\u008a\u0089\3\2\2\2\u008b\u008e"+
		"\3\2\2\2\u008c\u008d\3\2\2\2\u008c\u008a\3\2\2\2\u008d\u0092\3\2\2\2\u008e"+
		"\u008c\3\2\2\2\u008f\u0090\7,\2\2\u0090\u0093\7\61\2\2\u0091\u0093\7\2"+
		"\2\3\u0092\u008f\3\2\2\2\u0092\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094"+
		"\u0095\b\23\2\2\u0095&\3\2\2\2\u0096\u0097\t\t\2\2\u0097\u0098\3\2\2\2"+
		"\u0098\u0099\b\24\2\2\u0099(\3\2\2\2\20\2-8>HNfiov|\u0081\u008c\u0092"+
		"\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}