package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Transition

@Log4j
class Story {

    List<State> steps = []
    // multiple transitions may be enabled in a certain moment
    List<List<Transition>> accesses = []

    void addStep(State state) {
        steps << state
    }

    void addAccesses(List<Transition> transitions) {
        accesses << transitions
    }

    String toString() {
        String output = ""
        for (int i = 0; i < steps.size(); i++) {
            output += "(" + steps[i].toString() + ")"
            if (accesses[i]) output += " -- " + accesses[i].toString() + " -- "
        }
        output
    }

    Story minimalClone() {

        List<List<Transition>> newAccesses = []

        for (transitionList in accesses) {
            newAccesses << transitionList.collect()
        }

        new Story(
                steps: steps.collect(),
                accesses: newAccesses
        )
    }
}
