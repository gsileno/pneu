package org.leibnizcenter.pneu.animation

import org.leibnizcenter.pneu.animation.monolithic.PNRunner
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

class AnalysisTest extends GroovyTestCase {

    void test0EmptyPlace() {
        Net net = PNML2PN.parseFile("examples/basic/0emptyplace.pnml")

        PNRunner runner = new PNRunner()
        runner.load(net)
        assert(runner.runAnalysis() == 0)
        assert(runner.analysis.stateBase.base.size() == 1)
    }

    void test0PlaceFilledWith3Tokens() {
        Net net = PNML2PN.parseFile("examples/basic/0placefilledwith3tokens.pnml")

        PNRunner runner = new PNRunner()
        runner.load(net)
        assert(runner.runAnalysis() == 0)
        assert(runner.analysis.stateBase.base.size() == 1)
    }

    void test8AnalysisConflictBase() {
        Net net = PNML2PN.parseFile("examples/basic/8analysisconflict.pnml")
        PNRunner runner = new PNRunner()
        runner.load(net)
        assert(runner.runAnalysis() == 6)

        runner.status()
        assert(runner.analysis.stateBase.base.size() == 5)
        assert(runner.analysis.storySet.set.size() == 3)
    }

}