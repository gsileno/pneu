import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parser.pneu

class ParserTest extends GroovyTestCase {

    void test0EmptyPlace() {
        Net net = pneu.parseFile("examples/basic/0emptyplace.pnml")
    }

    void test0PlaceFilledWith3Tokens() {
        Net net = pneu.parseFile("examples/basic/0placefilledwith3tokens.pnml")
    }

    void test1Transition() {
        Net net = pneu.parseFile("examples/basic/1transition.pnml")
    }

    void test2Chaining() {
        Net net = pneu.parseFile("examples/basic/2chaining.pnml")
    }

    void test3Doublearc() {
        Net net = pneu.parseFile("examples/basic/3doublearc.pnml")
    }

    void test4Conflict() {
        Net net = pneu.parseFile("examples/basic/4conflict.pnml")
    }

    void test5Inhibitor() {
        Net net = pneu.parseFile("examples/basic/5inhibitor.pnml")
    }

    void test6Biflow() {
        Net net = pneu.parseFile("examples/basic/6biflow.pnml")
    }

}