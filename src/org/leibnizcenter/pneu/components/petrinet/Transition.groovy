package org.leibnizcenter.pneu.components.petrinet

import groovy.transform.AutoClone
import org.leibnizcenter.pneu.components.basicpetrinet.BasicToken

abstract class Transition extends Node {

    // transition by default of normal type
    TransitionType type = TransitionType.NORMAL

    Boolean isEmitter() {
        return (type == TransitionType.EMITTER)
    }

    Boolean isCollector() {
        return (type == TransitionType.COLLECTOR)
    }

    // useful functions
    abstract Transition minimalClone()

    abstract Boolean compare(Transition t1, Transition t2)

    // operational semantics
    abstract Boolean isEnabledIncludingEmission()

    abstract Boolean isEnabled()

    abstract Token fire()

    abstract void consumeInputTokens()

    // it returns the content produced as
    // a token with the label of the transition
    // (no anonymous variables generated for the places)
    abstract Token produceOutputTokens()

}
