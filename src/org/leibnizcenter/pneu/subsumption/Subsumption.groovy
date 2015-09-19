package org.leibnizcenter.pneu.subsumption

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

    static Boolean subsumes(Net generalNet, Net specificNet) {
        NetSubsumption eval = new NetSubsumption(generalNet, specificNet)

        if (eval.type == NetSubsumption.Type.SUBSUMES) return true
        else return false
    }

    static Boolean subsumes(Analysis generalAnalysis, Analysis specificAnalysis) {
        AnalysisSubsumption eval = new AnalysisSubsumption(generalAnalysis, specificAnalysis)

        if (eval.type == AnalysisSubsumption.Type.SUBSUMES) return true
        else return false
    }

    static Boolean subsumes(Story generalStory, Story specificStory) {
        StorySubsumption eval = new StorySubsumption(generalStory, specificStory)

        if (eval.type() == StorySubsumption.Type.SUBSUMES) return true
        else return false
    }
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