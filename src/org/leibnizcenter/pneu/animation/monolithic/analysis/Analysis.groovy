package org.leibnizcenter.pneu.animation.monolithic.analysis

import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.components.petrinet.Transition

class Analysis {

    StateBase stateBase = new StateBase()
    StorySet storySet = new StorySet()

    Story currentStory
    State currentState
    Execution execution

    Map<Transition, Integer> nFirings = [:]

    State saveSnapshot(State source = null, List<Transition> firedTransitions = []) {
        List<Transition> enabledTransitions = []
        State state = stateBase.add(execution.places)

        // if new state, add all enabled transitions
        if (!state.transitionStateMap) {
            for (t in execution.transitions) {
                if (t.isEnabled()) {
                    enabledTransitions << t
                }
            }
            state.setEnabledTransitions(enabledTransitions)
        }

        if (firedTransitions.size() > 0) {
            if (firedTransitions.size() > 1)
                println("ERROR!! I should consider only one transition per time") // TODO: transition with same label
            source.transitionStateMap[firedTransitions[0]] = state
        }

        currentStory.addStep(state)

        return state
    }

    Boolean step() {
        if (!currentStory) currentStory = new Story()
        if (!currentState) currentState = saveSnapshot() // for state 0

        Transition nextTransition

        // in case there the currentState has been already found in the past the story is complete
        if (!currentStory.completed) {
            // find the first transition which has not been explored from the current state
            nextTransition = currentState.transitionStateMap.find() { (!it.value) }
            // if there is no available transition at this state, the story is completed
            if (!nextTransition) currentStory.completed = true
        }

        // backtrack to uncovered transitions
        // depth-first search
        if (currentStory.completed) {
            storySet.addStory(currentStory)
            Story newStory = currentStory.clone()

            // reverse order of steps (so we reuse already the previous computation)
            for (int i = currentStory.steps.size() - 2; i >= 0; i--) {
                State step = currentStory.steps[i]
                newStory.steps.remove(i+1)
                nextTransition = step.transitionStateMap.find() { !it.value }
                if (nextTransition) {
                    currentState = step
                    break
                }
            }
            currentStory = newStory
        }

        if (!nextTransition) return false

        List<Transition> firedTransitions = execution.fire(nextTransition)

        for (t in firedTransitions) {
            if (!nFirings[t]) nFirings[t] = 1
            else nFirings[t]++
        }

        currentStory.addEvent(firedTransitions)

        currentState = saveSnapshot(currentState, firedTransitions)

        return true
    }

}