package org.leibnizcenter.pneu.execution

import org.leibnizcenter.pneu.components.Net
import org.leibnizcenter.pneu.parser.pneu

class BaseTest extends GroovyTestCase {

//    void test0EmptyPlace() {
//        Net net = pneu.parseFile("examples/basic/0emptyplace.pnml")
//
//        NetChoreography choreography = new NetChoreography()
//        choreography.embody(net)
//        choreography.start()
//        assert(choreography.getPlaceActorById('pl21').nTokesAvailable == 0)
//    }
//
//    void test0PlaceFilledWith3Tokens() {
//        Net net = pneu.parseFile("examples/basic/0placefilledwith3tokens.pnml")
//
//        NetChoreography choreography = new NetChoreography()
//        choreography.embody(net)
//        choreography.start()
//        assert(choreography.getPlaceActorById('pl21').nTokesAvailable == 3)
//    }
//
//    void test1Transition() {
//        Net net = pneu.parseFile("examples/basic/1transition.pnml")
//
//        NetChoreography choreography = new NetChoreography()
//        choreography.embody(net)
//        choreography.start()
//        choreography.emit()
//        def outcome = choreography.outcome()
//
//        assert(outcome["emitted"]["tr1"] == outcome["collected"]["tr11"])
//    }
//
//    void test1Transition2() { // more tokens
//        Net net = pneu.parseFile("examples/basic/1transition.pnml")
//
//        NetChoreography choreography = new NetChoreography()
//        choreography.embody(net)
//        choreography.start()
//        choreography.emit(10)
//
//        choreography.pause(100)
//        def outcome = choreography.outcome()
//
//        assert(outcome["emitted"]["tr1"] == outcome["collected"]["tr11"])
//    }
//
//    void test1Transition3() { // more tokens, faster
//        Net net = pneu.parseFile("examples/basic/1transition.pnml")
//
//        NetChoreography choreography = new NetChoreography()
//        choreography.embody(net)
//        choreography.start(10)
//        choreography.emit(10, 10)
//
//        choreography.pause(100)
//        def outcome = choreography.outcome()
//
//        assert(outcome["emitted"]["tr1"] == outcome["collected"]["tr11"])
//    }
//
//    void test1Transition4() { // a lot more tokens, 0 delay
//        Net net = pneu.parseFile("examples/basic/1transition.pnml")
//
//        NetChoreography choreography = new NetChoreography()
//        choreography.embody(net)
//        choreography.start(0)
//        choreography.emit(100, 0)
//
//        choreography.pause(100)
//        def outcome = choreography.outcome()
//
//        assert(outcome["emitted"]["tr1"] == outcome["collected"]["tr11"])
//    }

//    void test2Chaining() {
//        Net net = pneu.parseFile("examples/basic/2chaining.pnml")
//
//        NetChoreography choreography = new NetChoreography()
//        choreography.embody(net)
//        choreography.start()
//        choreography.emit()
//
//        choreography.pause(100)
//        def outcome = choreography.outcome()
//
//        assert(outcome["emitted"]["tr1"] == outcome["collected"]["tr10"])
//    }

    void test4Conflict() {
        Net net = pneu.parseFile("examples/basic/4conflict.pnml")

        NetChoreography choreography = new NetChoreography()
        choreography.embody(net)
        choreography.start()
        choreography.emit()

        choreography.pause(100)
        def outcome = choreography.outcome()

        // assert(outcome["emitted"]["tr1"] == outcome["collected"]["tr10"])
    }

}