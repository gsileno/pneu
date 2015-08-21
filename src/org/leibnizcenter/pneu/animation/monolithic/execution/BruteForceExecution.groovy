package org.leibnizcenter.pneu.animation.monolithic.execution

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType

// see execution.groovy for implementation notes and literature references

@Log4j
class BruteForceExecution extends Execution {

    // consume and record the collection if needed
    void consumeInputTokens(Transition t) {
        if (t.type == TransitionType.COLLECTOR)
            nTokenCollected++
        t.consumeInputTokens()
    }

    // consume and record the emission if needed
    void produceOutputTokens(Transition t) {
        if (t.type == TransitionType.EMITTER)
            nTokenEmitted++
        t.produceOutputTokens()
    }

    // when you already know which transition to fire (for analysis)
    void fire(Transition t) {
        if (!t.isEnabled()) {
            log.warn("transition $t should be enabled")
        } else {
            consumeInputTokens(t)
        }
        produceOutputTokens(t)
    }

    // ---------------------------------------------------
    // --------- Brute Force algorithm
    // ---------------------------------------------------
    List<Transition> cycle(Boolean includeEmission = true) {
        List<Transition> firedTransitions = []

        // enabling analysis and firing: all transitions are tested
        for (t in transitions) {
            // enabled transition analysis
            if ((!includeEmission && t.isEnabled()) || t.isEnabledIncludingEmission()) {
                // transition firing
                consumeInputTokens(t)
                firedTransitions.add(t)
                break; ////// my modification: only one transition per step!
            }
        }

        // end of firing: no formation lists are processed
        for (t in firedTransitions) {
            produceOutputTokens(t)
            transitions.remove(t) // in order to implement a kind of FIFO mechanism
            transitions.add(t)
        }

        // update lists not necessary in BF
        return firedTransitions
    }

    // if a transition has fired return true
    Boolean step() {
        List<Transition> firedTransitions = cycle()
        return (firedTransitions.size() > 0)
    }

    Boolean stepForAnalysis() {
        List<Transition> firedTransitions = cycle(false)
        return (firedTransitions.size() > 0)
    }

}
