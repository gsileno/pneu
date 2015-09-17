package org.leibnizcenter.pneu.components.basicpetrinet

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent

@Log4j
class BasicTransition extends Transition {

    String name

    Boolean compare(Transition other) {
        compare(this, other)
    }

    static Boolean compare(Transition t1, Transition t2) {
        log.trace("Comparing ${t1} with ${t2}")

        BasicTransition lt1 = (BasicTransition) t1
        BasicTransition lt2 = (BasicTransition) t2

        if (t1 == t2) { log.trace("They are the same object"); return true }
        if (lt1.name != lt2.name) { log.trace("They have a different name"); return false }
        if (lt1.id != lt2.id) { log.trace("They have a different id"); return false }
        return true
    }

    Boolean subsumes(Transition t) {
        BasicTransition lt = (BasicTransition) t
        if (name != lt.name) return false
        return true
    }

    static Transition build(String label) {
        return new BasicTransition(name: label)
    }

    Transition minimalClone() {
        return new BasicTransition(name: name, id: id)
    }

    Boolean isLink() {
        name == null
    }

    String toString() {
        if (name != null && name != "") name
        else id+(isLink()?"*":"")
    }

    String label() {
        if (name != null) name
        else if (isLink()) "*"
        else throw new RuntimeException("You should not be here")
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

    TransitionEvent fire() {
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

    TransitionEvent produceOutputTokens() {

        Token content = new BasicToken()
        TransitionEvent event = new TransitionEvent(transition: this, token: content)

        for (elem in outputs) {
            if (elem.type == ArcType.NORMAL) {
                for (int i = 0; i < elem.weight; i++) {
                    ((BasicPlace) elem.target).marking.add(content.minimalClone())
                }
            } else if (elem.type == ArcType.RESET) {
                ((BasicPlace) elem.target).flush()
            } else {
                throw new RuntimeException("Not yet implemented.")
            }
        }

        event
    }

    //  for basic petri nets, the tokens do not contain any data, their are all equals
    TransitionEvent fire(TransitionEvent event) {
        fire()
    }

    TransitionEvent produceOutputTokens(TransitionEvent event) {
        produceOutputTokens()
    }

    void consumeInputTokens(TransitionEvent event) {
        consumeInputTokens()
    }

    List<TransitionEvent> fireableEvents() {
        if (isEnabledIncludingEmission()) [new TransitionEvent(transition: this, token: new BasicToken())]
        else []
    }
}