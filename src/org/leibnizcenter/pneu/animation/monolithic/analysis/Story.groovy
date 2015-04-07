package org.leibnizcenter.pneu.animation.monolithic.analysis

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class Story {
    List<State> steps = []
    // multiple transitions may be fireable in a certain moment
    List<List<Transition>> events = []

    void addStep(State state) {
        steps << state
    }

    void addEvent(List<Transition> transitions) {
        events << transitions
    }

    String toString() {
        String output = ""
        for (int i = 0; i<steps.size(); i++) {
            output += "("+steps[i].toString()+")" + " -- " + events[i].toString() +" -- "
        }

        output
    }
}
