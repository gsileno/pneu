package org.leibnizcenter.pneu.decomponsition

import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.decomposition.Alignment
import org.leibnizcenter.pneu.decomposition.RelationType
import org.leibnizcenter.pneu.decomposition.Subsumption
import org.leibnizcenter.pneu.examples.CommonConstructs

class SubsumptionTest extends GroovyTestCase {

    static Net net00() {
        Net net = new BasicNet()
        net
    }

    static Net net0() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pA = net.createPlace("a")
        Transition tOut = net.createCollectorTransition()

        net.createArc(tIn, pA)
        net.createArc(pA, tOut)
        net.resetIds()
        net
    }

    static Net net1() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Transition tOut = net.createCollectorTransition()

        net.createArc(tIn, pA)
        net.createBridge(pA, pB)
        net.createBridge(pB, pC)
        net.createArc(pC, tOut)

        net.resetIds()
        net
    }

    static Net net2() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")
        Place pF = net.createPlace("f")
        Transition tOut = net.createCollectorTransition()

        net.createArc(tIn, pA)
        net.createBridge(pA, pB)
        net.createBridge(pB, pC)
        net.createBridge(pC, pF)
        net.createBridge(pA, pD)
        net.createBridge(pD, pE)
        net.createBridge(pE, pF)
        net.createArc(pF, tOut)

        net.resetIds()
        net
    }

    static Net net3() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")

        net.createArc(tIn, pA)
        net.createBridge(pA, pB)
        net.createBridge(pB, pC)
        net.createBridge(pA, pD)
        net.createBridge(pD, pE)

        Transition tOut = net.createCollectorTransition()
        net.createArc(pC, tOut)
        net.createArc(pE, tOut)

        net.resetIds()
        net
    }

    static Net net4() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pAA = net.createPlace("aa")
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")
        Place pF = net.createPlace("f")
        Transition tOut = net.createCollectorTransition()

        net.createArc(tIn, pAA)
        net.createBridge(pAA, pA)
        net.createBridge(pA, pB)
        net.createBridge(pB, pC)
        net.createBridge(pC, pF)
        net.createBridge(pA, pD)
        net.createBridge(pD, pE)
        net.createBridge(pE, pF)
        net.createArc(pF, tOut)

        net.resetIds()
        net
    }

    static Net net5() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pAA = net.createPlace("aa")
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")
        Place pF = net.createPlace("f")
        Place pG = net.createPlace("g")
        Transition tOut = net.createCollectorTransition()

        net.createArc(tIn, pAA)
        net.createBridge(pAA, pA)
        net.createBridge(pA, pB)
        net.createBridge(pB, pC)
        net.createBridge(pC, pF)
        net.createBridge(pA, pD)
        net.createBridge(pD, pE)
        net.createBridge(pE, pF)
        net.createBridge(pF, pG)
        net.createArc(pG, tOut)

        net.resetIds()
        net
    }

    static Net net6() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")

        net.createArc(tIn, pA)
        net.createBridge(pA, pB)
        net.createBridge(pB, pC)
        net.createBridge(pA, pD)
        net.createBridge(pD, pE)

        net.resetIds()
        net
    }

    static Net net7() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pAA = net.createPlace("aa")
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")
        Place pF = net.createPlace("f")
        Place pG = net.createPlace("g")
        Place pH = net.createPlace("h")
        Place pI = net.createPlace("i")
        Place pL = net.createPlace("l")

        net.createArc(tIn, pAA)
        net.createBridge(pAA, pA)
        net.createBridges([pA, pB, pC, pF])
        net.createBridges([pA, pD, pE, pF])
        net.createBridge(pF, pG)
        net.createBridges([pF, pH, pI, pL])

        net.resetIds()
        net
    }

    static Net net8() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pAA = net.createPlace("aa")
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")
        Place pF = net.createPlace("f")
        Place pG = net.createPlace("g")
        Place pH = net.createPlace("h")
        Place pI = net.createPlace("i")
        Place pL = net.createPlace("l")
        Place pM = net.createPlace("m")
        Place pN = net.createPlace("n")
        Place pO = net.createPlace("o")
        Place pP = net.createPlace("p")
        Place pQ = net.createPlace("q")
        Place pR = net.createPlace("r")
        Place pS = net.createPlace("s")
        Place pT = net.createPlace("t")

        net.createArc(tIn, pAA)
        net.createBridge(pAA, pA)
        net.createBridges([pA, pB, pC, pF])
        net.createBridges([pA, pD, pE, pF])
        net.createBridge(pF, pG)
        net.createBridges([pF, pH, pI, pL])
        net.createBridges([pF, pM, pN, pO, pP, pQ])

        List<Transition> tList= net.createBridges([pN, pR, pS, pP])
        net.createBridge(tList[0], pT, tList[1])

        net.resetIds()
        net
    }

    // a - b - c - d - e
    static Net netSpecific() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")

        net.createArc(tIn, pA)
        net.createBridges([pA, pB, pC, pD, pE])
        net.createArc(pE, tOut)

        net.resetIds()
        net
    }

    // a portion of the previous one: b - c - d
    static Net netSpecificBis() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")

        net.createArc(tIn, pB)
        net.createBridges([pB, pC, pD])
        net.createArc(pD, tOut)

        net.resetIds()
        net
    }

    // an abstraction of the previous one (b - d)
    static Net netGeneral() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pB = net.createPlace("b")
        Place pD = net.createPlace("d")

        net.createArc(tIn, pB)
        net.createBridges([pB, pD])
        net.createArc(pD, tOut)

        net.resetIds()
        net
    }

    // an abstraction of the previous one, with different terminals (a - b - d - e)
    static Net netGeneralBis() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")

        net.createArc(tIn, pA)
        net.createBridges([pA, pB, pD, pE])
        net.createArc(pE, tOut)

        net.resetIds()
        net
    }

    // I expect a net subsumed itself

    void testSubsumptionIdentity() {
        assert Subsumption.subsumes(net00(), net00())
        assert Subsumption.subsumes(net0(), net0())
        assert Subsumption.subsumes(net1(), net1())
        assert Subsumption.subsumes(net2(), net2())
        assert Subsumption.subsumes(net3(), net3())
        assert Subsumption.subsumes(net4(), net4())
        assert Subsumption.subsumes(net5(), net5())
        assert Subsumption.subsumes(net6(), net6())
        assert Subsumption.subsumes(net7(), net7())
        assert Subsumption.subsumes(net8(), net8())
        assert Subsumption.subsumes(CommonConstructs.inhibitorChoice1(), CommonConstructs.inhibitorChoice1())
        assert Subsumption.subsumes(CommonConstructs.inhibitorChoice2(), CommonConstructs.inhibitorChoice2())
    }

    void testSubsumption1() {
        assert Subsumption.subsumes(net0(), net1())
    }

    void testSubsumption1bis() {
        assert !Subsumption.subsumes(net1(), net0())
    }

    void testSubsumption2() {
        assert !Subsumption.subsumes(CommonConstructs.inhibitorChoice2(), CommonConstructs.inhibitorChoice1())
    }

    void testSubsumption2bis() {
        assert !Subsumption.subsumes(CommonConstructs.inhibitorChoice1(), CommonConstructs.inhibitorChoice2())
    }

    void testAlignment() {

        Map<Story, Map<Story, RelationType>> partialMap
        partialMap = Alignment.partialAlignmentTest(CommonConstructs.inhibitorChoice1(), CommonConstructs.inhibitorChoice2())

        println Alignment.mapToString(partialMap)
    }

    void testAlignment2() {

        Map<Story, Map<Story, RelationType>> partialMap

        partialMap = Alignment.partialAlignmentTest(netGeneral(), netSpecific())
        println "################################"
        println Alignment.mapToString(partialMap)
        println "################################"
        partialMap = Alignment.partialAlignmentTest(netGeneral(), netSpecificBis())
        println "################################"
        println Alignment.mapToString(partialMap)
        println "################################"
        partialMap = Alignment.partialAlignmentTest(netGeneralBis(), netSpecific())
        println "################################"
        println Alignment.mapToString(partialMap)
        println "################################"
        partialMap = Alignment.partialAlignmentTest(netGeneralBis(), netSpecificBis())
        println "################################"
        println Alignment.mapToString(partialMap)
        println "################################"

    }

}