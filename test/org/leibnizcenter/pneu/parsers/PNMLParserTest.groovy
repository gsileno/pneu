package org.leibnizcenter.pneu.parsers

import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net

class PNMLParserTest extends GroovyTestCase {

    void test0EmptyPlace() {
        Net net = PNML2PN.parseFile("examples/basic/0emptyplace.pnml")

        assert(net.transitionList.size() == 0)
        assert(net.placeList.size() == 1)
        assert(net.placeList.get(0).marking.size() == 0)
    }

    void test0PlaceFilledWith3Tokens() {
        Net net = PNML2PN.parseFile("examples/basic/0placefilledwith3tokens.pnml")

        assert(net.transitionList.size() == 0)
        assert(net.placeList.size() == 1)
        assert(net.placeList.get(0).marking.size() == 3)
    }

    void test1Transition() {
        Net net = PNML2PN.parseFile("examples/basic/1transition.pnml")

        assert(net.transitionList.size() == 2)
        assert(net.placeList.size() == 1)
        assert(net.placeList.get(0).marking.size() == 0)
    }

    void test2Chaining() {
        Net net = PNML2PN.parseFile("examples/basic/2chaining.pnml")

        assert(net.transitionList.size() == 3)
        assert(net.placeList.size() == 2)
        assert(net.placeList.get(0).marking.size() == 0)
    }

    void test3Doublearc() {
        Net net = PNML2PN.parseFile("examples/basic/3doublearc.pnml")

        assert(net.transitionList.size() == 4)
        assert(net.placeList.size() == 4)
        assert(net.arcList.size() == 7)
        assert(net.arcList.get(6).weight == 5) // the last arc has weight of 5
    }

    void test4Conflict() {
        Net net = PNML2PN.parseFile("examples/basic/4conflict.pnml")

        assert(net.transitionList.size() == 4)
        assert(net.placeList.size() == 3)
        assert(net.arcList.size() == 9)
    }

    void test5Inhibitor() {
        Net net = PNML2PN.parseFile("examples/basic/5inhibitor.pnml")

        assert(net.transitionList.size() == 3)
        assert(net.placeList.size() == 3)
        assert(net.arcList.size() == 6)

        assert(net.arcList.findAll { it.type == ArcType.NORMAL }.size() == 5 )
        assert(net.arcList.findAll { it.type == ArcType.INHIBITOR }.size() == 1 )
    }

    void test6Biflow() {
        Net net = PNML2PN.parseFile("examples/basic/6biflow.pnml")
        // TODO
    }

}