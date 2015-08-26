package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Transition

@Log4j
class Story {

    List<State> steps = []
    // in case multiple transitions may be fired in a certain moment
    List<List<Transition>> eventsPerStep = []

    void addStep(State state) {
        steps << state
    }

    void addAccesses(List<Transition> transitions) {
        eventsPerStep << transitions
    }

    List<Transition> getAllEvents() {
        List<Transition> allEvents = []
        for (events in eventsPerStep) {
            allEvents += events
        }
        allEvents
    }

    String toString() {
        String output = ""
        for (int i = 0; i < steps.size(); i++) {
            output += "(" + steps[i].toString() + ")"
            if (eventsPerStep[i]) output += " -- " + eventsPerStep[i].toString() + " -- "
        }
        output
    }

    Story minimalClone() {

        List<List<Transition>> newAccesses = []

        for (transitionList in eventsPerStep) {
            newAccesses << transitionList.collect()
        }

        new Story(
                steps: steps.collect(),
                eventsPerStep: newAccesses
        )
    }
}
