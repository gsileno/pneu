package org.leibnizcenter.pneu.animation.monolithic.execution

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent
import org.leibnizcenter.pneu.components.petrinet.TransitionType

// see execution.groovy for implementation notes and literature references

@Log4j
class BruteForceExecution extends Execution {

    ////////////// without event selection: for execution

    // consume and record the collection if needed
    void consumeInputTokens(Transition t) {
        if (t.type == TransitionType.COLLECTOR)
            nTokenCollected++
        t.consumeInputTokens()
    }

    // consume and record the emission if needed
    TransitionEvent produceOutputTokens(Transition t) {
        if (t.type == TransitionType.EMITTER)
            nTokenEmitted++
        t.produceOutputTokens()
    }

    // when you already know which transition to fire (for analysis)
    TransitionEvent fire(Transition t) {
        consumeInputTokens(t)
        TransitionEvent event = produceOutputTokens(t)

        if (inputs.contains(t)) {
            firedEmitterEventList << event
        }
        event
    }

    ////////////// with event selection: for analysis (backtracking on unexplored firing)

    // consume and record the collection if needed
    void consumeInputTokens(TransitionEvent t) {
        if (t.transition.type == TransitionType.COLLECTOR)
            nTokenCollected++
        t.transition.consumeInputTokens(t)
    }

    // consume and record the emission if needed
    TransitionEvent produceOutputTokens(TransitionEvent t) {
        if (t.transition.type == TransitionType.EMITTER)
            nTokenEmitted++
        t.transition.produceOutputTokens(t)
    }

    // when you already know which transition to fire (for analysis)
    TransitionEvent fire(TransitionEvent t) {
        if (inputs.contains(t.transition)) {
            firedEmitterEventList << t
        }

        consumeInputTokens(t)
        TransitionEvent event = produceOutputTokens(t)
        event
    }

    // ---------------------------------------------------
    // --------- Brute Force algorithm
    // ---------------------------------------------------
    List<TransitionEvent> cycle(Boolean includeEmission = true) {

        List<TransitionEvent> firedEvents = []

        // enabling analysis and firing: all transitions are tested
        for (t in transitions) {
            // enabled transition analysis
            if ((!includeEmission && t.isEnabled()) || t.isEnabledIncludingEmission()) {
                // transition firing
                consumeInputTokens(t)
                firedEvents.add(t)
                break; ////// my modification: only one transition per step!
            }
        }

        // end of firing: no formation lists are processed
        for (t in firedEvents) {
            produceOutputTokens(t)
            transitions.remove(t) // in order to implement a kind of FIFO mechanism
            transitions.add(t)
        }

        // update lists not necessary in BF
        return firedEvents
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
