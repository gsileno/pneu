package org.leibnizcenter.pneu.animation.monolithic.analysis

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

class State {

    String label

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

    String toString() {
        return label
    }

    String placesToString() {
        String output = "["
        for (elem in placeTokensMap) {
            output += elem.key.id+" ("+elem.value.size()+"), "
        }
        output = output[0..-3]+"]"

        return output
    }

    String transitionsToString() {
        String output = "["
        for (elem in transitionStateMap) {
            output += elem.key.id+" => "
            if (!elem.value) output += "?, "
            else output += "("+elem.value.label+"), "
        }
        output = output[0..-3]+"]"

        return output
    }
}
