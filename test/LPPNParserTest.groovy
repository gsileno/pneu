import org.leibnizcenter.pneu.parsers.LPPN.L
//
//class LPPNParserTest extends GroovyTestCase {
//
//    LPPNLoader asp = new LPPNLoader()
//
//    void testFact() {
//        String code = "a."
//
//        asp.reset()
//        asp.parseString(code)
//
//        assert(asp.program.atomBase.size() == 1)
//        assert(asp.program.formulaBase.size() == 1)
//        assert(asp.program.ruleBase.size() == 0)
//    }
//
//    void testNegFact() {
//        String code = "-a."
//
//        asp.reset()
//        asp.parseString(code)
//
//        assert(asp.program.atomBase.size() == 1)
//        assert(asp.program.formulaBase.size() == 1)
//        assert(asp.program.ruleBase.size() == 0)
//
//    }
//
//    void testChoiceOneAtomFact() {
//        String code = "{a}."
//
//        asp.reset()
//        asp.parseString(code)
//
//        assert(asp.program.atomBase.size() == 1)
//        assert(asp.program.formulaBase.size() == 2)
//        assert(asp.program.ruleBase.size() == 0)
//
//    }
//
//    void testChoiceTwoAtomsFact() {
//        String code = "{a, -a}."
//
//        asp.reset()
//        asp.parseString(code)
//
//        assert(asp.program.atomBase.size() == 1)
//        assert(asp.program.formulaBase.size() == 3)
//        assert(asp.program.ruleBase.size() == 0)
//
//    }
//
//    void testChoiceORTwoAtomsFact() {
//        String code = "1{a, b}."
//
//        asp.reset()
//        asp.parseString(code)
//
//        assert(asp.program.atomBase.size() == 2)
//        assert(asp.program.formulaBase.size() == 3)
//        assert(asp.program.ruleBase.size() == 0)
//
//    }
//
//    void testChoiceXORTwoAtomsFact() {
//        String code = "1{-a, a}1."
//
//        asp.reset()
//        asp.parseString(code)
//
//        assert(asp.program.atomBase.size() == 1)
//        assert(asp.program.formulaBase.size() == 3)
//        assert(asp.program.ruleBase.size() == 0)
//
//    }
//
//    void testChoiceANDTwoAtomsFact() {
//        String code = "2{a, b}2."
//
//        asp.reset()
//        asp.parseString(code)
//
//        assert(asp.program.atomBase.size() == 2)
//        assert(asp.program.formulaBase.size() == 3)
//        assert(asp.program.ruleBase.size() == 0)
//
//    }
//
//}