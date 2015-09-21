package org.leibnizcenter.pneu.subsumption

import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.subsumption.StorySubsumption
import org.leibnizcenter.pneu.subsumption.Subsumption
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
        net.createBridgesForPlaces([pA, pB, pC, pF])
        net.createBridgesForPlaces([pA, pD, pE, pF])
        net.createBridge(pF, pG)
        net.createBridgesForPlaces([pF, pH, pI, pL])

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
        net.createBridgesForPlaces([pA, pB, pC, pF])
        net.createBridgesForPlaces([pA, pD, pE, pF])
        net.createBridge(pF, pG)
        net.createBridgesForPlaces([pF, pH, pI, pL])
        net.createBridgesForPlaces([pF, pM, pN, pO, pP, pQ])

        List<Transition> tList= net.createBridgesForPlaces([pN, pR, pS, pP])
        net.createBridge(tList[0], pT, tList[1])

        net.resetIds()
        net
    }

    // a - b - c - d - e
    static Net transitionNetSpecific() {
        Net net = new BasicNet()

        Place pIn = net.createLinkPlace()
        Place pOut = net.createLinkPlace()
        pIn.createToken()

        Transition tA = net.createTransition("a")
        Transition tB = net.createTransition("b")
        Transition tC = net.createTransition("c")
        Transition tD = net.createTransition("d")
        Transition tE = net.createTransition("e")

        net.createArc(pIn, tA)
        net.createArc(tE, pOut)

        net.createsBridgesForTransitions([tA, tB, tC, tD, tE])
        net.resetIds()
        net
    }

    // b - c - d
    static Net transitionNetSpecificBis() {
        Net net = new BasicNet()

        Place pIn = net.createLinkPlace()
        Place pOut = net.createLinkPlace()
        pIn.createToken()

        Transition tB = net.createTransition("b")
        Transition tC = net.createTransition("c")
        Transition tD = net.createTransition("d")

        net.createArc(pIn, tB)
        net.createArc(tD, pOut)
        net.createsBridgesForTransitions([tB, tC, tD])
        net.resetIds()
        net
    }

    // an abstraction of the previous one (b - d)
    static Net transitionNetGeneral() {
        Net net = new BasicNet()

        Place pIn = net.createLinkPlace()
        Place pOut = net.createLinkPlace()
        pIn.createToken()

        Transition tB = net.createTransition("b")
        Transition tD = net.createTransition("d")

        net.createArc(pIn, tB)
        net.createArc(tD, pOut)
        net.createsBridgesForTransitions([tB, tD])
        net.resetIds()
        net
    }

    // an abstraction of the previous one, with different terminals (a - b - d - e)
    static Net transitionNetGeneralBis() {
        Net net = new BasicNet()

        Place pIn = net.createLinkPlace()
        Place pOut = net.createLinkPlace()
        pIn.createToken()

        Transition tA = net.createTransition("a")
        Transition tB = net.createTransition("b")
        Transition tD = net.createTransition("d")
        Transition tE = net.createTransition("e")

        net.createArc(pIn, tA)
        net.createArc(tE, pOut)
        net.createsBridgesForTransitions([tA, tB, tD, tE])
        net.resetIds()
        net
    }

    // a - b - c - d - e
    static Net placeNetSpecific() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")

        net.createArc(tIn, pA)
        net.createBridgesForPlaces([pA, pB, pC, pD, pE])
        net.createArc(pE, tOut)

        net.resetIds()
        net
    }

    // a portion of the previous one: b - c - d
    static Net placeNetSpecificBis() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pB = net.createPlace("b")
        Place pC = net.createPlace("c")
        Place pD = net.createPlace("d")

        net.createArc(tIn, pB)
        net.createBridgesForPlaces([pB, pC, pD])
        net.createArc(pD, tOut)

        net.resetIds()
        net
    }

    // an abstraction of the previous one (b - d)
    static Net placeNetGeneral() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pB = net.createPlace("b")
        Place pD = net.createPlace("d")

        net.createArc(tIn, pB)
        net.createBridgesForPlaces([pB, pD])
        net.createArc(pD, tOut)

        net.resetIds()
        net
    }

    // an abstraction of the previous one, with different terminals (a - b - d - e)
    static Net placeNetGeneralBis() {
        Net net = new BasicNet()
        Transition tIn = net.createEmitterTransition()
        Transition tOut = net.createCollectorTransition()
        Place pA = net.createPlace("a")
        Place pB = net.createPlace("b")
        Place pD = net.createPlace("d")
        Place pE = net.createPlace("e")

        net.createArc(tIn, pA)
        net.createBridgesForPlaces([pA, pB, pD, pE])
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

    // a   vs   a - b - c
    void testSubsumption1() {
        assert Subsumption.subsumes(net0(), net1())
    }

    // a - b - c   vs   a
    void testSubsumption1bis() {
        assert !Subsumption.subsumes(net1(), net0())
    }

    void testSubsumption2() {
        assert Subsumption.subsumes(CommonConstructs.inhibitorChoice1(), CommonConstructs.inhibitorChoice2())
    }

    void testSubsumption2bis() {
        assert !Subsumption.subsumes(CommonConstructs.inhibitorChoice2(), CommonConstructs.inhibitorChoice1())
    }

    void testInhibitorChoiceSubsumption() {

        StorySubsumption outcome

        // the two models represent a binary mutually-exclusive choice
        // choice 1 uses inhibitor arcs
        // choice 2 don't, but use an explicit negative place

        // in principle the first should be more generic than the first one, as we don't use
        // an explicit negative place. this test shows that our definition of subsumption is correct

        Analysis inhibitorChoice1Analysis = Analysis.analyse(CommonConstructs.inhibitorChoice1())
        Analysis inhibitorChoice2Analysis = Analysis.analyse(CommonConstructs.inhibitorChoice2())

        Story inhibitorChoice1Story = inhibitorChoice1Analysis.storyBase.base[0]
        Story inhibitorChoice2Story = inhibitorChoice2Analysis.storyBase.base[1]

        outcome = new StorySubsumption(inhibitorChoice1Story, inhibitorChoice2Story)

        assert outcome.type() == StorySubsumption.Type.SUBSUMES

        outcome = new StorySubsumption(inhibitorChoice2Story, inhibitorChoice1Story)

        // (st0) -- * -- (st1) -- not c -- (st4) -- e2 -- (st5) <-
        // (st0) -- * -- (st1) -- e2 -- (st2)
        // ? PARTIALLY_SUBSUMES (0, 0) <- (0, 0)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES
        assert outcome.leftGeneralLimit == 0
        assert outcome.rightGeneralLimit == 0
        assert outcome.leftSpecificLimit == 0
        assert outcome.rightSpecificLimit == 0

        println inhibitorChoice1Analysis.toLog()
        println inhibitorChoice2Analysis.toLog()

        inhibitorChoice1Story = inhibitorChoice1Analysis.storyBase.base[1]
        inhibitorChoice2Story = inhibitorChoice2Analysis.storyBase.base[0]

        outcome = new StorySubsumption(inhibitorChoice2Story, inhibitorChoice1Story)

        assert outcome.type() == StorySubsumption.Type.SUBSUMES

        outcome = new StorySubsumption(inhibitorChoice1Story, inhibitorChoice2Story)

        assert outcome.type() == StorySubsumption.Type.SUBSUMES

    }

    void testTransitionStorySubsumption() {

        StorySubsumption outcome

        Analysis generalAnalysis = Analysis.analyse(transitionNetGeneral())
        Analysis specificAnalysis = Analysis.analyse(transitionNetSpecific())

        assert generalAnalysis.storyBase.size == 1
        assert specificAnalysis.storyBase.size == 1

        // b - d    vs    a - b - c - d - e

        Story generalStory = generalAnalysis.storyBase.base[0]
        Story specificStory = specificAnalysis.storyBase.base[0]

        outcome = new StorySubsumption(generalStory, specificStory)

        assert outcome.type() == StorySubsumption.Type.SUBSUMES
        assert outcome.leftGeneralLimit == 0
        assert outcome.rightGeneralLimit == 2
        assert outcome.leftSpecificLimit == 1
        assert outcome.rightSpecificLimit == 4

        outcome = new StorySubsumption(specificStory, generalStory)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES
        assert outcome.leftGeneralLimit == 1
        assert outcome.rightGeneralLimit == 1
        assert outcome.leftSpecificLimit == 0
        assert outcome.rightSpecificLimit == 0

    }

    void testTransitionStorySubsumption2() {

        Analysis generalAnalysis = Analysis.analyse(transitionNetGeneral())
        Analysis specificAnalysis = Analysis.analyse(transitionNetSpecificBis())

        assert generalAnalysis.storyBase.size == 1
        assert specificAnalysis.storyBase.size == 1

        // b - d    vs    b - c - d

        Story generalStory = generalAnalysis.storyBase.base[0]
        Story specificStory = specificAnalysis.storyBase.base[0]

        StorySubsumption outcome

        outcome = new StorySubsumption(generalStory, specificStory)

        assert outcome.type() == StorySubsumption.Type.SUBSUMES
        assert outcome.leftGeneralLimit == 0
        assert outcome.rightGeneralLimit == 2
        assert outcome.leftSpecificLimit == 0
        assert outcome.rightSpecificLimit == 3

        outcome = new StorySubsumption(specificStory, generalStory)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES
        assert outcome.leftGeneralLimit == 0
        assert outcome.rightGeneralLimit == 0
        assert outcome.leftSpecificLimit == 0
        assert outcome.rightSpecificLimit == 0

    }

    void testTransitionStorySubsumption3() {

        Analysis generalAnalysis = Analysis.analyse(transitionNetGeneralBis())
        Analysis specificAnalysis = Analysis.analyse(transitionNetSpecific())

        assert generalAnalysis.storyBase.size == 1
        assert specificAnalysis.storyBase.size == 1

        // a - b - d - e   vs    a - b - c - d - e

        Story generalStory = generalAnalysis.storyBase.base[0]
        Story specificStory = specificAnalysis.storyBase.base[0]

        StorySubsumption outcome

        outcome = new StorySubsumption(generalStory, specificStory)

        assert outcome.type() == StorySubsumption.Type.SUBSUMES
        assert outcome.leftGeneralLimit == 0
        assert outcome.rightGeneralLimit == 4
        assert outcome.leftSpecificLimit == 0
        assert outcome.rightSpecificLimit == 5

        outcome = new StorySubsumption(specificStory, generalStory)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES
        assert outcome.leftGeneralLimit == 0
        assert outcome.rightGeneralLimit == 1
        assert outcome.leftSpecificLimit == 0
        assert outcome.rightSpecificLimit == 1

    }

    void testTransitionStorySubsumption4() {

        Analysis generalAnalysis = Analysis.analyse(transitionNetGeneralBis())
        Analysis specificAnalysis = Analysis.analyse(transitionNetSpecificBis())

        assert generalAnalysis.storyBase.size == 1
        assert specificAnalysis.storyBase.size == 1

        // a - b - d - e   vs    b - c - d

        Story generalStory = generalAnalysis.storyBase.base[0]
        Story specificStory = specificAnalysis.storyBase.base[0]

        StorySubsumption outcome

        outcome = new StorySubsumption(generalStory, specificStory)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES
        assert outcome.leftGeneralLimit == 1
        assert outcome.rightGeneralLimit == 3
        assert outcome.leftSpecificLimit == 0
        assert outcome.rightSpecificLimit == 3

        outcome = new StorySubsumption(specificStory, generalStory)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES
        assert outcome.leftGeneralLimit == 0
        assert outcome.rightGeneralLimit == 0
        assert outcome.leftSpecificLimit == 1
        assert outcome.rightSpecificLimit == 1

    }
}