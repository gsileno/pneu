package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent

@Log4j
class Story {

    List<State> steps = []
    // in case multiple transitions may be fired in a certain moment
    List<List<TransitionEvent>> eventsPerStep = []

    void addStep(State state) {
        steps << state
    }

    void addEvents(List<TransitionEvent> transitions) {
        eventsPerStep << transitions
    }

    List<TransitionEvent> getAllTransitionEvents() {
        List<TransitionEvent> allEvents = []
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

        List<List<TransitionEvent>> newEvent = []

        for (eventList in eventsPerStep) {
            newEvent << eventList.collect()
        }

        new Story(
                steps: steps.collect(),
                eventsPerStep: newEvent
        )
    }
}
