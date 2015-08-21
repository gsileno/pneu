package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.builders.PN2log
import org.leibnizcenter.pneu.components.petrinet.Transition

@Log4j
class Analysis {

    StateBase stateBase = new StateBase()
    StoryBase storyBase = new StoryBase()

    Story currentStory
    State currentState
    Execution execution

    // this function save the current place, together with the
    // provenance state and the transitions which allowed its creation
    State saveConsequent(State antecedent = null, List<Transition> firedTransitions = []) {

        State state = stateBase.save(execution)
        log.trace("Recorded state: "+state)

        currentStory.addStep(state)

        if (firedTransitions.size() > 0) {
            if (firedTransitions.size() > 1)
                throw new RuntimeException("ERROR!! I should consider only one transition per time") // TODO: transition with same label
            antecedent.transitionStateMap[firedTransitions[0]] = state
            currentStory.addEvent(firedTransitions)
        }

        return state
    }

    Boolean step() {

        if (!currentStory) currentStory = new Story()
        if (!currentState) currentState = saveConsequent() // for state 0

        log.trace("Current story: "+currentStory)
        log.trace("Current state: "+currentState)

        // in case there the currentState has been already found in the past the story is complete
        // find the first transition which has not been explored from the current state
        Transition nextTransition = currentState.findNextTransition()

        log.trace("Next transition: "+nextTransition)

        // backtrack to uncovered transitions
        // depth-first search
        if (!nextTransition) {

            log.trace("Story completed --> attempt backtrack")

            storyBase.addStory(currentStory)
            Story newStory = currentStory.clone()

            // reverse order of steps (so we reuse already the previous computation)
            for (int i = currentStory.steps.size() - 2; i >= 0; i--) {
                State step = currentStory.steps[i]
                newStory.steps.remove(i+1) // remove last state
                newStory.events.remove(i)  // remove last transition

                log.trace("Adjusting story: "+newStory)

                // find the first transition which has not been explored from the step state
                nextTransition = step.findNextTransition()
                if (nextTransition) {
                    log.trace("========= Reload @ state "+step)

                    // opportunistic reload
                    currentState = step
                    currentStory = newStory
                    execution.loadState(currentState)

                    log.trace("Reloaded story: "+currentStory)
                    break
                }
            }
        }

        if (!nextTransition) {
            log.trace("Exploration finished")
            return false
        }

        execution.fire(nextTransition)
        currentState = saveConsequent(currentState, [nextTransition])

        return true
    }

    void exportToLog(String filename, String path = "out/log/") {
        File folder
        String outputFile

        folder = new File(path)
        if (!folder.exists()) folder.mkdirs()

        outputFile = path + filename + ".analysis.log"

        new File(outputFile).withWriter { out ->
            out.println("Summary: \n" + storyBase.toLog())
            out.println("Stories: \n" + storyBase)
            out.println("States: \n" + stateBase)
        }
    }

}