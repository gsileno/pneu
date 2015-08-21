package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.components.petrinet.Transition

@Log4j
class Analysis {

    StateBase stateBase = new StateBase()
    StorySet storySet = new StorySet()

    Story currentStory
    State currentState
    Execution execution

    // this function save the current place, together with the
    // provenance state and the transitions which allowed its creation
    State saveConsequent(State antecedent = null, List<Transition> firedTransitions = []) {

        State state = stateBase.save(execution)
        // log.info("Recorded state: "+state)

        currentStory.addStep(state)

        if (firedTransitions.size() > 0) {
            if (firedTransitions.size() > 1)
                println("ERROR!! I should consider only one transition per time") // TODO: transition with same label
            antecedent.transitionStateMap[firedTransitions[0]] = state
            currentStory.addEvent(firedTransitions)
        }

        return state
    }

    Boolean step() {

        if (!currentStory) currentStory = new Story()
        if (!currentState) currentState = saveConsequent() // for state 0

        // log.info("Current story: "+currentStory)
        // log.info("Current state: "+currentState)

        // in case there the currentState has been already found in the past the story is complete
        // find the first transition which has not been explored from the current state
        Transition nextTransition = currentState.findNextTransition()

        // log.info("Next transition: "+nextTransition)

        // backtrack to uncovered transitions
        // depth-first search
        if (!nextTransition) {

            // log.info("Story completed --> attempt backtrack")

            storySet.addStory(currentStory)
            Story newStory = currentStory.clone()

            // reverse order of steps (so we reuse already the previous computation)
            for (int i = currentStory.steps.size() - 2; i >= 0; i--) {
                State step = currentStory.steps[i]
                newStory.steps.remove(i+1) // remove last state
                newStory.events.remove(i)  // remove last transition

                // log.info("Adjusting story: "+newStory)

                // find the first transition which has not been explored from the step state
                nextTransition = step.findNextTransition()
                if (nextTransition) {
                    // log.info("========= Reload @ state "+step)

                    // opportunistic reload
                    currentState = step
                    currentStory = newStory
                    execution.loadState(currentState)

                    // log.info("Reloaded story: "+currentStory)
                    break
                }
            }
        }

        if (!nextTransition) {
            log.info("Exploration finished")
            return false
        }

        execution.fire(nextTransition)
        currentState = saveConsequent(currentState, [nextTransition])

        return true
    }

}