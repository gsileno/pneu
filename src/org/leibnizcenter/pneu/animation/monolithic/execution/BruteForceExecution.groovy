package org.leibnizcenter.pneu.animation.monolithic.execution

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.animation.monolithic.analysis.StateBase
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType

// see execution.groovy for implementation notes and literature references

@Log4j
class BruteForceExecution extends Execution {

    void consumeInputTokens(Transition t) {
        if (t.type == TransitionType.COLLECTOR)
            nTokenCollected++
        t.consumeInputTokens()
        t.flushResetTokens()
    }

    void produceOutputTokens(Transition t) {
        if (t.type == TransitionType.EMITTER)
            nTokenEmitted++
        t.produceOutputTokens()
    }

    List<Transition> fire(Transition t) {
        // log.info("firing $t")
        if (!t.isEnabled()) {
            log.error("transition $t should be enabled")
        } else {
            consumeInputTokens(t)
        }
        produceOutputTokens(t)
        return [t]
    }

    // Brute Force algorithm
    List<Transition> transitionStep() {
        List<Transition> firedTransitions = []

        // enabling analysis and firing: all transitions are tested
        for (t in transitions) {
            // enabled transition analysis
            if (t.isEnabled()) {
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

    Boolean step() {
        List<Transition> firedTransitions = []
        firedTransitions = transitionStep()
        return (firedTransitions.size() > 0)
    }

}
