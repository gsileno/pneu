package org.leibnizcenter.pneu.animation

import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration
import org.leibnizcenter.pneu.animation.monolithic.execution.ExecutionMode
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

class BasicSimulationTest extends GroovyTestCase {

    void test0EmptyPlace() {
        Net net = PNML2PN.parseFile("examples/basic/0emptyplace.pnml")

        NetOrchestration orchestration = new NetOrchestration()
        orchestration.load(net)
        assert(orchestration.run() == 0)
        assert(orchestration.execution.places.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl21' }.marking.size() == 0)

    }

    void test0PlaceFilledWith3Tokens() {
        Net net = PNML2PN.parseFile("examples/basic/0placefilledwith3tokens.pnml")

        NetOrchestration orchestration = new NetOrchestration()
        orchestration.load(net)
        assert(orchestration.run() == 0)
        assert(orchestration.execution.places.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl21' }.marking.size() == 3)
    }

    // test for execution based on transitions
    // only one transition is fired per step.
    // therefore at 100 there is no token in the place (collector consumed it)
    // at 101 there is a token (emitter consumed it)
    void test1TransitionBased(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/basic/1transition.pnml")
        orchestration.load(net)
        assert(orchestration.run(100) == 100)
        assert(orchestration.execution.places.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 0)
        assert(orchestration.run(1) == 1)
        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 1)
        assert(orchestration.execution.nTokenEmitted == orchestration.execution.nTokenCollected + 1)
    }

    void test1TransitionBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        test1TransitionBased(orchestration)
    }

//    void test1TransitionEnabledTransitions() {
//        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.EnabledTransition)
//        test1TransitionBased(orchestration)
//    }

    // test for execution based on places
//    void test1PlaceBased(NetOrchestration orchestration) {
//        Net net = PNML2PN.parseFile("examples/basic/1transition.pnml")
//        orchestration.load(net)
//        // this execution does not handle emitters,
//        assert(orchestration.run() == 0)
//        // so we have to artificially create tokens
//        net.placeList.find { it.id == 'pl4' }.marking += [new Token()] * 100
//        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 100)
//        // and reset the marked representing places
//        orchestration.execution.resetMarkedRepresentingPlaces()
//        // it consumes it in just one turn
//        assert(orchestration.run() == 100)
//        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 0)
//        assert(orchestration.execution.nTokenEmitted == orchestration.execution.nTokenCollected - 20)
//    }

//    void test1TransitionStaticRepresentingPlaces() {
//        Net net = PNMLparser.parseFile("examples/basic/1transition.pnml")
//
//        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.StaticRepresentingPlaces)
//        test1PlaceBased(net, orchestration)
//
//    }
//
//    void test1TransitionDynamicRepresentingPlaces() {
//        Net net = PNMLparser.parseFile("examples/basic/1transition.pnml")
//
//        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.DynamicRepresentingPlaces)
//        test1PlaceBased(net, orchestration)
//    }

    // test for execution based on transitions
    void test2TransitionBased(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/basic/2chaining.pnml")
        orchestration.load(net)
        assert(orchestration.run() == 100)
        assert(orchestration.execution.places.size() == 2)
        assert(orchestration.execution.places.find { it.id == 'pl1' }.marking.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl3' }.marking.size() == 0)
        assert(orchestration.run(1) == 1)
        assert(orchestration.execution.places.find { it.id == 'pl1' }.marking.size() == 0)
        assert(orchestration.execution.places.find { it.id == 'pl3' }.marking.size() == 1)
        assert(orchestration.execution.nTokenEmitted == orchestration.execution.nTokenCollected + 1)
    }

    void test2ChainingBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        test2TransitionBased(orchestration)
    }

//    void test2ChainingEnabledTransitions() {
//        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.EnabledTransition)
//        test2TransitionBased(orchestration)
//    }

    // test for execution based on transitions
    void test3TransitionBased(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/basic/3doublearc.pnml")
        orchestration.load(net)
        assert(orchestration.run() == 100)
        assert(orchestration.execution.places.size() == 4)
        assert(orchestration.execution.places.find { it.id == 'pl1' }.marking.size() == 46)
        assert(orchestration.execution.places.find { it.id == 'pl2' }.marking.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 0)
        assert(orchestration.run(2) == 2)
        assert(orchestration.execution.places.find { it.id == 'pl1' }.marking.size() == 47)
        assert(orchestration.execution.places.find { it.id == 'pl3' }.marking.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 0)
        assert(orchestration.execution.nTokenEmitted == orchestration.execution.nTokenCollected + 38)
    }

    void test3DoubleArcBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        test3TransitionBased(orchestration)
    }

//    void test3DoubleArcEnabledTransitions() {
//        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.EnabledTransition)
//        test3TransitionBased(orchestration)
//    }

    // test for execution based on transitions
    void test4TransitionBased(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/basic/4conflict.pnml")
        orchestration.load(net)
        assert(orchestration.execution.places.size() == 3)
        assert(orchestration.run(1))
        assert(orchestration.execution.places.find { it.id == 'pl2' }.marking.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl3' }.marking.size() == 0)
        assert(orchestration.run(1))
        assert(orchestration.execution.places.find { it.id == 'pl2' }.marking.size() == 0)
        assert(orchestration.execution.places.find { it.id == 'pl3' }.marking.size() == 1)
        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 0)
        assert(orchestration.run(101))
        assert(orchestration.execution.nTokenEmitted == orchestration.execution.nTokenCollected+1)
    }

    void test4ConflictBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        test4TransitionBased(orchestration)
    }

//    void test4ConflictEnabledTransitions() {
//        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.EnabledTransition)
//        test4TransitionBased(orchestration)
//    }

    // test for execution based on transitions
    void test5TransitionBased(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/basic/5inhibitor.pnml")
        orchestration.load(net)
        assert(orchestration.run() == 100)

        assert(orchestration.execution.places.find { it.id == 'pl4' }.marking.size() == 100)
        assert(orchestration.execution.places.find { it.id == 'pl3' }.marking.size() == 0)
        assert(orchestration.execution.places.find { it.id == 'pl2' }.marking.size() == 100)
        assert(orchestration.execution.nTokenEmitted == 100)
        assert(orchestration.execution.nTokenCollected == 0)
    }

    void test5InhibitorBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        test5TransitionBased(orchestration)
    }

    void test5InhibitorEnabledTransitions() {
        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.EnabledTransition)
        test5TransitionBased(orchestration)
    }

    // test for execution based on transitions
    void test6TransitionBased(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/basic/6biflow.pnml")
        orchestration.load(net)
        assert(orchestration.run() == 100)
    }

    void test6BiflowBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        test6TransitionBased(orchestration)
    }
    void test6BiflowEnabledTransitions() {
        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.EnabledTransition)
        test6TransitionBased(orchestration)
    }

    void test7TransitionBased(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/basic/7reset.pnml")
        orchestration.load(net)
        assert(orchestration.run() == 100)
    }

    void test7ResetBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        test7TransitionBased(orchestration)
    }
//
//    void test7ResetEnabledTransitions() {
//        NetOrchestration orchestration = new NetOrchestration(ExecutionMode.EnabledTransition)
//        test7TransitionBased(orchestration)
//    }
}