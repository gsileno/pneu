package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
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

    // to check whether a "general" state subsumes a "specific" one
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
            } else if (generalPlace.isLink()) {
                log.trace("the place is a link place, jumping...")
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

// TODO: consider subsumption with only events
//
//    // to check whether a "general" story subsumes a "specific" one,
//    // only considering events and not states
//    Boolean concreteEventSubsumption(Story generalStory, Story specificStory) {
//        log.trace("does story " + generalStory + " subsume event-wise " + specificStory + "?")
//
//        if (generalStory.eventsPerStep.size() == 0) {
//            log.trace("there are no events in the general story")
//            if (specificStory.eventsPerStep.size() == 0) {
//                log.trace("there are no events in the specific story as well")
//                return true
//            }
//            return false
//        }
//
//        Integer i = 0, j = 0
//        for (; i < generalStory.steps.size() - 1; i++) {
//            log.trace("general story passes to step " + (i))
//
//            if (generalStory.eventsPerStep[i][0].transition.isLink()) {
//                log.trace("I have found a link transition on the general model, I jump it.")
//                for (i++; i < generalStory.steps.size() - 1; i++) {
//                    log.trace("general story passes to step " + (i))
//                    if (!generalStory.eventsPerStep[i][0].transition.isLink()) {
//                        break
//                    } else {
//                        log.trace("I have found a link transition on the general model, I jump it.")
//                    }
//                }
//            }
//
//            if (i == generalStory.steps.size() - 1) {
//                break
//            }
//
//            Boolean stepFound = false
//            for (; j < specificStory.steps.size() - 1; j++) {
//                if (!specificStory.eventsPerStep[j][0].transition.isLink()) {
//                    // second check the subsumption of the event
//                    // if it is not the case you can think of intermediate steps
//                    if (concreteSubsumption(generalStory.eventsPerStep[i][0], specificStory.eventsPerStep[j][0])) {
//                        stepFound = true
//                        log.trace("specific story passes to step " + (j + 1))
//                        j++
//                        break
//                    }
//                } else {
//                    log.trace("I have found a link transition on the specific model, I jump it.")
//                }
//                log.trace("specific story passes to step " + (j + 1))
//            }
//
//            if (!stepFound) {
//                log.trace("no, story " + generalStory + " does not subsume " + specificStory + ".")
//                return false
//            }
//        }
//
//        log.trace("yes, story " + generalStory + " subsumes " + specificStory + ".")
//        return true
//    }

    // to check whether a "general" story subsumes a "specific" one
    StorySubsumptionOutcome concreteSubsumption(Story generalStory, Story specificStory) {

        StorySubsumptionOutcome outcome = new StorySubsumptionOutcome(generalStory: generalStory, specificStory: specificStory)

        log.trace("does story " + generalStory + " subsume " + specificStory + "?")

        Boolean firstSpecific = true
        Boolean firstGeneral = true
        Boolean concludedSpecific = false
        Boolean concludedGeneral = false

        Integer curGeneralPos = 0, curSpecificPos = 0
        for (int i = curGeneralPos; i < generalStory.steps.size(); i++) {

            log.trace("general story attempts to pass to step " + i)
            Boolean alignmentFound = false

            for (int j = curSpecificPos; j < specificStory.steps.size(); j++) {
                log.trace("specific story attempts to pass to step " + j)

                // check for subsumption of local states
                if (concreteSubsumption(generalStory.steps[i], specificStory.steps[j])) {

                    if (j == specificStory.steps.size() - 1) { // if we are in the last specific step
                        log.trace("specific story has concluded. we cannot proceed further")
                        alignmentFound = true
                        concludedSpecific = true
                    } else if (i == generalStory.steps.size() - 1) { // if we are in the last general step
                        log.trace("general story has concluded. we cannot proceed further")
                        alignmentFound = true
                        concludedGeneral = true
                    } else { // in the general case

                        // if there is a transition link then jump to the next step
                        if (generalStory.eventsPerStep[i][0].transition.isLink()) {
                            log.trace("I have found a link transition on the general model, I jump the event.")
                            if (specificStory.eventsPerStep.get(j) != null) {
                                log.trace("the specific story has an event as well. we can override it.")
                                alignmentFound = true
                            } else {
                                log.trace("the specific story has no remaining events. it cannot be subsumed.")
                                throw new RuntimeException("To be checked")
                            }
                        } else {
                            for (; j < specificStory.steps.size() - 1; j++) {
                                log.trace("checking events between general step $i and specific step $j")
                                if (!specificStory.eventsPerStep[j][0].transition.isLink()) {
                                    if (concreteSubsumption(generalStory.eventsPerStep[i][0], specificStory.eventsPerStep[j][0])) {
                                        alignmentFound = true
                                        break
                                    }
                                } else {
                                    log.trace("I have found a link transition on the specific model, I jump it.")
                                    // there is an assumption there: that the subsumed model is at lower granularity than
                                    // the other one. therefore where link transitions appears in the subsumed model,
                                    // certainly there won't be specified transitions in the subsuming one.
                                    // however, in general we could have mixed situations. TODO: to be better reflected
                                }
                            }
                        }
                    }
                }

                if (alignmentFound) {
                    log.trace("alignment found: general $i vs specific $j")

                    curGeneralPos = i
                    curSpecificPos = j

                    if (firstGeneral) {
                        outcome.leftGeneralLimit = curGeneralPos
                        firstGeneral = false
                    }
                    if (firstSpecific) {
                        outcome.leftSpecificLimit = curSpecificPos
                        firstSpecific = false
                    }

                    outcome.rightGeneralLimit = curGeneralPos
                    outcome.rightSpecificLimit = curSpecificPos

                    if (concludedSpecific) {
                        outcome.mapIdentifiers = mapIdentifiers
                        return outcome
                    } else {
                        if (!concludedGeneral) curSpecificPos++  // adjustment for the next cycle
                    }
                    break
                }
            }

            if (!alignmentFound) {
                if (firstGeneral && (i < generalStory.steps.size() - 1)) {
                    log.trace("no, story " + generalStory + " does not subsume " + specificStory + " from this step.")
                    log.trace("attempting by changing the left general limit.")
                } else {
                    log.trace("no, story " + generalStory + " does not subsume " + specificStory + ".")

                    if (outcome.leftGeneralLimit != null && outcome.rightGeneralLimit != null && outcome.leftSpecificLimit != null && outcome.rightSpecificLimit != null)
                        log.trace("but well, story " + generalStory + " partially subsume " + specificStory + ": (" +
                                outcome.leftGeneralLimit + ", " + outcome.rightGeneralLimit + ") vs (" +
                                outcome.leftSpecificLimit + ", " + outcome.rightSpecificLimit + ")")
                    log.trace("I cannot find correspondences for step " + generalStory.steps[i].placesToString() + " / event " + generalStory.eventsPerStep[i])

                    outcome.mapIdentifiers = mapIdentifiers
                    return outcome
                }
            }
        }

        if (outcome.leftGeneralLimit != null && outcome.rightGeneralLimit != null && outcome.leftSpecificLimit != null && outcome.rightSpecificLimit != null) {
            if (outcome.leftGeneralLimit == 0 && outcome.rightGeneralLimit == generalStory.steps.size() - 1) {
                log.trace("yes, story " + generalStory + " subsumes " + specificStory + ".")
                if (outcome.leftSpecificLimit > 0 || outcome.rightSpecificLimit < specificStory.steps.size() - 1) {
                    log.trace("however, boundaries details have been neglected: (" +
                            outcome.leftSpecificLimit + ", " + outcome.rightSpecificLimit + "), intead of (0, "
                            + specificStory.steps.size() + ")")
                }
            } else {
                log.trace("well, story " + generalStory + " partially subsume " + specificStory + ": (" +
                        outcome.leftGeneralLimit + ", " + outcome.rightGeneralLimit + ") vs (" +
                        outcome.leftSpecificLimit + ", " + outcome.rightSpecificLimit + ")")
            }
        } else {
            throw new RuntimeException("You should not be here.")
        }

        outcome.mapIdentifiers = mapIdentifiers
        return outcome
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

                StorySubsumptionOutcome eval = concreteSubsumption(generalStory, specificStory)
                log.debug(eval.toLog())

                if (eval.type() == StorySubsumptionOutcome.Type.SUBSUMES) {
                    storyFound = true
                    break
                }
            }
            if (!storyFound) {
                log.trace("no, it does not. I cannot find any general story which subsumes this specific story")
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

    static StorySubsumptionOutcome subsumes(Story generalStory, Story specificStory) {
        Subsumption subsumption = new Subsumption()
        subsumption.concreteSubsumption(generalStory, specificStory)
    }
}
