package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent

@Log4j
class StateBase {
    List<State> base = []

    State save(Execution execution) {

        List<Place> places = execution.places

        // create a new state with the places and add to the database
        State newState = new State(places)
        log.trace("attempt to record a new state: " + places)

        newState.placeList = []
        newState.transitionList = []
        for (place in execution.net.placeList)
            newState.placeList << place.minimalClone()
        for (transition in execution.net.transitionList)
            newState.transitionList << transition.minimalClone()

        // for each state in the database
        for (state in base) {
            log.trace("check with: " + state)
            // if the marking are the same return it
            if (State.compare(newState, state)) {
                log.trace("it is the same ==> I don't add it")
                return state
            }
        }

        // create a new state with the places and add to the database
        newState.id = "st" + base.size()

        log.trace("I don't have it ==> I add it: " + newState)

        // if new state, add all enabled transitions
        List<TransitionEvent> enabledFiringList = []

        // consider all emitters enabled as long as they are not fired
        // emitters are stored in execution.inputs, i.e. in execution.net.inputs
        if (execution.inputs.size() > 0) {
            log.trace("there are emitters in this network")
            for (emitter in execution.getEmitterInputs()) {
                log.trace("considering emitter "+emitter.id)
                if (!execution.firedEmitterEventList.find() { it.transition == emitter }) {
                    log.trace("this emitter has still not fired "+emitter.id)
                    List<TransitionEvent> fireableEvents = emitter.fireableEvents()
                    log.trace("because it is an emitter, I consider only one token "+fireableEvents.first().token)
                    enabledFiringList << new TransitionEvent(transition: emitter, token: fireableEvents.first().token)
                } else {
                    log.trace("this emitter has already fired "+emitter.id)
                }
            }
        }

        log.trace("enabled transition events from emitters: " + enabledFiringList)

        if (!newState.transitionEventStateMap) {
            for (t in execution.transitions - execution.inputs) {
                List<TransitionEvent> enabledFiringListPerTransition = t.fireableEvents()
                if (enabledFiringListPerTransition.size() > 0)
                    enabledFiringList += enabledFiringListPerTransition
            }
            newState.setEnabledFiring(enabledFiringList)
        }

        log.trace("enabled transition events: " + enabledFiringList)

        base.add(newState)

        log.trace("new state "+newState.status())

        return newState
    }

    String toString() {
        String output = ""
        for (int i = 0; i < base.size(); i++) {
            output += base[i].toString() + ": " + base[i].placesToString() + " / " + base[i].transitionEventsToString() + " \n"
        }
        output
    }
}