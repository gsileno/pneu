package org.leibnizcenter.pneu.animation.monolithic

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class Play {
    List<State> steps = []
    List<List<Transition>> events = []

    void addStep(List<Place> places) {
        steps << new State(places)
    }

    void addEvent(List<Transition> transitions) {
        events << transitions
    }
}
