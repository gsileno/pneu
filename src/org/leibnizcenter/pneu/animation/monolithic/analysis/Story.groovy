package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.transform.AutoClone
import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

@AutoClone@Log4j
class Story {

    List<State> steps = []
    // multiple transitions may be fireable in a certain moment
    List<List<Transition>> events = []

    void addStep(State state) {
        if (steps.contains(state)) {
            steps << state
        }
        else
            steps << state
    }


    void addEvent(List<Transition> transitions) {
        events << transitions
    }

    String toString() {
        String output = ""
        for (int i = 0; i<steps.size(); i++) {
            output += "("+steps[i].toString()+")"
            // if (i == (steps.size() - 1) && completed) output += "*"
            if (events[i]) output += " -- " + events[i].toString() +" -- "
        }
        output
    }
}
