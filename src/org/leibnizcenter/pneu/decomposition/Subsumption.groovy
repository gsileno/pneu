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
import org.leibnizcenter.pneu.examples.CommonConstructs


@Log4j
class Subsumption {

    Map<String, Map<String, String>> mapIdentifiers = [:]

    // to check whether a "general" position (local state) concreteSubsumption a "specific" one
    Boolean concreteSubsumption(Place generalPlace, Place specificPlace) {
        log.trace("does local state " + generalPlace.label() + " subsume " + specificPlace.label() + "?")

        if (!generalPlace.subsumes(specificPlace)) {
            log.trace("no, local state " + generalPlace.label() + " does not subsume " + specificPlace.label() + " (because of its structure).")
            return false
        }

        List<Token> generalTokens = generalPlace.marking
        List<Token> specificTokens = specificPlace.marking

        if (specificTokens.size() < generalTokens.size()) {
            log.trace("no, as the specific state has less tokens than the generic one.")
            return false
        } else if (generalTokens.size() == 0) {
            throw new RuntimeException("Not yet considered.")
        } else {
            // for all general tokens, there should be at least a *distinct* specific token which is subsumed
            for (generalToken in generalPlace.marking) {
                Token token
                for (specificToken in specificTokens) {
                    if (generalToken.subsumes(specificToken, mapIdentifiers)) {
                        token = specificToken
                    }
                }
                if (token == null) {
                    log.trace("no, local state " + generalPlace.label() + " does not subsume " + specificPlace.label() + " (because of its content).")
                    return false
                }
                specificTokens = specificTokens - token
            }
            log.trace("yes, local state " + generalPlace.label() + " subsumes " + specificPlace.label() + ".")
            return true
        }
    }

    // to check whether a "general" event concreteSubsumption a "specific" one
    Boolean concreteSubsumption(TransitionEvent generalEvent, TransitionEvent specificEvent) {
        log.trace("does event " + generalEvent.label() + " subsume " + specificEvent.label() + "?")

        if (!generalEvent.transition.subsumes(specificEvent.transition)) {
            log.trace("no, event " + generalEvent.label() + " does not subsume " + specificEvent.label() + " (because of its structure).")
            return false
        }

        if (!generalEvent.token.subsumes(specificEvent.token, mapIdentifiers)) {
            log.trace("no, event " + generalEvent.label() + " does not subsume " + specificEvent.label() + " (because of its content).")
            return false
        }
        log.trace("yes, event " + generalEvent.label() + " subsumes " + specificEvent.label() + ".")
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

            if (generalPlace.marking.size() == 0) {
                log.trace("the place is empty, jumping...")
            } else {
                Place p

                for (specificPlace in specificPlaces) {
                    log.trace("considering specific place: " + specificPlace)
                    if (concreteSubsumption(generalPlace, specificPlace)) {
                        p = specificPlace
                        break
                    }
                }
                if (p == null) {
                    log.trace("no, general state " + generalState + " does not subsume specific state " + specificState + ".")
                    return false
                }
                specificPlaces = specificPlaces - p
            }
        }
        log.trace("yes, general state " + generalState + " subsumes specific state " + specificState + ".")
        return true
    }

    // to check whether a "general" state subsumes a "specific" one, in abstract terms,
    // i.e. in checking only for the place labels
    static Boolean abstractSubsumption(State generalState, State specificState) {
        log.trace("does state " + generalState.placesToString() + " abstractly subsume " + specificState.placesToString() + "?")

        List<Place> specificPlaces = specificState.placeList

        // for all general places, there should be at least a *distinct* specific place which is subsumed
        for (generalPlace in generalState.placeList) {

            log.trace("checking general place: " + generalPlace)
            Place p
            for (specificPlace in specificPlaces) {
                log.trace("considering specific place: " + specificPlace)
                if (generalPlace.subsumes(specificPlace)) {
                    p = specificPlace
                    break
                }
            }
            if (p == null) {
                log.trace("no, general state " + generalState + " does not subsume specific state " + specificState + ".")
                return false
            }
            specificPlaces = specificPlaces - p
        }
        log.trace("yes, general state " + generalState + " abstractly subsumes specific state " + specificState + ".")
        return true
    }

    // to check whether a "general" story subsumes a "specific" one
    Boolean concreteEventSubsumption(Story generalStory, Story specificStory) {
        log.trace("does story " + generalStory + " subsume event-wise " + specificStory + "?")

        if (generalStory.eventsPerStep.size() == 0) {
            log.trace("there are no events in the general story")
            if (specificStory.eventsPerStep.size() == 0) {
                log.trace("there are no events in the specific story as well")
                return true
            }
            return false
        }

        Integer i = 0, j = 0
        for (; i < generalStory.steps.size() - 1; i++) {
            log.trace("general story passes to step " + (i))

            if (generalStory.eventsPerStep[i][0].transition.isLink()) {
                log.trace("I have found a link transition on the general model, I jump it.")
                for (i++; i < generalStory.steps.size() - 1; i++) {
                    log.trace("general story passes to step " + (i))
                    if (!generalStory.eventsPerStep[i][0].transition.isLink()) {
                        break
                    } else {
                        log.trace("I have found a link transition on the general model, I jump it.")
                    }
                }
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
                        log.trace("specific story passes to step " + (j + 1))
                        j++
                        break
                    }
                } else {
                    log.trace("I have found a link transition on the specific model, I jump it.")
                }
                log.trace("specific story passes to step " + (j + 1))
            }

            if (!stepFound) {
                log.trace("no, story " + generalStory + " does not subsume " + specificStory + ".")
                return false
            }
        }

        log.trace("yes, story " + generalStory + " subsumes " + specificStory + ".")
        return true
    }

    // to check whether a "general" story subsumes a "specific" one
    Boolean concreteSubsumption(Story generalStory, Story specificStory) {
        log.trace("does story " + generalStory + " subsume " + specificStory + "?")

        if (generalStory.eventsPerStep.size() == 0) {
            log.trace("there are no events in the general story")
            if (specificStory.eventsPerStep.size() == 0) {
                log.trace("there are no events in the specific story as well")
                log.trace("checking state subsumptions")
                return concreteSubsumption(generalStory.steps[0], specificStory.steps[0])
            }
            return false
        }

        Integer i = 0, j = 0
        for (; i < generalStory.steps.size(); i++) {
            log.trace("general story passes to step " + (i))

            Boolean stepFound = false
            for (; j < specificStory.steps.size(); j++) {
                log.trace("specific story passes to step " + (j))

                if (concreteSubsumption(generalStory.steps[i], specificStory.steps[j])) {

                    if (i < generalStory.steps.size() - 1) {
                        log.trace("looking for an event not related to a link transition (step $i)")

                        if (generalStory.eventsPerStep[i][0].transition.isLink()) {
                            log.trace("I have found a link transition on the general model, I jump it.")

                            for (; i < generalStory.steps.size() - 1; i++) {
                                log.trace("general story passes to step " + (i))
                                if (!generalStory.eventsPerStep[i][0].transition.isLink()) {
                                    break
                                } else {
                                    log.trace("I have found a link transition on the general model, I jump it.")
                                }
                            }
                        }
                        if (i == generalStory.steps.size() - 1) {
                            log.trace("found no events in the general story which is not related to a link transition")

                            for (; j < specificStory.steps.size() - 1; j++) {
                                if (!specificStory.eventsPerStep[j][0].transition.isLink()) {
                                    log.trace("but I found it in the specific story")
                                    return false
                                }
                            }
                            return true
                        }

                        for (; j < specificStory.steps.size() - 1; j++) {
                            log.trace("specific story passes to step " + (j))
                            if (!specificStory.eventsPerStep[j][0].transition.isLink()) {
                                if (concreteSubsumption(generalStory.eventsPerStep[i][0], specificStory.eventsPerStep[j][0])) {
                                    stepFound = true
                                    log.trace("specific story passes to step " + (j + 1))
                                    j++
                                    break
                                }
                            } else {
                                log.trace("I have found a link transition on the specific model, I jump it.")
                            }
                        }
                    } else {
                        stepFound = true
                    }
                }

                if (stepFound) break
            }

            if (!stepFound) {
                log.trace("no, story " + generalStory + " does not subsume " + specificStory + ".")
                return false
            }
        }

        log.trace("yes, story " + generalStory + " subsumes " + specificStory + ".")
        return true
    }

    // to check whether a "general" net subsumes a "specific" one
    Boolean concreteSubsumption(Net generalNet, Net specificNet) {

        Analysis generalNetAnalysis = Analysis.analyse(generalNet)
        Analysis specificNetAnalysis = Analysis.analyse(specificNet)

        concreteSubsumption(generalNetAnalysis, specificNetAnalysis)
    }

    Boolean concreteSubsumption(Analysis generalNetAnalysis, Analysis specificNetAnalysis) {

        generalNetAnalysis.exportToLog("generalNet")
        specificNetAnalysis.exportToLog("specificNet")

        log.trace("does this general model subsume the specific one?")

        // check if, all execution path in the specific story
        // have at least a path which subsume them in the general story
        for (specificStory in specificNetAnalysis.storyBase.base) {
            log.trace("considering specific story " + specificStory)
            Boolean storyFound = false
            for (generalStory in generalNetAnalysis.storyBase.base) {
                if (concreteSubsumption(generalStory, specificStory)) {
                    storyFound = true
                    break
                }
            }
            if (!storyFound) {
                log.trace("no, it does not. I cannot find any general story which subsume the specific story")
                return false
            }
        }

        log.trace("yes, it does.")
        return true
    }

    static Boolean subsumes(Net generalNet, Net specificNet) {
        Subsumption subsumption = new Subsumption()

        generalNet.exportToLog("generalNet")
        specificNet.exportToLog("specificNet")

        subsumption.concreteSubsumption(generalNet, specificNet)
    }

    static Boolean subsumes(Analysis generalNetAnalysis, Analysis specificNetAnalysis) {
        Subsumption subsumption = new Subsumption()
        subsumption.concreteSubsumption(generalNetAnalysis, specificNetAnalysis)
    }

    static Boolean subsumes(Story generalStory, Story specificStory) {
        Subsumption subsumption = new Subsumption()
        subsumption.concreteSubsumption(generalStory, specificStory)
    }
}
