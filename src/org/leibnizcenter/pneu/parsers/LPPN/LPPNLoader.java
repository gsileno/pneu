//package org.leibnizcenter.pneu.parsers.LPPN;
//
//import org.antlr.v4.runtime.ANTLRInputStream;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.ParseTreeWalker;
//import org.leibnizcenter.pneu.components.logic.Program;
//
//import java.io.*;
//
//public class LPPNLoader {
//
//    private Program program;
//    private final RuleBaseType ruleBaseType;
//
//    public LPPNLoader() {
//        ruleBaseType = RuleBaseType.CONSTRAINT_BASED;
//        program = new Program(ruleBaseType);
//    }
//
//    public LPPNLoader(RuleBaseType type) {
//        ruleBaseType = type;
//        program = new Program(ruleBaseType);
//    }
//
//    public void reset() {
//        program = new Program(ruleBaseType);
//    }
//
//    public Program getProgram() {
//        return program;
//    }
//
//    public void parseString(String text) {
//
//        InputStream is = null;
//
//        try {
//            is = new ByteArrayInputStream(text.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        parse(is);
//    }
//
//    public void parseFile(String filename) throws FileNotFoundException {
//
//        InputStream is = null;
//
//        is = new FileInputStream(filename);
//
//        parse(is);
//    }
//
//
//    public void parse(InputStream is) {
//
//        ANTLRInputStream input = null;
//
//        try {
//            input = new ANTLRInputStream(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ASPLexer lexer = new ASPLexer(input);
//        lexer.removeErrorListeners();
//        lexer.addErrorListener(LPPNLoaderErrorListener.INSTANCE);
//
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//        ASPParser parser = new ASPParser(tokens);
//
//        //** TO LET ANTLR GIVES FEEDBACK ON THE GRAMMAR **//
//        parser.removeErrorListeners();
//        parser.addErrorListener(LPPNLoaderErrorListener.INSTANCE);
//
//        // parser.addErrorListener(new DiagnosticErrorListener());
//        // parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
//        //** **//
//
//        parser.setErrorHandler(new LPPNLoaderErrorStrategy());
//
//        ParseTree tree = parser.program();
//
//        // create a standard ANTLR parse tree walker
//        ParseTreeWalker walker = new ParseTreeWalker();
//        // create listener then feed to walker
//        LPPNLoaderListener loader = new LPPNLoaderListener();
//
//        loader.setProgram(program);
//
//        walker.walk(loader, tree); // walk parse tree
//    }
//}
