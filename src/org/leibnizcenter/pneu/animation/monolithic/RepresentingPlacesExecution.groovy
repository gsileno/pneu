package org.leibnizcenter.pneu.animation.monolithic

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

// see execution.groovy for implementation notes and literature references

class RepresentingPlacesExecution extends BruteForceExecution {

    List<Place> markedRepresentingPlaces = []
    List<Place> markedSynchronizationPlaces = []

    // map each transition to a representing places amongst each inputs
    Map<Transition, Place> transitions2RepresentingPlaces = [:]

    // from the representing place p, transition t is fired
    void consumeInputTokens(Transition t,
                            List<Place> markedRepresentingPlaces,
                            List<Place> markedSynchronizationPlaces) {

        for (elem in t.inputs) {
            elem.place.marking -= elem.weight
            if (markedRepresentingPlaces.contains(elem.place))
                markedRepresentingPlaces.remove(elem.place)
            if (markedSynchronizationPlaces.contains(elem.place))
                markedSynchronizationPlaces.remove(elem.place)
        }
    }

    void produceOutputTokens(Transition t,
                             List<Place> newMarkedRepresentingPlaces,
                             List<Place> newMarkedSynchronizationPlaces) {

        for (elem in t.outputs) {
            elem.place.marking += elem.weight
            if (transitions2RepresentingPlaces.values().contains(elem.place)) {
                // TODO
            }
        }
    }

    void changeRepresentingPlace(Transition t) {

        Place current = transitions2RepresentingPlaces[t]
        Place newRepresentingPlace
        Integer min

        for (elem in t.inputs) {
            if (elem.place != current) { // different from the previous one
                if (elem.place.marking == elem.weight - 1) { // only one token remaining
                    newRepresentingPlace = elem.place
                    break
                } else { // find the nearest to trigger
                    if (min == null) {
                        min = elem.weight - elem.place.marking
                        newRepresentingPlace = elem.place
                    }
                    if (elem.weight - elem.place.marking < min) {
                        min = elem.weight - elem.place.marking
                        newRepresentingPlace = elem.place
                    }
                }
            }
        }
        transitions2RepresentingPlaces[t] = newRepresentingPlace
    }

    // Representing Places approaches, static (SRP) / dynamic (DRP)
    Boolean step(boolean dynamic = false) {

        List<Transition> firedTransitions = []

        boolean somethingFired = false

        // enabling analysis and firing:
        // only output transitions of marked representing places are tested
        // if a transition is fired, the rest of represented transitions
        // are not tested (binary tests)
        for (p in markedRepresentingPlaces) {
            for (t in p.outputs) {
                // enabled transition analysis
                if (t.enabled()) { // & t.predicate()) TODO to be added later for labeling
                    // transition firing update
                    // consumeInputTokens(t, markedRepresentingPlaces, markedSynchronizationPlaces) // TOCHECK
                    consumeInputTokens(t, markedRepresentingPlaces, markedSynchronizationPlaces)
                    firedTransitions.add(t)
                    somethingFired = true
                    break; // TODO only for binary !!!
                } else if (dynamic) {
                    // p is marked already, so let us choose
                    // another representing place amongst the empty ones
                    changeRepresentingPlace(t)
                }
            }
        }
        if (!somethingFired) return false

        // end of firing: the formation lists are built with the
        // descending representing places of fired transitions
        List<Place> newMarkedRepresentingPlaces = []
        List<Place> newMarkedSynchronizationPlaces = []

        for (t in firedTransitions) {
            t.produceOutputTokens()
            produceOutputTokens(t, newMarkedSynchronizationPlaces)
        }

        // update lists: the treatment list is composed by the new marked
        // representing places and the representing places not unmarked in this cycle
        markedRepresentingPlaces << newMarkedRepresentingPlaces
        markedSynchronizationPlaces << newMarkedSynchronizationPlaces

        return true
    }

}
