package org.leibnizcenter.pneu.animation.monolithic.execution

import org.leibnizcenter.pneu.components.basicpetrinet.BasicToken
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType

// see execution.groovy for implementation notes and literature references

////// TODO: it does not work with not binary nets

class RepresentingPlacesExecution extends BruteForceExecution {

    boolean dynamic = false

    List<Place> markedRepresentingPlaces
    List<Place> markedSynchronizationPlaces

    // map each transition to a representing places amongst each inputs
    Map<Transition, Place> transitions2RepresentingPlaces = [:]

    // from the representing place p, transition t is fired
    void consumeInputTokens(Transition t,
                            List<Place> markedRepresentingPlaces,
                            List<Place> markedSynchronizationPlaces) {

        if (t.type == TransitionType.COLLECTOR)
            nTokenCollected++

        for (elem in t.inputs) {
            // remove tokens
            for (int i=0; i<elem.weight; i++) {
                elem.place.marking.pop()
            }

            if (elem.place.marking.size() < elem.weight) {
               if (markedSynchronizationPlaces.contains(elem.place))
                  markedSynchronizationPlaces.remove(elem.place)
            }
        }
    }

    void produceOutputTokens(Transition t,
                             List<Place> newMarkedRepresentingPlaces,
                             List<Place> newMarkedSynchronizationPlaces) {

        if (t.type == TransitionType.EMITTER)
            nTokenEmitted++

        // println("transition "+t.id +" producing")

        for (elem in t.outputs) {

            // put empty tokens
            for (int i=0; i<elem.weight; i++) {
                elem.place.marking.push(new BasicToken())
            }

            if (transitions2RepresentingPlaces.values().contains(elem.place)) {
                newMarkedRepresentingPlaces << elem.place
            } else {
                newMarkedSynchronizationPlaces << elem.place
            }
        }
    }

    void changeRepresentingPlace(Transition t) {

        Place current
        if (transitions2RepresentingPlaces.containsKey(t)) // case in which it has not set up
            current = transitions2RepresentingPlaces[t]
        Place newRepresentingPlace
        Integer min

        if (t.inputs.size() > 0) {
            for (elem in t.inputs) {
                if (elem.place != current) { // different from the previous one
                    if (elem.place.marking == elem.weight - 1) { // only one token remaining
                        newRepresentingPlace = elem.place
                        break
                    } else { // find the nearest to trigger
                        Integer diff = elem.weight - elem.place.marking.size()
                        if (!min || diff < min) {
                            min = diff
                            newRepresentingPlace = elem.place
                        }
                    }
                }
            }
            transitions2RepresentingPlaces[t] = newRepresentingPlace
        }
    }

    void resetMarkedRepresentingPlaces() {
        markedSynchronizationPlaces = []
        markedRepresentingPlaces = []
        for (elem in transitions2RepresentingPlaces) {
            if (elem.value.marking.size() > 0)  // TODO, it should check the real weight
                markedRepresentingPlaces << elem.value
        }
    }

    // Representing Places approaches, static (SRP) / dynamic (DRP)
    Boolean step() {

        List<Transition> firedTransitions = []

        // setup of representing places at first execution
        if (transitions2RepresentingPlaces.size() == 0) {
            for (t in transitions) {
                changeRepresentingPlace(t)
            }

            resetMarkedRepresentingPlaces()
        }

        println "MRP pre: "+markedRepresentingPlaces
        println "MSP pre: "+markedSynchronizationPlaces

        boolean somethingFired = false

        List<Place> newMarkedRepresentingPlaces = []
        List<Place> newMarkedSynchronizationPlaces = []

        // enabling analysis and firing:
        // only output transitions of marked representing places are tested
        // if a transition is fired, the rest of represented transitions
        // are not tested (binary tests)

        while (markedRepresentingPlaces.size() > 0) {
            // take the place to be checked
            Place p = markedRepresentingPlaces.pop()
            for (t in p.outputs) {
                // enabled transition analysis
                if (t.isEnabled()) {
                    // transition firing update
                    // consumeInputTokens(t, markedRepresentingPlaces, markedSynchronizationPlaces) // TOCHECK
                    consumeInputTokens(t, markedRepresentingPlaces, markedSynchronizationPlaces)
                    firedTransitions.add(t)
                    somethingFired = true
                    // break; // TODO only for binary !!!
                } else if (dynamic) {
                    // p is marked already, so let us choose
                    // another representing place amongst the unmarked synchronization ones
                    changeRepresentingPlace(t)
                    // p becomes a marked synchronization places
                    markedSynchronizationPlaces << p
                }
            }
            if (p.marking.size())
                markedRepresentingPlaces.remove(elem.place)
            // we haven't used it in a static setting, it returns to the list
            if (!somethingFired && !dynamic)
                newMarkedRepresentingPlaces << p
        }
        if (!somethingFired) return false

        // end of firing: the formation lists are built with the
        // descending representing places of fired transitions

        for (t in firedTransitions) {
            produceOutputTokens(t, newMarkedRepresentingPlaces, newMarkedSynchronizationPlaces)
            // update lists: the treatment list is composed by the new marked
            // representing places and the representing places not unmarked in this cycle
            markedRepresentingPlaces += newMarkedRepresentingPlaces
            markedSynchronizationPlaces += newMarkedSynchronizationPlaces
        }

        println "MRP post: "+markedRepresentingPlaces
        println "MSP post: "+markedSynchronizationPlaces
        return true

    }
}