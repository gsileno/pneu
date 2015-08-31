package org.leibnizcenter.pneu.animation

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
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

    void testNexusStructure() {
        Net net = new BasicNet()

        Transition tInput = net.createEmitterTransition()
        Place pInput = net.createPlace("input")
        net.createArc(tInput, pInput)

        Transition tOutput = net.createCollectorTransition()
        Place pOutput = net.createPlace("output")
        net.createArc(pOutput, tOutput)

        Transition tInhibitor = net.createEmitterTransition()
        Place pInhibitor = net.createPlace("inhibitor")
        net.createArc(tInhibitor, pInhibitor)

        Transition tBiflow = net.createEmitterTransition()
        Place pBiflow = net.createPlace("biflow")
        net.createArc(tBiflow, pBiflow)

        Transition tDiode = net.createEmitterTransition()
        Place pDiode = net.createPlace("diode")
        net.createArc(tDiode, pDiode)

        net.createNexus([pInput], [pOutput], [pBiflow], [pDiode], [pInhibitor])

        net.resetIds()

        NetRunner runner = new NetRunner()
        runner.load(net)
        runner.analyse()
        runner.analysis.exportToLog("BasicNexusStructure")

        assert runner.analysis.stateBase.base.size() == 22
        assert runner.analysis.storyBase.base.size() == 23
    }
}