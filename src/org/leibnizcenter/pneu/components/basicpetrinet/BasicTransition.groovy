package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class BasicTransition extends Transition {

    // Operational Semantics
    boolean isEnabled(boolean analysis = false) {

        if (inputs.size() == 0) {
            if (!analysis && isEmitter())
                return true
            else
                return false
        }

        for (elem in inputs) {
            // inhibitor
            if (elem.type == ArcType.INHIBITOR) {
                if (((Place) elem.source).marking.size() >= elem.weight) {
                    return false
                }
            } else if (elem.type == ArcType.NORMAL) {
                if (((Place) elem.source).marking.size() < elem.weight) {
                    return false
                }
            }
        }
        return true
    }

    void fire() {
        consumeInputTokens()
        produceOutputTokens()
    }

    void consumeInputTokens() {
        for (elem in inputs) {
            for (int i=0; i<elem.weight; i++) {
                ((Place) elem.source).marking.pop()
            }
        }
    }

    void produceOutputTokens() {
        for (elem in outputs) {
            if (elem.type == ArcType.NORMAL) {
                for (int i = 0; i < elem.weight; i++) {
                    ((Place) elem.target).marking.push(new BasicToken())
                }
            } else if (elem.type == ArcType.INHIBITOR) {
                ((Place) elem.target).flush()
            }
        }
    }

    String toString() {
        if (name != "") return name
        else return id
    }

}
