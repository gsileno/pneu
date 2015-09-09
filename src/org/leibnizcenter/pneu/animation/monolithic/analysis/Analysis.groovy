package org.leibnizcenter.pneu.animation.monolithic.analysis

import groovy.json.JsonBuilder
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

        log.trace("attempt to record current state, after firing: " + firedEvents)
        State state = stateBase.save(execution)
        log.trace("recorded state: " + state)

        if (antecedent) {
            log.trace("antecedent: "+antecedent)
            log.trace("antecedent transition/state map: "+antecedent.transitionEventStateMap)
        }

        currentStory.addStep(state)

        if (firedEvents.size() > 0) {
            if (firedEvents.size() > 1)
                throw new RuntimeException("ERROR!! I should consider only one transition per time")

            // BUG: there was a problem with the hashcode. repaired manually
            TransitionEvent firedEvent = firedEvents[0]
            log.trace("fired event: " + firedEvents[0])

            TransitionEvent key
            for (event in antecedent.transitionEventStateMap.keySet()) {
                log.trace("event to be compared: " + event)


                if (event.transition.compare(firedEvent.transition) &&
                        event.token.compare(firedEvent.token)) {
                    key = event
                    break;
                } else {
                    if (!event.transition.compare(firedEvent.transition)) {
                        log.trace("transitions are different! "+event.transition+" vs "+firedEvent.transition)
                    }
                    if (!event.token.compare(firedEvent.token)) {
                        log.trace("tokens are different! "+event.token+" vs "+firedEvent.token)
                    }

                }
            }

            if (key == null)
                throw new RuntimeException("You should not be here.")

            antecedent.transitionEventStateMap[key] = state
            currentStory.addEvents(firedEvents)
        }

        return state
    }

    Boolean step() {

        if (currentStory == null) currentStory = new Story()
        if (currentState == null) currentState = saveConsequent() // for state 0

        log.trace("current story: " + currentStory)
        log.trace("current state: " + currentState)

        TransitionEvent nextEvent

        // first type of completion:
        // the currentState has been already found in the past the story
        Boolean complete = (currentStory.steps.findAll() { it == currentState}.size() > 1)
        if (complete) {
            log.trace("state already found in the story, restarting again.")
        } else {
            // second type of completion: depletion of possible firings
            // find the first transition which has not been explored from the current state
            nextEvent = currentState.findNextEvent()
            log.trace("next event: " + nextEvent)
            complete = (nextEvent == null)
        }

        // backtrack to uncovered transitions
        // depth-first search
        if (complete) {

            log.trace("story completed --> attempt backtrack")

            storyBase.addStory(currentStory)
            Story newStory = currentStory.minimalClone()

            // reverse order of steps (so we reuse already the previous computation)
            for (int i = currentStory.steps.size() - 2; i >= 0; i--) {
                State step = currentStory.steps[i]
                newStory.steps.remove(i + 1) // remove last state
                newStory.eventsPerStep.remove(i)  // remove last transition
                log.trace("readjusting story: " + newStory)

                // find the first transition which has not been explored from the step state
                nextEvent = step.findNextEvent()

                if (nextEvent == null)
                    log.trace("no more event remain to be explored.")
                else
                    log.trace("I will fire: " + nextEvent)

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
            log.trace("exploration finished")
            return false
        }

        log.trace("executing event: " + nextEvent)
        TransitionEvent firedContent = execution.fire(nextEvent)
        log.trace("fired content: " + firedContent)

        currentState = saveConsequent(currentState, [firedContent])

        return true
    }

    void exportToLog(String filename, String path = "out/log/analysis") {
        File folder
        String outputFile

        folder = new File(path)
        if (!folder.exists()) folder.mkdirs()

        outputFile = path + '/' + filename + ".analysis.log"

        new File(outputFile).withWriter { out ->
            out.println("Summary: \n" + storyBase.toLog())
            out.println("Stories: \n" + storyBase)
            out.println("States: \n" + stateBase)
        }
    }

//    // for cache
//
//    // save to json file
//    void saveToFile(String filename, String path = "out/analysis") {
//        File folder
//        String outputFile
//
//        folder = new File(path)
//        if (!folder.exists()) folder.mkdirs()
//
//        outputFile = path + '/' + filename + ".analysis.serialization.json"
//
//        new File(outputFile).withWriter { out ->
//            out << new JsonBuilder(this).toPrettyString()
//        }
//    }
//
//    // load from json file
//    static Analysis loadFromFile(String filename, String path = "out/analysis") {
//        return null
//    }

}