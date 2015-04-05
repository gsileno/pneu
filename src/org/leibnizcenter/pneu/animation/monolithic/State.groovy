package org.leibnizcenter.pneu.animation.monolithic

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

class State {
    Map<Place, List<Token>> placeTokensMap = [:]
    Map<Transition, State> transitionStateMap = [:]

    State(List<Place> places) {
        for (p in places) {
            placeTokensMap[p] = p.marking.clone()      // I have to clone the marking, or it will change
        }
    }

    boolean isEqualTo(State otherState) {
        if (placeTokensMap.size() != otherState.placeTokensMap.size())
            return false
        if (transitionStateMap.keySet().size() != otherState.transitionStateMap.keySet().size())
            return false
        for (place in placeTokensMap.keySet()) {
            if (placeTokensMap[place] != otherState.placeTokensMap[place]) //TODO CHECK
                return false
        }
        return true
    }

}
