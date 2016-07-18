package pneu.subsumption

import groovy.util.logging.Log4j
import pneu.animation.monolithic.analysis.Analysis
import pneu.animation.monolithic.analysis.Story

@Log4j
class AnalysisSubsumption {

    Map<String, Map<String, String>> mapIdentifiers = [:]

    Analysis generalAnalysis
    Analysis specificAnalysis

    Type type

    Map<Story, Map<Story, StorySubsumption>> generalSpecificStorySubsumptionMap = [:]

    enum Type {
        NONE,
        SUBSUMES,
        PARTIALLY_SUBSUMES,
    }

    AnalysisSubsumption(Analysis inputGeneralAnalysis, Analysis inputSpecificAnalysis, Boolean fullAnalysis = false) {

        generalAnalysis = inputGeneralAnalysis
        specificAnalysis = inputSpecificAnalysis

        generalAnalysis.exportToLog("generalNet")
        specificAnalysis.exportToLog("specificNet")

        type = Type.NONE

        log.trace("does this general model subsume the specific one?")

        // check if, all execution path in the specific story
        // have at least a path which subsume them in the general story
        for (specificStory in specificAnalysis.storyBase.base) {
            log.trace("considering specific story " + specificStory)
            Boolean storyFound = false
            for (generalStory in generalAnalysis.storyBase.base) {

                StorySubsumption eval = new StorySubsumption(generalStory, specificStory, mapIdentifiers)
                log.debug(eval.toLog())

                if (eval.type() != StorySubsumption.Type.NONE) {
                    if (generalSpecificStorySubsumptionMap[generalStory] == null) {
                        generalSpecificStorySubsumptionMap[generalStory] = [:]
                    }
                    generalSpecificStorySubsumptionMap[generalStory][specificStory] = eval
                    if (eval.type() == StorySubsumption.Type.SUBSUMES) {
                        if (type != Type.PARTIALLY_SUBSUMES) type = Type.SUBSUMES
                        storyFound = true
                        if (!fullAnalysis) break // FOR optimization: find only the first one
                    } else {
                        type = Type.PARTIALLY_SUBSUMES
                    }
                }
            }

            if (!fullAnalysis && !storyFound) {
                log.trace("no, it does not. I cannot find any general story which subsumes this specific story")
                type = Type.NONE
                return
            }
        }

        if (!fullAnalysis) {
            log.trace("yes, it does.")
            type = Type.SUBSUMES
        }
    }


}

