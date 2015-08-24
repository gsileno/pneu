package org.leibnizcenter.pneu.components

import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class StateComparatorTest extends GroovyTestCase {

    void testCompareEq1() {
        Net n1 = new BasicNet()

        Place pIn = n1.createPlace("in")
        pIn.createToken()
        Transition tFunction = n1.createTransition("function")
        Place pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        Place pIn2 = n2.createPlace("in")
        pIn2.createToken()
        Transition tFunction2 = n2.createTransition("function")
        Place pOut2 = n2.createPlace("out")

        n1.resetIds()
        n2.resetIds()

        State state = new State(n1.placeList)
        State state1 = new State(n1.placeList)
        State state2 = new State(n2.placeList)

        assert (state.compare(state1))
        assert (state.compare(state2))

    }

    void testCompareEq2() {
        Net n1 = new BasicNet()

        Place pIn = n1.createPlace("in")
        pIn.createToken()
        Transition tFunction = n1.createTransition("function")
        Place pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        Place pIn2 = n2.createPlace("in")
        pIn2.createToken()
        Transition tFunction2 = n2.createTransition("function")
        Place pOut2 = n2.createPlace("out")

        n1.resetIds()
        n2.resetIds()

        State state = new State(n1.placeList)
        State state1 = state.minimalClone()
        State state2 = new State(n2.placeList)

        assert (state.compare(state1))
        assert (state.compare(state2))
    }

    void testCompareEq3() {
        Net n1 = new BasicNet()
        Place p0 = n1.createPlace("p0")
        p0.createToken()
        Transition t0 = n1.createTransition("t0")
        Transition t1 = n1.createTransition("t1")
        Place p1 = n1.createPlace("p1")
        n1.createBridge(p0, t0, p1)
        n1.createBridge(p0, t1, p1)

        Net n2 = new BasicNet()
        Place p02 = n2.createPlace("p0")
        p02.createToken()
        Transition t02 = n2.createTransition("t0")
        Transition t12 = n2.createTransition("t1")
        Place p12 = n2.createPlace("p1")
        n2.createBridge(p02, t02, p12)
        n2.createBridge(p02, t12, p12)

        n1.resetIds()
        n2.resetIds()

        State state = new State(n1.placeList)
        State state2 = new State(n2.placeList)

        assert (state.compare(state2))

        t0.fire()

        State stateb = new State(n1.placeList)
        State stateb2 = new State(n2.placeList)

        assert (!stateb.compare(stateb2))

        t12.fire()

        State statec = new State(n1.placeList)
        State statec2 = new State(n2.placeList)

        assert (statec.compare(statec2))
    }

    void testCompareNEq1() {
        Net n1 = new BasicNet()

        Place pIn = n1.createPlace("in")
        pIn.createToken()
        Transition tFunction = n1.createTransition("function")
        Place pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        Place pIn2 = n2.createPlace("in")
        pIn2.createToken()
        pIn2.createToken()
        Transition tFunction2 = n2.createTransition("function")
        Place pOut2 = n2.createPlace("out")

        n1.resetIds()
        n2.resetIds()

        assert pIn2.marking.size() == 2

        State state = new State(n1.placeList)
        State state1 = new State(n1.placeList)
        State state2 = new State(n2.placeList)

        assert (state.compare(state1))
        assert (!state.compare(state2))
    }

    void testCompareNEq2() {
        Net n1 = new BasicNet()

        Place pIn = n1.createPlace("in")
        pIn.createToken()
        Transition tFunction = n1.createTransition("function")
        Place pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        Place pIn2 = n2.createPlace("in")
        pIn2.createToken()
        Transition tFunction2 = n2.createTransition("function")
        Place pOut2 = n2.createPlace("out")

        n1.resetIds()
        n2.resetIds()

        State state = new State(n1.placeList)
        State state1 = state.minimalClone()
        State state2 = new State(n2.placeList)

        assert (state.compare(state1))
        assert (state.compare(state2))

        pIn.createToken()
        pIn2.createToken()

        State stateb = new State(n1.placeList)
        State stateb2 = new State(n2.placeList)

        assert (!stateb.compare(state1))
        assert (stateb.compare(stateb2))

        pIn.createToken()

        State statec = new State(n1.placeList)
        State statec2 = new State(n2.placeList)

        assert (!statec.compare(state1))
        assert (!statec.compare(state2))
    }
}