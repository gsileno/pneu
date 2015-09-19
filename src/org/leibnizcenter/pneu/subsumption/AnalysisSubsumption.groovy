package org.leibnizcenter.pneu.subsumption

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis

@Log4j
class AnalysisSubsumption {

    Map<String, Map<String, String>> mapIdentifiers = [:]

    Analysis generalAnalysis
    Analysis specificAnalysis

    Type type

    enum Type {
        NONE,
        SUBSUMES,
        ANCILLARY_SUBSUMES
    }

    AnalysisSubsumption(Analysis inputGeneralAnalysis, Analysis inputSpecificAnalysis) {

        generalAnalysis = inputGeneralAnalysis
        specificAnalysis = inputSpecificAnalysis

        generalAnalysis.exportToLog("generalNet")
        specificAnalysis.exportToLog("specificNet")

        log.trace("does this general model subsume the specific one?")

        // check if, all execution path in the specific story
        // have at least a path which subsume them in the general story
        for (specificStory in specificAnalysis.storyBase.base) {
            log.trace("considering specific story " + specificStory)
            Boolean storyFound = false
            for (generalStory in generalAnalysis.storyBase.base) {

                StorySubsumption eval = new StorySubsumption(generalStory, specificStory, mapIdentifiers)
                log.debug(eval.toLog())

                if (eval.type() == StorySubsumption.Type.SUBSUMES) {
                    storyFound = true
                    break
                }
            }

            if (!storyFound) {
                log.trace("no, it does not. I cannot find any general story which subsumes this specific story")
                type = Type.NONE
                return
            }
        }
        log.trace("yes, it does.")
        type = Type.SUBSUMES
    }

}

