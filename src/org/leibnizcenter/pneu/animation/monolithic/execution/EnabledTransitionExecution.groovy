package org.leibnizcenter.pneu.animation.monolithic.execution

import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType

// see execution.groovy for implementation notes and literature references

class EnabledTransitionExecution extends BruteForceExecution {

    // Enabled transitions list:
    // list of transitions with input places all marked
    List<Transition> enabledTransitions

    Map<Transition, Integer> nFirings = [:]

    void consumeInputTokens(Transition t) {
        if (t.type == TransitionType.COLLECTOR)
            nTokenCollected++
        t.consumeInputTokens()
        t.flushResetTokens()
    }

    void produceOutputTokens(Transition t, List<Transition> newEnabledTransitions) {
        if (t.type == TransitionType.EMITTER) {
            nTokenEmitted++
            newEnabledTransitions << t
        }

        for (elem in t.outputs) {
            // put empty tokens
            for (int i=0; i<elem.weight; i++) {
                elem.place.marking.push(new Token())
            }
            for (descendant in elem.place.outputs) {
                if (descendant.isEnabled()) {
                    newEnabledTransitions << descendant
                }
            }
        }
    }

    // Enabled Transitions (ET) algorithm
    Boolean step() {
        List<Transition> firedTransitions = []

        boolean somethingFired = false

        // setup of enabled transitions at first execution
        if (!enabledTransitions) {
            enabledTransitions = new ArrayList<Transition>()
            for (t in transitions) {
                if (t.isEnabled()) {
                    enabledTransitions << t
                }
            }
        }

        // enabling analysis and firing: only enabled transitions are tested

        for (t in enabledTransitions) {
            // enabled transition analysis
            if (t.isEnabled()) {
                // transition firing update
                consumeInputTokens(t)
                firedTransitions.add(t)
                if (!nFirings[t]) nFirings[t] = 1
                else nFirings[t]++
                somethingFired = true
                break; ////// My modification: only one transition per step!
            }
        }

        if (!somethingFired) return false
        else {
            enabledTransitions -= firedTransitions
        }

        // New enabled transition or formation list:
        // transitions which are enabled by the marking in the firing of the transitions
        List<Transition> newEnabledTransitions = []

        // end of firing: the formation lists are built with the
        // descending transitions of fired transitions
        for (t in firedTransitions) {
            produceOutputTokens(t, newEnabledTransitions)
        }

        // update lists: the treatment list is composed by the new enabled
        // transitions (all transitions in the formation list are tested for enabling)
        // and the transitions enabled but not fired in this cycle
        enabledTransitions += newEnabledTransitions

        return true
    }

}
