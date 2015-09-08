package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent


@Log4j
class Subsumption {

    // compute all possible executions in terms of states/transition events
    static private Analysis analyze(Net net) {
        NetRunner netRunner = new NetRunner()
        netRunner.load(net)
        netRunner.analyse()
        netRunner.analysis
    }

    // to check whether a "general" position (local state) subsumes a "specific" one
    static Boolean subsumes(Place genericPlace, Place specificPlace) {
        List<Token> specificTokens = specificPlace.marking

        if (!genericPlace.subsumes(specificPlace)) {
            return false
        }

        // for all general tokens, there should be at least a *distinct* specific token which is subsumed
        for (genericToken in genericPlace.marking) {
            Token token
            for (specificToken in specificTokens) {
                if (genericToken.subsumes(specificToken)) {
                    token = specificToken
                }
            }
            if (token == null) return false
            specificTokens = specificTokens - token
        }

        return true
    }

    // to check whether a "general" event subsumes a "specific" one
    static Boolean subsumes(TransitionEvent genericEvent, TransitionEvent specificEvent) {
        if (!genericEvent.transition.subsumes(specificEvent.transition)) {
            return false
        }

        if (!genericEvent.token.subsumes(specificEvent.token))
            return false
        return true
    }

    // to check whether a "general" state subsumes a "specific" one
    // as we are considering executions, we are not any more dealing with potential transitions
    // but we can check only the steps
    static Boolean subsumes(State generalState, State specificState) {

        List<Place> specificPlaces = specificState.placeList

        // for all general places, there should be at least a *distinct* specific place which is subsumed
        for (generalPlace in generalState.placeList) {
            Place p
            for (specificPlace in specificPlaces) {
                if (subsumes(generalPlace, specificPlace)) {
                    p = specificPlace
                    break
                }
            }
            if (p == null) return false
            specificPlaces = specificPlaces - p
        }

        return true
    }

    // to check whether a "general" story subsumes a "specific" one
    static Boolean subsumes(Story generalStory, Story specificStory) {

        Integer i = 0, j = 0
        for (; i < generalStory.steps.size(); i++) {
//            log.trace("general story step (size): $i, (${generalStory.steps.size()})")
//            log.trace("specific story step (size): $i, (${specificStory.steps.size()})")

            Boolean stepFound = false
            for (; j < specificStory.steps.size(); j++) {

                // first check the subsumption of the state,
                // if it is not the case you can
                // think of intermediate steps
                if (subsumes(generalStory.steps[i], specificStory.steps[j])) {

                    // TO CHECK: I do not completely agree with that definition
                    if (generalStory.eventsPerStep.size() == 0) {
//                        log.trace("there are no transitions any more in the general story")
                        return true
                    }

                    // there is always an event less than the number of steps
                    if (i == generalStory.steps.size - 1 || j == specificStory.steps.size() - 1) {
                        stepFound = true
                        break
                    } else {
                        for (; j < specificStory.steps.size() - 1; j++) {
                            // second check the subsumption of the event
                            // if it is not the case you can think of intermediate steps
                            if (subsumes(generalStory.eventsPerStep[i][0], specificStory.eventsPerStep[j][0])) {
                                stepFound = true
                                break
                            }
                        }
                    }
                }
                // I found it, pass on the next step
                if (stepFound) {
                    j++
                    break
                }
            }
            if (!stepFound) return false
        }

        return true
    }

    // to check whether a "general" net subsumes a "specific" one
    static Boolean subsumes(Net generalNet, Net specificNet) {

        Analysis generalNetAnalysis = analyze(generalNet)
        Analysis specificNetAnalysis = analyze(specificNet)

        // check if, all execution path in the specific story
        // have at least a path which subsume them in the general story
        for (specificStory in specificNetAnalysis.storyBase.base) {
            Boolean storyFound = false
            for (generalStory in generalNetAnalysis.storyBase.base) {
                if (subsumes(generalStory, specificStory)) {
                    storyFound = true
                    break
                }
            }
            if (!storyFound) return false
        }

        return true
    }

}
