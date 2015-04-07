package org.leibnizcenter.pneu.animation.monolithic.execution

import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.animation.monolithic.analysis.StateBase
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType

// see execution.groovy for implementation notes and literature references

class BruteForceExecution implements Execution {

    Story play = new Story()
    StateBase stateBase = new StateBase()
    State current

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

    State saveSnapshot(State source = null, List<Transition> firedTransitions = []) {
        List<Transition> enabledTransitions = []
        State state = stateBase.add(places)

        // if new state, add all enabled transitions
        if (!state.transitionStateMap) {
            for (t in transitions) {
                if (t.isEnabled()) {
                    enabledTransitions << t
                }
            }
            state.setEnabledTransitions(enabledTransitions)
        }

        if (firedTransitions.size() > 0) {
            if (firedTransitions.size() > 1)
                println("ERROR!! I consider only one transition per time")

            source.transitionStateMap[firedTransitions[0]] = state
        }
        play.addStep(state)

        return state
    }

    void load(Net net) {
        transitions = net.transitionList
        places = net.placeList
    }

    // Brute Force algorithm
    Boolean step() {

        List<Transition> firedTransitions = []

        if (!current) current = saveSnapshot() // for state 0

        boolean somethingFired = false

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
                break; ////// my modification: only one transition per step!
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

        current = saveSnapshot(current, firedTransitions)

        // update lists not necessary in BF

        return true
    }




}
