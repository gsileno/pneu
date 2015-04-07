package org.leibnizcenter.pneu.animation.monolithic.analysis

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token

class StateBase {
    List<State> base

    State add(List<Place> places) {

        // for each state in the database
        for (state in base) {
            Boolean differentState = false

            // check if all the places are the same of the input state
            for (p in places) {
                List<Token> marking = s.placeTokensMap[p]
                if (p.marking.size() != marking.size() || p.marking != marking) {
                    differentState = true
                    break
                }
            }

            // if the marking are the same return it
            if (!differentState) return state
        }

        // create a new state with the places and add to the database
        State state = new State(places)
        base.add(state)

        return state
    }
}
