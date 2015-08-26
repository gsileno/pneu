package org.leibnizcenter.pneu.animation

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.parsers.PNML2PN

class AnalysisTest extends GroovyTestCase {

    void test0EmptyPlace() {
        Net net = PNML2PN.parseFile("examples/basic/0emptyplace.pnml")

        NetRunner runner = new NetRunner()
        runner.load(net)
        assert(runner.analyse() == 0)
        assert(runner.analysis.stateBase.base.size() == 1)
    }

    void test0PlaceFilledWith3Tokens() {
        Net net = PNML2PN.parseFile("examples/basic/0placefilledwith3tokens.pnml")

        NetRunner runner = new NetRunner()
        runner.load(net)
        assert(runner.analyse() == 0)
        assert(runner.analysis.stateBase.base.size() == 1)
    }

    void test8AnalysisConflictBase() {
        Net net = PNML2PN.parseFile("examples/basic/8analysisconflict.pnml")
        NetRunner runner = new NetRunner()
        runner.load(net)
        assert(runner.analyse() == 6)

        runner.status()
        assert(runner.analysis.stateBase.base.size() == 5)
        assert(runner.analysis.storyBase.base.size() == 3)
    }

}