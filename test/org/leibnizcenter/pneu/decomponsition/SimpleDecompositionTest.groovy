package org.leibnizcenter.pneu.decomponsition

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.decomposition.SimpleSESEDecomposer
import org.leibnizcenter.pneu.decomposition.StoryTree

class SimpleDecompositionTest extends GroovyTestCase {

    static StoryTree batchDecompose(Net net) {
        NetRunner runner = new NetRunner()
        runner.load(net)
        runner.analyse()
        runner.status()
        SimpleSESEDecomposer decomposer = new SimpleSESEDecomposer()
        StoryTree tree = decomposer.decompose(runner.analysis)

        assert (runner.analysis.storyBase.base.size() <= tree.getStories().size())

        tree
    }

    // individual path
    void testIndividualPath() {
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
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.individualpath")
        tree.exportToLog("SDT.individualpath")
    }

    // alt with output place
    void testAltWithOutputPlace() {
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
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.altwihtoutputplace")
        tree.exportToLog("SDT.altwihtoutputplace")
    }

    // alt with output transition
    void testAltWithOutputTransition() {
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
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.altwihtoutputtransition")
        tree.exportToLog("SDT.altwihtoutputtransition")
    }

    // alt with no output transition
    void testAltWithNoOutputTransition() {
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
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.altwihtnooutputtransition")
        tree.exportToLog("SDT.altwihtnooutputtransition")
    }

    // seq with alt
    void testSeqWithAlt() {
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
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.seqwithalt")
        tree.exportToLog("SDT.seqwithalt")
    }

    // seq with alt with seq
    void testSeqWithAltWithSeq() {
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
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.seqwithaltwithseq")
        tree.exportToLog("SDT.seqwithaltwithseq")
    }

    // seq with alt with alt
    void testSeqWithAltWithAlt() {
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
        StoryTree tree = batchDecompose(net)
        net.exportToDot("SDT.seqwithaltwithalt.net")
        tree.exportToDot("SDT.seqwithaltwithalt")
        tree.exportToLog("SDT.seqwithaltwithalt")
    }

    // test internal transition alt
    void testInternalTransitionAlt() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pN = net.createPlace("n")
        Place pP = net.createPlace("p")
        Place pR = net.createPlace("r")
        Place pS = net.createPlace("s")
        Place pT = net.createPlace("t")
        Transition tOut = net.createCollectorTransition()

        net.createArc(tIn, pN)
        List<Transition> tList= net.createBridges([pN, pR, pS, pP])
        net.createBridge(tList[0], pT, tList[1])
        net.createArc(pP, tOut)

        net.resetIds()
        net.exportToDot("SDT.internaltransitionalt.net")
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.internaltransitionalt")
        tree.exportToLog("SDT.internaltransitionalt")
    }

    // compound net
    void testCompound() {
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
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.compound")
        tree.exportToLog("SDT.compound")
    }
}