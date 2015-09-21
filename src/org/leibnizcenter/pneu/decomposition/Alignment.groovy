package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.subsumption.StorySubsumption

@Log4j
class Alignment {

    private void load(Alignment alignment) {
        this.directPartialMap = alignment.directPartialMap
        this.inversePartialMap = alignment.inversePartialMap
    }

    Map<Story, Map<Story, StorySubsumption>> directPartialMap = [:]
    Map<Story, Map<Story, StorySubsumption>> inversePartialMap = [:]

    Alignment(Net left, Net right) {
        // analyse
        Analysis leftAnalysis = Analysis.analyse(left)
        Analysis rightAnalysis = Analysis.analyse(right)

        load(new Alignment(leftAnalysis, rightAnalysis))
    }

    Alignment(Analysis left, Analysis right) {

        // decompose in partial stories
        AnalysisSESEDecomposer leftDecomposition = new AnalysisSESEDecomposer()
        StoryTree leftTree = leftDecomposition.decompose(left)

        AnalysisSESEDecomposer rightDecomposition = new AnalysisSESEDecomposer()
        StoryTree rightTree = rightDecomposition.decompose(right)

        load(new Alignment(leftTree, rightTree))
    }

    Alignment(StoryTree left, StoryTree right) {

        // look for beginning/end subsumed
        List<Story> leftStories = left.flatten()
        List<Story> rightStories = right.flatten()

        load(new Alignment(leftStories, rightStories))
    }

    Alignment(List<Story> leftStories, List<Story> rightStories) {
        for (leftStory in leftStories) {
            for (rightStory in rightStories) {
                StorySubsumption directSubsumption = new StorySubsumption(leftStory, rightStory)
                StorySubsumption inverseSubsumption = new StorySubsumption(rightStory, leftStory)

                if (directSubsumption.type() != StorySubsumption.Type.NONE) {
                    if (!directPartialMap.containsKey(leftStory)) {
                        directPartialMap[leftStory] = [:]
                    }
                    directPartialMap[leftStory][rightStory] = directSubsumption
                }

                if (inverseSubsumption.type() != StorySubsumption.Type.NONE) {
                    if (!inversePartialMap.containsKey(leftStory)) {
                        inversePartialMap[leftStory] = [:]
                    }
                    inversePartialMap[leftStory][rightStory] = inverseSubsumption
                }
            }
        }
    }

    private static String partialMapToString(Map<Story, Map<Story, StorySubsumption>> partialMap) {
        String output = ""
        for (left in partialMap.keySet()) {
            for (right in partialMap[left].keySet()) {
                output += partialMap[left][right].toLog()+"\n"
            }
        }
        output
    }

    String toLog() {
        String output = ""
        output += "Direct ---------------------\n"
        output += partialMapToString(directPartialMap) + "\n"
        output += "Inverse --------------------\n"
        output += partialMapToString(inversePartialMap) + "\n"
        output
    }

}

