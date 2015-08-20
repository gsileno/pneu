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

    // Operational Semantics
    abstract Boolean isEnabledForAnalysis()
    abstract Boolean isEnabled()
    abstract void fire()
    abstract List<Token> consumeInputTokens()
    abstract void produceOutputTokens(List<Token> tokens)

    abstract String toString()

    abstract Transition clone()

    abstract Boolean compare(Transition t1, Transition t2)

}
