package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

class BasicTransition extends Transition {

    // Operational Semantics

    Boolean isEnabledForAnalysis() {
        if ( inputs.size ( ) == 0 ) {
            return (isEmitter())
        }
        isEnabled()
    }

    Boolean isEnabled() {
        for (elem in inputs) {
            // inhibitor
            if (elem.type == ArcType.INHIBITOR) {
                if (((BasicPlace) elem.source).marking.size() >= elem.weight) {
                    return false
                }
            } else if (elem.type == ArcType.NORMAL) {
                if (((BasicPlace) elem.source).marking.size() < elem.weight) {
                    return false
                }
            } else {
                throw new RuntimeException("Not yet implemented.")
            }
        }
        return true
    }

    void fire() {
        List<BasicToken> tokens
        tokens = consumeInputTokens()
        produceOutputTokens(tokens)
    }

    List<BasicToken> consumeInputTokens() {
        List<BasicToken> tokens = []
        for (elem in inputs) {
            for (int i=0; i<elem.weight; i++) {
                tokens << ((BasicPlace) elem.source).marking.pop()
            }
        }
        tokens
    }

    void produceOutputTokens(List<Token> tokens) { // TOCHECK, I shoulnd't have the abstract here
        for (elem in outputs) {
            if (elem.type == ArcType.NORMAL) {
                for (int i = 0; i < elem.weight; i++) {
                    ((BasicPlace) elem.target).marking.push(new BasicToken())
                }
            } else if (elem.type == ArcType.RESET) {
                ((BasicPlace) elem.target).flush()
            } else {
                throw new RuntimeException("Not yet implemented.")
            }
        }
    }

    static Boolean compare(Transition t1, Transition t2) {
        if (t1 == t2) return true
        if (t1.name != t2.name) return false
        return true
    }

    BasicTransition clone() {
        return new BasicTransition(name: name)
    }

    String toString() {
        if (name != "") return name
        else return id
    }

}
