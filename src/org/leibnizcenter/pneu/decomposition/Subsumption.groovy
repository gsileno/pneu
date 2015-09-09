package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent


@Log4j
class Subsumption {

    Map<String, Map<String, String>> mapIdentifiers = [:]

    // compute all possible executions in terms of states/transition events
    static private Analysis analyze(Net net) {
        NetRunner netRunner = new NetRunner()
        netRunner.load(net)
        netRunner.analyse()
        netRunner.analysis
    }

    // to check whether a "general" position (local state) concreteSubsumption a "specific" one
    Boolean concreteSubsumption(Place generalPlace, Place specificPlace) {
        log.trace("does local state " + generalPlace + " subsume " + specificPlace + "?")
        List<Token> specificTokens = specificPlace.marking

        // for all general tokens, there should be at least a *distinct* specific token which is subsumed
        for (generalToken in generalPlace.marking) {
            Token token
            for (specificToken in specificTokens) {
                if (generalToken.subsumes(specificToken, mapIdentifiers)) {
                    token = specificToken
                }
            }
            if (token == null) {
                log.trace("no, local state " + generalPlace + " does not subsume " + specificPlace + ".")
                return false
            }
            specificTokens = specificTokens - token
        }
        log.trace("yes, local state " + generalPlace + " subsumes " + specificPlace + ".")
        return true
    }

    // to check whether a "general" event concreteSubsumption a "specific" one
    Boolean concreteSubsumption(TransitionEvent generalEvent, TransitionEvent specificEvent) {
        log.trace("does event " + generalEvent + " subsume " + specificEvent + "?")

        if (!generalEvent.token.subsumes(specificEvent.token, mapIdentifiers)) {
            log.trace("no, event " + generalEvent + " does not subsume " + specificEvent + ".")
            return false
        }
        log.trace("yes, event " + generalEvent + " subsumes " + specificEvent + ".")
        return true
    }

    // to check whether a "general" state concreteSubsumption a "specific" one
    // as we are considering executions, we are not any more dealing with potential transitions
    // but we can check only the steps
    Boolean concreteSubsumption(State generalState, State specificState) {
        log.trace("does state " + generalState.placesToString() + " subsume " + specificState.placesToString() + "?")

        List<Place> specificPlaces = specificState.placeList

        // for all general places, there should be at least a *distinct* specific place which is subsumed
        for (generalPlace in generalState.placeList) {
            log.trace("checking general place: " + generalPlace)
            if (!generalPlace.isLink()) {
                Place p

                for (specificPlace in specificPlaces) {
                    log.trace("considering specific place: " + specificPlace)
                    if (concreteSubsumption(generalPlace, specificPlace)) {
                        p = specificPlace
                        break
                    }
                }
                if (p == null) {
                    log.trace("no, state " + generalState + " does not subsume " + specificState + ".")
                    return false
                }
                specificPlaces = specificPlaces - p
            } else {
                log.trace("it is a link, jumping...")
            }
        }
        log.trace("yes, state " + generalState + " subsumes " + specificState + ".")
        return true
    }

    // to check whether a "general" story subsumes a "specific" one
    Boolean concreteSubsumption(Story generalStory, Story specificStory) {
        log.trace("does story " + generalStory + " subsume " + specificStory + "?")

        if (generalStory.eventsPerStep.size() == 0) {
            log.trace("there are no events in the general story")
            return false
        }

        Integer i = 0, j = 0
        for (; i < generalStory.steps.size() - 1; i++) {
            for (; i < generalStory.steps.size() - 1; i++) {
                if (!generalStory.eventsPerStep[i][0].transition.isLink()) {
                    break
                } else {
                    log.trace("I have found a link transition on the general model, I jump it.")
                }
                log.trace("general story passes to step "+(i+1))
            }

            if (i == generalStory.steps.size() - 1) {
                break
            }

            Boolean stepFound = false
            for (; j < specificStory.steps.size() - 1; j++) {
                if (!specificStory.eventsPerStep[j][0].transition.isLink()) {
                    // second check the subsumption of the event
                    // if it is not the case you can think of intermediate steps
                    if (concreteSubsumption(generalStory.eventsPerStep[i][0], specificStory.eventsPerStep[j][0])) {
                        stepFound = true
                        log.trace("specific story passes to step "+(j+1))
                        j++
                        break
                    }
                } else {
                    log.trace("I have found a link transition on the specific model, I jump it.")
                }
                log.trace("specific story passes to step "+(j+1))
            }

            if (!stepFound) {
                log.trace("no, story " + generalStory + " does not subsume " + specificStory + ".")
                return false
            }
            log.trace("general story passes to step "+(i+1))
        }

        log.trace("yes, story " + generalStory + " subsumes " + specificStory + ".")
        return true
    }

// to check whether a "general" net concreteSubsumption a "specific" one
    Boolean concreteSubsumption(Net generalNet, Net specificNet) {

        Analysis generalNetAnalysis = analyze(generalNet)
        Analysis specificNetAnalysis = analyze(specificNet)

        log.trace("does this general model subsume the specific one?")

        // check if, all execution path in the specific story
        // have at least a path which subsume them in the general story
        for (specificStory in specificNetAnalysis.storyBase.base) {
            Boolean storyFound = false
            for (generalStory in generalNetAnalysis.storyBase.base) {
                if (concreteSubsumption(generalStory, specificStory)) {
                    storyFound = true
                    break
                }
            }
            if (!storyFound) {
                log.trace("no, it does not.")
                return false
            }
        }

        log.trace("yes, it does.")
        return true
    }

    static Boolean subsumes(Net generalNet, Net specificNet) {
        Subsumption subsumption = new Subsumption()
        subsumption.concreteSubsumption(generalNet, specificNet)
    }

}
