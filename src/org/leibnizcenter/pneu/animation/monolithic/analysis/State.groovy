package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.transform.AutoClone
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

@AutoClone
class State {

    String label

    Map<String, List<Token>> placeTokensMap
    Map<Transition, State> transitionStateMap

    State(List<Place> places) {
        placeTokensMap = [:]

        // clone the marking, maintaining the reference to the place
        for (p in places) {
            placeTokensMap[p.id] = p.marking.clone()
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

    Transition findNextTransition() {
        Transition nextTransition = null
        for (elem in transitionStateMap) {
            if (elem.value == null) {
                nextTransition = elem.key
                break
            }
        }
        nextTransition
    }

    String placesToString() {
        String output = "["
        for (elem in placeTokensMap) {
            output += elem.key + " (" + elem.value.size()+")" + ", "
        }
        if (placeTokensMap.size() > 0) output = output[0..-3]+"]"
        else output += "]"

        return output
    }

    String transitionsToString() {
        String output = "["
        for (elem in transitionStateMap) {
            output += elem.key.toString() + " => "
            if (!elem.value) output += "?, "
            else output += "("+elem.value.label+"), "
        }
        if (transitionStateMap.size() > 0) output = output[0..-3]+"]"
        else output += "]"

        return output
    }
}
