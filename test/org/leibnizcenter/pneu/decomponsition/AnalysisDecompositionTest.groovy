package org.leibnizcenter.pneu.decomponsition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.decomposition.AnalysisSESEDecomposer
import org.leibnizcenter.pneu.decomposition.StoryTree
import org.leibnizcenter.pneu.decomposition.StoryTreeType

class AnalysisDecompositionTest extends GroovyTestCase {

    static StoryTree batchDecompose(Net net) {
        NetRunner runner = new NetRunner()
        runner.load(net)
        runner.analyse()
        runner.status()
        AnalysisSESEDecomposer decomposer = new AnalysisSESEDecomposer()
        StoryTree tree = decomposer.decompose(runner.analysis)

        assert (runner.analysis.storyBase.base.size() <= tree.getStories().size())

//      TODO: inverse decomposition
//        Analysis analysis = AnalysisSESEDecomposer.compose(tree)
//        assert (Analysis.compare(runner.analysis, analysis))

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
        tree.exportToDot("SDT.individualPath")
        tree.exportToLog("SDT.individualPath")

        assert tree.type == null
        assert tree.story.steps.size() == 5
    }

    // individual path with two collectors
    void testIndividualPathWithTwoCollectors() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Transition tOut1 = net.createCollectorTransition()
        Transition tOut2 = net.createCollectorTransition()

        net.createArc(tIn, pA)
        net.createBridge(pA, pB)
        net.createArc(pB, tOut1)
        net.createBridge(pB, pC)
        net.createArc(pC, tOut2)

        net.resetIds()
        StoryTree tree = batchDecompose(net)
        tree.exportToDot("SDT.individualPathWithTwoCollectors")
        tree.exportToLog("SDT.individualPathWithTwoCollectors")

        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves[0].story.steps.size() == 3
        assert tree.leaves[1].leaves[0].story.steps.size() == 3
        assert tree.leaves[1].leaves[1].story.steps.size() == 2

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
        tree.exportToDot("SDT.altWithOutputPlace")
        tree.exportToLog("SDT.altWithOutputPlace")

        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves[0].story.steps.size() == 2
        assert tree.leaves[1].leaves[0].story.steps.size() == 4
        assert tree.leaves[1].leaves[1].story.steps.size() == 4
        assert tree.leaves[2].story.steps.size() == 2
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
        tree.exportToDot("SDT.altWihtOutputTransition")
        tree.exportToLog("SDT.altWihtOutputTransition")

        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves[0].story.steps.size() == 2
        assert tree.leaves[1].leaves[0].story.steps.size() == 3
        assert tree.leaves[1].leaves[1].story.steps.size() == 3
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

        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves[0].story.steps.size() == 2
        assert tree.leaves[1].leaves[0].story.steps.size() == 3
        assert tree.leaves[1].leaves[1].story.steps.size() == 3
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

        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves[0].story.steps.size() == 3
        assert tree.leaves[1].leaves[0].story.steps.size() == 4
        assert tree.leaves[1].leaves[1].story.steps.size() == 4
        assert tree.leaves[2].story.steps.size() == 2
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

        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves[0].story.steps.size() == 3
        assert tree.leaves[1].leaves[0].story.steps.size() == 4
        assert tree.leaves[1].leaves[1].story.steps.size() == 4
        assert tree.leaves[2].story.steps.size() == 3
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

        assert tree.leaves.size() == 3
        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves[0].story.steps.size() == 3
        assert tree.leaves[1].type == StoryTreeType.ALT
        assert tree.leaves[2].type == StoryTreeType.ALT
        assert tree.leaves[1].leaves.size() == 2
        assert tree.leaves[2].leaves.size() == 2
        assert tree.leaves[1].leaves[0].story.steps.size() == 4
        assert tree.leaves[1].leaves[1].story.steps.size() == 4
        assert tree.leaves[2].leaves[0].story.steps.size() == 4
        assert tree.leaves[2].leaves[1].story.steps.size() == 2

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

        assert tree.type == StoryTreeType.SEQ
        assert tree.leaves.size() == 3
        assert tree.leaves[0].story.steps.size() == 3
        assert tree.leaves[1].type == StoryTreeType.ALT
        assert tree.leaves[1].leaves.size() == 2
        assert tree.leaves[1].leaves[0].story.steps.size() == 4
        assert tree.leaves[1].leaves[1].story.steps.size() == 4
        assert tree.leaves[2].type == StoryTreeType.ALT
        assert tree.leaves[2].leaves.size() == 3
        assert tree.leaves[2].leaves[1].story.steps.size() == 4
        assert tree.leaves[2].leaves[2].story.steps.size() == 2
        assert tree.leaves[2].leaves[0].type == StoryTreeType.SEQ
        assert tree.leaves[2].leaves[0].leaves.size() == 3
        assert tree.leaves[2].leaves[0].leaves[0].story.steps.size() == 3
        assert tree.leaves[2].leaves[0].leaves[1].type == StoryTreeType.ALT
        assert tree.leaves[2].leaves[0].leaves[1].leaves.size() == 2
        assert tree.leaves[2].leaves[0].leaves[1].leaves[0].story.steps.size() == 4
        assert tree.leaves[2].leaves[0].leaves[1].leaves[1].story.steps.size() == 3
        assert tree.leaves[2].leaves[0].leaves[2].story.steps.size() == 2

    }

}