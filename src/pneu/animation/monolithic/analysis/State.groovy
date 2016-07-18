package pneu.animation.monolithic.analysis

import pneu.components.petrinet.TransitionEvent
import pneu.components.petrinet.Place
import pneu.components.petrinet.Token
import pneu.components.petrinet.Transition

class State {

    String id // set by StateBase

    // associate to each place id a certain marking
    Map<String, List<Token>> placeIdTokensMap
    // associate to each fired content of transition, the state in which it brings
    Map<TransitionEvent, State> transitionEventStateMap

    List<Place> placeList
    List<Transition> transitionList

    State(List<Place> places, List<Transition> transitions = []) {

        placeList = []
        transitionList = []
        for (place in places)
            placeList << place.minimalClone()
        for (transition in transitions)
            transitionList << transition.minimalClone()

        placeIdTokensMap = [:]

        // clone the marking, maintaining the reference to the place
        for (p in places) {
            if (p.id == null)
                throw new RuntimeException("Ids of places should have been initialized")
            placeIdTokensMap[p.id] = p.marking.collect()
        }
    }

    void setEnabledFiring(List<TransitionEvent> enabledFiringList) {
        transitionEventStateMap = [:]

        // open the space for potential next steps
        for (t in enabledFiringList) {
            transitionEventStateMap[t] = null
        }
    }

    String toString() {
        if (id) id
        else placesToString()
    }

    String status() {
        String output = ""
        if (id) output += id +": "
        output += placeIdTokensMap.size() + ", " + transitionEventStateMap.size()
        output
    }

    TransitionEvent findNextEvent() {
        TransitionEvent nextEvent = null
        for (elem in transitionEventStateMap) {
            if (elem.value == null) {
                nextEvent = elem.key
                break
            }
        }
        nextEvent
    }

    String placesToString() {
        String output = "["
        for (elem in placeIdTokensMap) {
            // output += elem.key + " (" + elem.value /*.size() */ +")" + ", "
            output += elem.key + " (" + elem.value.size()+")" + ", "
        }
        if (placeIdTokensMap.size() > 0) output = output[0..-3]+"]"
        else output += "\n]"

        return output
    }

    String transitionEventsToString() {
        String output = "["
        for (elem in transitionEventStateMap) {
            output += elem.key.toString() + " => "
            if (!elem.value) output += "?, "
            else output += "("+elem.value.id+"), "
        }
        if (transitionEventStateMap.size() > 0) output = output[0..-3]+"]"
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
                transitionEventStateMap: transitionEventStateMap
        )
    }

    private State() {}

}
