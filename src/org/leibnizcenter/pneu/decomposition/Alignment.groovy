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
class Alignment {

    static Map<Story, Map<Story, RelationType>> partialAlignmentTest(Net left, Net right) {

        // analyse
        Analysis leftAnalysis = Analysis.analyse(left)
        Analysis rightAnalysis = Analysis.analyse(right)

        partialAlignmentTest(leftAnalysis, rightAnalysis)
    }

    static Map<Story, Map<Story, RelationType>> partialAlignmentTest(Analysis left, Analysis right) {

        // decompose in partial stories
        AnalysisSESEDecomposer leftDecomposition = new AnalysisSESEDecomposer()
        StoryTree leftTree = leftDecomposition.decompose(left)

        AnalysisSESEDecomposer rightDecomposition = new AnalysisSESEDecomposer()
        StoryTree rightTree = rightDecomposition.decompose(right)

        partialAlignmentTest(leftTree, rightTree)
    }

    static Map<Story, Map<Story, RelationType>> partialAlignmentTest(StoryTree left, StoryTree right) {

        // look for beginning/end subsumed
        List<Story> leftStories = left.flatten()
        List<Story> rightStories = right.flatten()

        partialAlignmentTest(leftStories, rightStories)
    }

    static Map<Story, Map<Story, RelationType>> partialAlignmentTest(List<Story> leftStories, List<Story> rightStories) {

        Map<Story, Map<Story, RelationType>> partialMap = [:]

        for (leftStory in leftStories) {
            for (rightStory in rightStories) {
                Boolean gs = Subsumption.subsumes(leftStory, rightStory)
                Boolean sg = Subsumption.subsumes(rightStory, leftStory)

                RelationType relation

                if (gs && sg) relation = RelationType.EQUIVALENT
                else if (gs) relation = RelationType.SUBSUMES
                else if (sg) relation = RelationType.IS_SUBSUMED
                else relation = RelationType.NONE

                if (!partialMap.containsKey(leftStory)) {
                    partialMap[leftStory] = [:]
                }

                if (relation != RelationType.NONE)
                    partialMap[leftStory][rightStory] = relation
            }
        }

        partialMap
    }

}

