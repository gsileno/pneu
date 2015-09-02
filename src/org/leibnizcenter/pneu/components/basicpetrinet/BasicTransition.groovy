package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

class BasicTransition extends Transition {

    String name

    static Boolean compare(Transition t1, Transition t2) {
        if (t1 == t2) return true
        if (((BasicTransition) t1).name != ((BasicTransition) t2).name) return false
        return true
    }

    static Transition build(String label) {
        return new BasicTransition(name: label)
    }

    Transition minimalClone() {
        return new BasicTransition(name: name)
    }

    String toString() {
        if (name != null) name
        else id
    }

    String label() {
        if (name != null) name
        else ""
    }

    //////////////////////////////
    // Operational Semantics
    //////////////////////////////

    Boolean isEnabledIncludingEmission() {
        if (inputs.size() == 0 && isEmitter())
            true
        else
            isEnabled()
    }

    Boolean isEnabled() {

        // cut out not emitters
        if (inputs.size() == 0) return false

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
            } else {
                throw new RuntimeException("Not yet implemented.")
            }
        }
        return true
    }

    Token fire() {
        consumeInputTokens()
        produceOutputTokens()
    }

    void consumeInputTokens() {
        for (arc in inputs) {
            if (arc.type == ArcType.NORMAL) {
                for (int i = 0; i < arc.weight; i++) {
                    ((BasicPlace) arc.source).marking.pop()
                }
            }
        }
    }

    Token produceOutputTokens() {

        Token content = new BasicToken()

        for (elem in outputs) {
            if (elem.type == ArcType.NORMAL) {
                for (int i = 0; i < elem.weight; i++) {
                    ((BasicPlace) elem.target).marking.push(content.minimalClone())
                }
            } else if (elem.type == ArcType.RESET) {
                ((BasicPlace) elem.target).flush()
            } else {
                throw new RuntimeException("Not yet implemented.")
            }
        }
    }

}
