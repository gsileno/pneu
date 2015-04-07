package org.leibnizcenter.pneu.animation.monolithic.analysis

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

class State {
    Map<Place, List<Token>> placeTokensMap
    Map<Transition, State> transitionStateMap

    State(List<Place> places) {
        placeTokensMap = [:]

        // clone the marking, maintaining the reference to the place
        for (p in places) {
            placeTokensMap[p] = p.marking.clone()
        }
    }

    void setEnabledTransitions(List<Transition> transitions) {
        transitionStateMap = [:]

        // open the space for potential next steps
        for (t in transitions) {
            transitionStateMap[t] = null
        }
    }

}
