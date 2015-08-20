package org.leibnizcenter.pneu.components.petrinet

import groovy.transform.AutoClone
import org.leibnizcenter.pneu.components.basicpetrinet.BasicToken

abstract class Transition extends Node {

    // transition by default of normal type
    TransitionType type = TransitionType.NORMAL

    boolean isEmitter() {
        return (type == TransitionType.EMITTER)
    }

    boolean isCollector() {
        return (type == TransitionType.COLLECTOR)
    }

    // Operational Semantics
    abstract boolean isEnabled(boolean analysis)
    abstract void fire()
    abstract void consumeInputTokens()
    abstract void produceOutputTokens()


    abstract String toString()

}
