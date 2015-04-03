package org.leibnizcenter.pneu.animation.monolithic
import org.leibnizcenter.pneu.components.petrinet.Transition

// see execution.groovy for implementation notes and literature references

class EnabledTransitionExecution extends BruteForceExecution {
    // Enabled transitions list:
    // list of transitions with input places all marked
    List<Transition> enabledTransitions = []

    void produceOutputTokens(Transition t, List<Transition> newEnabledTransitions) {
        for (elem in t.outputs) {
            elem.place.marking +=  elem.weight
            for (descendent in elem.place.outputs) {
                if (descendent.isEnabled()) {
                    newEnabledTransitions << descendent
                }
            }
        }
    }

    // Enabled Transitions (ET) algorithm
    Boolean step() {
        List<Transition> firedTransitions = []

        boolean somethingFired = false

        // enabling analysis and firing: only enabled transitions are tested
        for (t in enabledTransitions) {
            // enabled transition analysis
            if (t.isEnabled()) { // & t.predicate()) { TODO to be added later for labeling
                // transition firing update
                t.consumeInputTokens()
                enabledTransitions.remove(t)
                firedTransitions.add(t)
                somethingFired = true
            }
        }
        if (!somethingFired) return false

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
        enabledTransitions << newEnabledTransitions

        return true
    }

}
