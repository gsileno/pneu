//package org.leibnizcenter.pneu.parsers.LPPN;
//
//import org.antlr.v4.runtime.*;
//import org.antlr.v4.runtime.misc.*;
//
//public class LPPNLoaderErrorListener extends BaseErrorListener {
//
//   public static final LPPNLoaderErrorListener INSTANCE = new LPPNLoaderErrorListener();
//
//   @Override
//   public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
//      throws ParseCancellationException {
//         throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
//      }
//}