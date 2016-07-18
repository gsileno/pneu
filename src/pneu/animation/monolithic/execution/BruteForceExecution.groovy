package pneu.animation.monolithic.execution

import groovy.util.logging.Log4j
import pneu.components.petrinet.Transition
import pneu.components.petrinet.TransitionEvent
import pneu.components.petrinet.TransitionType

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

    // when you already know which transition to fire (for execution)
    TransitionEvent fire(Transition t) {
        log.trace("firing transition "+t)
        consumeInputTokens(t)
        TransitionEvent event = produceOutputTokens(t)

        log.trace("fired event "+event)
        if (inputs.contains(t)) {
            firedEmitterEventList << event
        }
        event

    }

    ////////////// with event selection: for analysis (backtracking on unexplored firing)

    // consume and record the collection if needed
    void consumeInputTokens(TransitionEvent event) {
        if (event.transition.type == TransitionType.COLLECTOR)
            nTokenCollected++
        event.transition.consumeInputTokens(event)
    }

    // consume and record the emission if needed
    TransitionEvent produceOutputTokens(TransitionEvent event) {
        if (event.transition.type == TransitionType.EMITTER)
            nTokenEmitted++
        event.transition.produceOutputTokens(event)
    }

    // when you already know which transition to fire (for analysis)
    TransitionEvent fire(TransitionEvent event) {
        log.trace("firing event "+event)

        if (inputs.contains(event.transition)) {
            firedEmitterEventList << event
        }

        consumeInputTokens(event)
        event = produceOutputTokens(event)
        log.trace("fired event "+event)

        event
    }

    // -------------------------------------------------------------
    // --------- Brute Force algorithm ------------ for execution --
    // -------------------------------------------------------------
    List<TransitionEvent> cycle() {

        Transition firedTransition

        // enabling analysis and firing: all transitions are tested
        for (t in transitions) {
            // enabled transition analysis
            if (t.isEnabledIncludingEmission()) {
                // transition firing
                consumeInputTokens(t)
                firedTransition = t
                break; ////// my modification: only one transition per step!
            }
        }

        if (firedTransition != null) {
            // end of firing: no formation lists are processed
            TransitionEvent event = produceOutputTokens(firedTransition)
            transitions.remove(event.transition) // in order to implement a kind of FIFO mechanism
            transitions.add(event.transition)

            // update lists not necessary in BF
            [event]
        } else {
            []
        }

    }

    // if a transition has fired return true
    Boolean step() {
        List<TransitionEvent> firedTransitionEvents = cycle()
        return (firedTransitionEvents.size() > 0)
    }

}
