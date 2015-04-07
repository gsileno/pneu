package org.leibnizcenter.pneu.animation.monolithic.analysis

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token

class StateBase {
    List<State> base = []

    State add(List<Place> places) {

        // for each state in the database
        for (state in base) {
            Boolean differentState = false

            // check if all the places are the same of the input state
            for (place in places) {
                List<Token> marking = state.placeTokensMap[place]
                if (place.marking.size() != marking.size() || place.marking != marking) {
                    differentState = true
                    break
                }
            }

            // if the marking are the same return it
            if (!differentState) return state
        }

        // create a new state with the places and add to the database
        State state = new State(places)
        state.label = "st"+base.size()
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
