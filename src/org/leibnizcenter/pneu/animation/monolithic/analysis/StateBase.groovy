package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

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
        newState.label = "st" + base.size()

        log.trace("I don't have it ==> I add it: " + newState)

        // if new state, add all enabled transitions
        List<Token> enabledFiringList = []

        // consider all emitters enabled as long as they are not fired
        // emitters are stored in execution.inputs, i.e. in execution.net.inputs
        if (execution.inputs.size() > 0) {
            for (emitter in execution.getEmitterInputs()) {
                if (!execution.firedEmitterList.contains(emitter)) {
                    enabledFiringList << (Transition) emitter
                }
            }
        }

        if (!newState.firedContentStateMap) {
            for (t in execution.transitions - execution.inputs) {
                if (t.isEnabled()) {  // we are in the analysis cycle, no continuous emission
                    enabledFiringList << t
                }
            }
            newState.setEnabledTransitions(enabledFiringList)
        }

        log.trace("enabled transitions: " + enabledFiringList)

        base.add(newState)

        return newState
    }

    String toString() {
        String output = ""
        for (int i = 0; i < base.size(); i++) {
            output += base[i].toString() + ": " + base[i].placesToString() + " / " + base[i].transitionsToString() + " \n"
        }
        output
    }
}