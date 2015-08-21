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

        // log.info("attempt to record a new state: "+places)

        // for each state in the database
        for (state in base) {
            // log.info("check with: "+state)

            Boolean differentState = false

            // check if all the places are the same of the input state
            for (place in places) {
                List<Token> marking = state.placeTokensMap[place.id]

                // log.info("target place: "+place+" / "+"recorded marking: "+marking)

                if (place.marking.size() != marking.size() || place.marking != marking) {
                    differentState = true
                    // log.info("it is different, check another one")
                    break
                }
            }

            // if the marking are the same return it
            if (!differentState) {
                // log.info("it is the same ==> I don't add it")
                return state
            }
        }

        // create a new state with the places and add to the database
        State state = new State(places)
        state.label = "st"+base.size()

        // log.info("I don't have it ==> I add it: "+state)

        // if new state, add all enabled transitions
        List<Transition> enabledTransitions = []
        if (!state.transitionStateMap) {
            for (t in execution.transitions) {
                if (t.isEnabled()) {  // we are in the analysis cycle, no emission
                    enabledTransitions << t
                }
            }
            state.setEnabledTransitions(enabledTransitions)
        }

        base.add(state)

        return state
    }

    String toString() {
        String output = ""
        for (int i = 0; i<base.size(); i++) {
            output += base[i].toString() +": "+base[i].placesToString()+" / "+  base[i].transitionsToString() +" \n"
        }
        output
    }
}
