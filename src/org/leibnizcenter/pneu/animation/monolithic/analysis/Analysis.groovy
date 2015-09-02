package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent

@Log4j
class Analysis {

    StateBase stateBase = new StateBase()
    StoryBase storyBase = new StoryBase()

    Story currentStory
    State currentState
    Execution execution

    // this function save the current place, together with the
    // provenance state and the transitions which allowed its creation
    State saveConsequent(State antecedent = null, List<TransitionEvent> firedEvents = []) {

        log.trace("attempt to record current state, after firing: "+firedEvents)
        State state = stateBase.save(execution)
        log.trace("Recorded state: " + state)

        currentStory.addStep(state)

        if (firedEvents.size() > 0) {
            if (firedEvents.size() > 1)
                throw new RuntimeException("ERROR!! I should consider only one transition per time")

            // there is a problem with the hashcode

            TransitionEvent firedEvent = firedEvents[0]

            TransitionEvent key
            for (event in antecedent.transitionEventStateMap.keySet()) {
                if (event.transition.compare(firedEvent.transition) &&
                        event.token.compare(firedEvent.token)) {
                    key = event
                    break;
                }
            }

            antecedent.transitionEventStateMap[key] = state
            currentStory.addEvents(firedEvents)
        }

        return state
    }

    Boolean step() {

        if (currentStory == null) currentStory = new Story()
        if (currentState == null) currentState = saveConsequent() // for state 0

        log.trace("Current story: " + currentStory)
        log.trace("Current state: " + currentState)

        // in case there the currentState has been already found in the past the story is complete
        // find the first transition which has not been explored from the current state
        TransitionEvent nextEvent = currentState.findNextEvent()

        log.trace("Next event: " + nextEvent)

        // backtrack to uncovered transitions
        // depth-first search
        if (nextEvent == null) {

            log.trace("Story completed --> attempt backtrack")

            storyBase.addStory(currentStory)
            Story newStory = currentStory.minimalClone()

            // reverse order of steps (so we reuse already the previous computation)
            for (int i = currentStory.steps.size() - 2; i >= 0; i--) {
                State step = currentStory.steps[i]
                newStory.steps.remove(i + 1) // remove last state
                newStory.eventsPerStep.remove(i)  // remove last transition
                log.trace("Adjusting story: " + newStory)

                // find the first transition which has not been explored from the step state
                nextEvent = step.findNextEvent()

                log.trace("I can fire: " + nextEvent)

                if (nextEvent != null) {
                    log.trace("========= reload @ state " + step)

                    // opportunistic reload
                    currentState = step
                    currentStory = newStory
                    execution.loadState(currentState)

                    // recompute the emitters which fired
                    execution.firedEmitterEventList = currentStory.getAllTransitionEvents() - (execution.getEmitterInputs() - currentStory.getAllTransitionEvents())

                    log.trace("reloaded story: " + currentStory)

                    log.trace("emitter inputs: " + execution.getEmitterInputs())
                    log.trace("events in story: " + currentStory.getAllTransitionEvents())
                    log.trace("fired emitter event list: " + execution.firedEmitterEventList)
                    break
                }
            }
        }

        if (nextEvent == null) {
            log.trace("Exploration finished")
            return false
        }

        TransitionEvent firedContent = execution.fire(nextEvent)
        currentState = saveConsequent(currentState, [firedContent])

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