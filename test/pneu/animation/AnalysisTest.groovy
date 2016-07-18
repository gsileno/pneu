package pneu.animation

import pneu.animation.monolithic.NetRunner
import pneu.components.basicpetrinet.BasicNet
import pneu.components.petrinet.Net
import pneu.components.petrinet.Place
import pneu.components.petrinet.Transition
import pneu.parsers.PNML2PN

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

        net.createTransitionNexus([pInput], [pOutput], [pBiflow], [pDiode], [pInhibitor])

        net.resetIds()

        NetRunner runner = new NetRunner()
        runner.load(net)
        runner.analyse()
        runner.analysis.exportToLog("BasicNexusStructure")

        assert runner.analysis.stateBase.base.size() == 22
        assert runner.analysis.storyBase.base.size() == 23
    }

    void testSimpleBasicOut() {
        Net net = new BasicNet()

        Transition tInput = net.createEmitterTransition()
        Transition tOutput = net.createCollectorTransition()

        Place p = net.createPlace("p")
        net.createArc(tInput, p)
        net.createArc(p, tOutput)

        net.resetIds()

        NetRunner runner = new NetRunner()
        runner.load(net)

        assert runner.analyse() == 2

        runner.status()
        assert runner.analysis.storyBase.base.size() == 1
        assert runner.analysis.stateBase.base.size() == 2

    }

    void testBasicInputWithDifferentEmitters() {
        Net net = new BasicNet()

        Transition tInput = net.createEmitterTransition()
        Place pInput = net.createPlace("input")
        net.createArc(tInput, pInput)

        Transition tInput2 = net.createEmitterTransition()
        Place pInput2 = net.createPlace("input")
        net.createArc(tInput2, pInput2)

        Transition tOutput = net.createCollectorTransition()
        Place pOutput = net.createPlace("output")
        net.createArc(pOutput, tOutput)

        net.createTransitionNexus([pInput, pInput2], [pOutput], [], [], [])

        net.resetIds()

        NetRunner runner = new NetRunner()
        runner.load(net)

        runner.analyse()

        assert runner.analysis.storyBase.base.size() == 2
        assert runner.analysis.storyBase.base[0].steps.size() == 5
        assert runner.analysis.storyBase.base[1].steps.size() == 3
        assert runner.analysis.stateBase.base.size() == 5
        runner.status()

    }

}