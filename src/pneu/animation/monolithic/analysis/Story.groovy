package pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import pneu.components.petrinet.TransitionEvent

@Log4j
class Story {

    String id // set by StoryBase

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
            if (i < steps.size() - 1) output += " -- " + eventsPerStep[i][0].label() + " -- " // TODO: only one event per Step
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

    static Story append(Story left, Story right) {

        log.trace("append $left with $right")

        Story appended
        if (left != null)
            appended = left.minimalClone()
        else
            appended = new Story()

        for (int i = 0; i < right.steps.size(); i++) {
            if (appended.steps.size() == 0 || i > 0) {
                appended.addStep(right.steps[i])
            } else {
                if (appended.steps.last() != right.steps.first()) {
                    throw new RuntimeException("You cannot append two stories which do no share the last state (${appended.steps.last()} vs ${right.steps.first()}.")
                }
            }

            if (i < right.steps.size() - 1) {
                appended.addEvents(right.eventsPerStep[i])
            }
        }

        log.trace("result: $appended")

        appended
    }

    static Boolean compare(Story source, Story target) {

        for (sourceState in source.steps) {
            Boolean found = false
            for (targetState in target.steps) {
                log.trace("comparing ${sourceState} with ${targetState}")
                if (sourceState.compare(targetState)) {
                    found = true
                    break
                }
            }
            if (!found) return false
        }

        for (sourceTransitionEvent in source.eventsPerStep) {
            Boolean found = false
            for (targetTransitionEvent in target.eventsPerStep) {
                // TODO: only one event considered
                log.trace("comparing ${sourceTransitionEvent[0].token} with ${targetTransitionEvent[0].token}")
                if (sourceTransitionEvent[0].token.compare(targetTransitionEvent[0].token)) {
                    found = true
                    break
                }
            }
            if (!found) return false
        }

        return true
    }

}
