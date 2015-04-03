package org.leibnizcenter.pneu.animation.monolithic

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

// see execution.groovy for implementation notes and literature references

class BruteForceExecution implements Execution {

    List<Transition> transitions
    List<Place> places

    void embody(Net net) {
        transitions = net.transitionList
        places = net.placeList
    }

    // Brute Force algorithm
    Boolean step() {

        List<Transition> firedTransitions = []

        boolean somethingFired = false

        // enabling analysis and firing: all transitions are tested
        for (t in transitions) {
            // enabled transition analysis
            if (t.enabled()) { // & t.predicate()) { TODO to be added later for labeling
                // transition firing
                t.consumeInputTokens()
                firedTransitions.add(t)
                somethingFired = true
            }
        }

        if (!somethingFired) return false

        // end of firing: no formation lists are processed

        for (t in firedTransitions) {
            t.produceOutputTokens()
        }
        // update lists not necessary in BF

        return true
    }



}
