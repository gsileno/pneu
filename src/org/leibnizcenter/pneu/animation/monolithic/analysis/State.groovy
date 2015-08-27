package org.leibnizcenter.pneu.animation.monolithic.analysis

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

class State {

    String label

    // associate to each place id a certain marking
    Map<String, List<Token>> placeIdTokensMap
    // associate to each transition, the state in which it brings
    Map<Transition, State> transitionStateMap

    List<Place> placeList
    List<Transition> transitionList

    State() {}

    State(List<Place> places) {
        placeIdTokensMap = [:]

        // clone the marking, maintaining the reference to the place
        for (p in places) {
            if (p.id == null)
                throw new RuntimeException("Ids of places should have been initialized")
            placeIdTokensMap[p.id] = p.marking.collect()
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
        if (label) label
        else placesToString()
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
        for (elem in placeIdTokensMap) {
            output += elem.key + " (" + elem.value.size()+")" + ", "
        }
        if (placeIdTokensMap.size() > 0) output = output[0..-3]+"]"
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

    Boolean compare(State s) {
        // check the number of places
        if (placeIdTokensMap.size() != s.placeIdTokensMap.size())
            return false

        // for each place
        for (pId in placeIdTokensMap.keySet()) {
            // check if it exists in the other state
            if (!s.placeIdTokensMap.keySet().contains(pId)) {
                return false
            }

            // check if the number of tokens is equal
            if (placeIdTokensMap[pId].size() != s.placeIdTokensMap[pId].size()) {
                return false
            }

            // check if the markings are the same
            for (token in placeIdTokensMap[pId]) {
                Boolean found = false
                for (otherToken in s.placeIdTokensMap[pId]) {
                    if (token.compare(otherToken)) {
                        found = true
                        break
                    }
                }
                // if a token has not been found, the state are not the same
                if (!found) return false
            }
        }

        return true
    }

    static Boolean compare(State s1, State s2) {
        s1.compare(s2)
    }

    State minimalClone() {

        Map<String, List<Token>> clonedPlaceIdTokensMap = [:]

        for (pId in placeIdTokensMap.keySet()) {
            clonedPlaceIdTokensMap[pId] = placeIdTokensMap[pId].collect()
        }

        new State(
                placeIdTokensMap: clonedPlaceIdTokensMap,
                transitionStateMap: transitionStateMap
        )
    }
}
