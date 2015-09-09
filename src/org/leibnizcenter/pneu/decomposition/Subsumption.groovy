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

    Map<String, Map<String, String>> mapAnonymousIdentifiers = [:]

    // compute all possible executions in terms of states/transition events
    static private Analysis analyze(Net net) {
        NetRunner netRunner = new NetRunner()
        netRunner.load(net)
        netRunner.analyse()
        netRunner.analysis
    }

    // to check whether a "general" position (local state) subsumes a "specific" one
    Boolean subsumes(Place generalPlace, Place specificPlace) {
        log.trace("Does place "+generalPlace+" subsume "+specificPlace+"?")
        List<Token> specificTokens = specificPlace.marking


        // for all general tokens, there should be at least a *distinct* specific token which is subsumed
        for (generalToken in generalPlace.marking) {
            Token token
            for (specificToken in specificTokens) {
                if (generalToken.subsumes(specificToken, mapAnonymousIdentifiers)) {
                    token = specificToken
                }
            }
            if (token == null) {
                log.trace("No, place "+generalPlace+" do not subsume "+specificPlace+".")
                return false
            }
            specificTokens = specificTokens - token
        }
        log.trace("Yes, place "+generalPlace+" subsumes "+specificPlace+".")
        return true
    }

    // to check whether a "general" event subsumes a "specific" one
    Boolean subsumes(TransitionEvent generalEvent, TransitionEvent specificEvent) {
        log.trace("Does event "+generalEvent+" subsume "+specificEvent+"?")

        if (!generalEvent.token.subsumes(specificEvent.token, mapAnonymousIdentifiers)) {
            log.trace("No, event "+generalEvent+" do not subsume "+specificEvent+".")
            return false
        }
        log.trace("Yes, event "+generalEvent+" subsumes "+specificEvent+".")
        return true
    }

    // to check whether a "general" state subsumes a "specific" one
    // as we are considering executions, we are not any more dealing with potential transitions
    // but we can check only the steps
    Boolean subsumes(State generalState, State specificState) {
        log.trace("Does state "+generalState.placesToString()+" subsume "+specificState.placesToString()+"?")

        List<Place> specificPlaces = specificState.placeList

        // for all general places, there should be at least a *distinct* specific place which is subsumed
        for (generalPlace in generalState.placeList) {
            if (!generalPlace.isLink()) {
                Place p

                for (specificPlace in specificPlaces) {
                    if (subsumes(generalPlace, specificPlace)) {
                        p = specificPlace
                        break
                    }
                }
                if (p == null) {
                    log.trace("No, state " + generalState + " do not subsume " + specificState + ".")
                    return false
                }
                specificPlaces = specificPlaces - p
            }
        }
        log.trace("Yes, state "+generalState+" subsumes "+specificState+".")
        return true
    }

    // to check whether a "general" story subsumes a "specific" one
    Boolean subsumes(Story generalStory, Story specificStory) {
        log.trace("Does story "+generalStory+" subsume "+specificStory+"?")

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
                    if (i == generalStory.steps.size() - 1 || j == specificStory.steps.size() - 1) {
                        stepFound = true
                        break
                    } else {

                        for (; i < generalStory.steps.size(); i++) {
                            if (!generalStory.eventsPerStep[i][0].transition.isLink()) {
                                break
                            } else {
                                log.trace("I have found a link transition on the general model, I jump it.")
                            }
                        }
                        if (i == generalStory.steps.size()) {
                            log.trace("I have found only link transitions after having settled the state.")
                        }

                        for (; j < specificStory.steps.size() - 1; j++) {

                            if (!specificStory.eventsPerStep[j][0].transition.isLink()) {
                                // second check the subsumption of the event
                                // if it is not the case you can think of intermediate steps
                                if (subsumes(generalStory.eventsPerStep[i][0], specificStory.eventsPerStep[j][0])) {
                                    stepFound = true
                                    break
                                }
                            } else {
                                log.trace("I have found a link transition on the specific model, I jump it.")
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
            if (!stepFound) {
                log.trace("No, story "+generalStory+" do not subsume "+specificStory+".")
                return false
            }
        }
        log.trace("Yes, story "+generalStory+" subsumes "+specificStory+".")
        return true
    }

    // to check whether a "general" net subsumes a "specific" one
    Boolean subsumes(Net generalNet, Net specificNet) {

        Analysis generalNetAnalysis = analyze(generalNet)
        Analysis specificNetAnalysis = analyze(specificNet)

        log.trace("Does this general model subsumes the specific one?")

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
            if (!storyFound) {
                log.trace("No, it does not.")
                return false
            }
        }

        log.trace("Yes, it does.")
        return true
    }

}
