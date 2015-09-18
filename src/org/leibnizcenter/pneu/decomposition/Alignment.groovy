package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.components.petrinet.Net

@Log4j
class Alignment {

    static Map<Story, Map<Story, StorySubsumptionOutcome>> partialAlignmentTest(Net left, Net right) {

        // analyse
        Analysis leftAnalysis = Analysis.analyse(left)
        Analysis rightAnalysis = Analysis.analyse(right)

        partialAlignmentTest(leftAnalysis, rightAnalysis)
    }

    static Map<Story, Map<Story, StorySubsumptionOutcome>> partialAlignmentTest(Analysis left, Analysis right) {

        // decompose in partial stories
        AnalysisSESEDecomposer leftDecomposition = new AnalysisSESEDecomposer()
        StoryTree leftTree = leftDecomposition.decompose(left)

        AnalysisSESEDecomposer rightDecomposition = new AnalysisSESEDecomposer()
        StoryTree rightTree = rightDecomposition.decompose(right)

        partialAlignmentTest(leftTree, rightTree)
    }

    static Map<Story, Map<Story, StorySubsumptionOutcome>> partialAlignmentTest(StoryTree left, StoryTree right) {

        // look for beginning/end subsumed
        List<Story> leftStories = left.flatten()
        List<Story> rightStories = right.flatten()

        partialAlignmentTest(leftStories, rightStories)
    }

    static Map<Story, Map<Story, Map<Boolean, StorySubsumptionOutcome>>> partialAlignmentTest(List<Story> leftStories, List<Story> rightStories) {

        Map<Story, Map<Story, Map<Boolean, StorySubsumptionOutcome>>> partialMap = [:]

        for (leftStory in leftStories) {
            for (rightStory in rightStories) {
                StorySubsumptionOutcome gs = Subsumption.subsumes(leftStory, rightStory)
                StorySubsumptionOutcome sg = Subsumption.subsumes(rightStory, leftStory)

                if (gs.type() != StorySubsumptionOutcome.Type.NONE || sg.type() != StorySubsumptionOutcome.Type.NONE) {
                    if (!partialMap.containsKey(leftStory)) {
                        partialMap[leftStory] = [:]
                    }
                    if (!partialMap[leftStory].containsKey(rightStory)) {
                        partialMap[leftStory][rightStory] = [:]
                    }

                    if (gs.type() != StorySubsumptionOutcome.Type.NONE)
                        partialMap[leftStory][rightStory][true] = gs
                    if (sg.type() != StorySubsumptionOutcome.Type.NONE)
                        partialMap[leftStory][rightStory][false] = sg
                }
            }
        }

        partialMap
    }

    static String mapToString(Map<Story, Map<Story, Map<Boolean, StorySubsumptionOutcome>>> alignment) {

        String output = ""
        String relation = ""

        for (left in alignment.keySet()) {
            for (right in alignment[left].keySet()) {

                StorySubsumptionOutcome directOutcome = alignment[left][right][true]
                StorySubsumptionOutcome inverseOutcome = alignment[left][right][false]

                // left subsumes right?
                if (alignment[left][right][true] != null) {

                    relation = "(" + directOutcome.leftGeneralLimit + ", " + directOutcome.rightGeneralLimit + ") <- " + "(" + directOutcome.leftSpecificLimit + ", " + directOutcome.rightSpecificLimit + ")"

                    // right subsumes left as well?
                    if (alignment[left][right][false] != null) {
                        relation += " && (" + inverseOutcome.leftGeneralLimit + ", " + inverseOutcome.rightGeneralLimit + ") -> " + "(" + inverseOutcome.leftSpecificLimit + ", " + inverseOutcome.rightSpecificLimit + ")"

                        if (directOutcome.leftGeneralLimit == inverseOutcome.leftSpecificLimit &&
                                directOutcome.leftSpecificLimit == inverseOutcome.leftGeneralLimit &&
                                directOutcome.rightGeneralLimit == inverseOutcome.rightSpecificLimit &&
                                directOutcome.rightSpecificLimit == inverseOutcome.rightGeneralLimit)
                            relation = "(" + inverseOutcome.leftGeneralLimit + ", " + inverseOutcome.rightGeneralLimit + ") <-> " + "(" + inverseOutcome.leftSpecificLimit + ", " + inverseOutcome.rightSpecificLimit + ")"
                    }

                } else if (alignment[left][right][false] != null) {

                    relation = "(" + inverseOutcome.leftGeneralLimit + ", " + inverseOutcome.rightGeneralLimit + ") -> " + "(" + inverseOutcome.leftSpecificLimit + ", " + inverseOutcome.rightSpecificLimit + ")"


                }

                output += left.toString() + " vs\n" + right.toString() + "\n"+ "= "+relation+"\n\n"
            }
        }

        output
    }

}

