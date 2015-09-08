package org.leibnizcenter.pneu.decomponsition

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.decomposition.SimpleSESEDecomposer
import org.leibnizcenter.pneu.decomposition.StoryTree
import org.leibnizcenter.pneu.decomposition.Subsumption

class SubsumptionTest extends GroovyTestCase {

    static Net net00() {
        Net net = new BasicNet()
        net
    }

    static Net net0() {
        Net net = new BasicNet()
        Place pA = net.createPlace("a")
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
    }


    void testSubsumption1() {
        assert Subsumption.subsumes(net0(), net1())
    }


}