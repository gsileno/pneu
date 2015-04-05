package org.leibnizcenter.pneu.animation.monolithic

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType

// see execution.groovy for implementation notes and literature references

class BruteForceExecution implements Execution {

    Play play = new Play()

    Integer nTokenEmitted = 0
    Integer nTokenCollected = 0

    Map<Transition, Integer> nFirings = [:]

    List<Transition> transitions
    List<Place> places

    void consumeInputTokens(Transition t) {
        if (t.type == TransitionType.COLLECTOR)
            nTokenCollected++
        t.consumeInputTokens()
        t.flushResetTokens()
    }

    void produceOutputTokens(Transition t) {
        if (t.type == TransitionType.EMITTER)
            nTokenEmitted++
        t.produceOutputTokens()
    }

    void load(Net net) {
        transitions = net.transitionList
        places = net.placeList
    }

    // Brute Force algorithm
    Boolean step() {

        List<Transition> firedTransitions = []

        boolean somethingFired = false

        play.addStep(places)

        // enabling full analysis
        for (t in transitions) {
            // enabled transition analysis
            if (t.isEnabled()) {
                
            }
        }

        // enabling analysis and firing: all transitions are tested
        for (t in transitions) {
            // enabled transition analysis
            if (t.isEnabled()) {
                // transition firing
                consumeInputTokens(t)
                firedTransitions.add(t)
                if (!nFirings[t]) nFirings[t] = 1
                else nFirings[t]++
                somethingFired = true
                break; ////// My modification: only one transition per step!
            }
        }

        if (!somethingFired) return false

        // end of firing: no formation lists are processed

        for (t in firedTransitions) {
            produceOutputTokens(t)
            transitions.remove(t) // in order to implement a kind of FIFO mechanism
            transitions.add(t)
        }

        play.addEvent(firedTransitions)

        // update lists not necessary in BF

        return true
    }




}
