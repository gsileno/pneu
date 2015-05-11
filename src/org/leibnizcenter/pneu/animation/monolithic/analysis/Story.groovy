package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.transform.AutoClone
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

@AutoClone
class Story {

    boolean completed = false

    List<State> steps = []
    // multiple transitions may be fireable in a certain moment
    List<List<Transition>> events = []

    void addStep(State state) {
        if (completed) {
            println("Error. I should not be here")
            return
        }

        if (steps.contains(state)) {
            steps << state
            completed = true
        }
        else
            steps << state
    }

    void removeStep() {

    }

    void addEvent(List<Transition> transitions) {
        if (completed) {
            println("Error. I should not be here")
            return
        } else {
          events << transitions
        }
    }

    String toString() {
        String output = ""
        for (int i = 0; i<steps.size(); i++) {
            output += "("+steps[i].toString()+")"
            if (i == (steps.size() - 1) && completed) output += "*"
            if (events[i]) output += " -- " + events[i].toString() +" -- "
        }
        output
    }
}
