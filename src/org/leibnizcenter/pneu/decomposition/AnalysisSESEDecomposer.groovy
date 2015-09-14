package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story


@Log4j
class AnalysisSESEDecomposer {

    Map<State, Map<State, List<Story>>> partialStoriesBase = [:]

    // cut a story from state stepi to state stepj, including transition events
    static private Story cutPartialStory(Story source, Integer stepi, Integer stepj) {

        log.trace("source story: " + source)
        Story newStory = new Story()

        if (stepj == -1)
            stepj = source.steps.size() - 1

        for (int z = stepi; z <= stepj; z++) {
            newStory.addStep(source.steps[z])
            if (z < source.steps.size() - 1) {
                if (source.eventsPerStep[z])
                    newStory.addEvents(source.eventsPerStep[z])
                else {
                    throw new RuntimeException("Not well formed story")
                }
            }
        }

        log.trace("cut story: " + newStory)
        newStory
    }

    private void save(Story story) {
        if (story.steps.size() == 0) throw new RuntimeException("You should not be here")

        log.trace("saving story: "+story)

        State x = story.steps.first()
        State y = story.steps.last()
        if (!partialStoriesBase.containsKey(x)) {
            partialStoriesBase[x] = [:]
        }
        if (!partialStoriesBase[x].containsKey(y)) {
            partialStoriesBase[x][y] = []
        }

        Integer posX, posY, posZ
        posX = partialStoriesBase.keySet().findIndexOf {it == x}
        posY = partialStoriesBase[x].keySet().findIndexOf {it == y}
        posZ = partialStoriesBase[x][y].size()

        story.id = "part_"+x.id+"_"+y.id+"_"+posZ
        partialStoriesBase[x][y] << story

        log.trace("story id: "+story.id)


    }

    private Story cutAndSavePartialStory(Story source, Integer stepi, Integer stepj) {
        if (source.steps.size() == 0)
            throw new RuntimeException("You should not be here")
        Story partialStory = cutPartialStory(source, stepi, stepj)
        save(partialStory)
        partialStory
    }


    static StoryTree integrateStoryInStoryTree(Story story, StoryTree currentStoryTree, StoryTreeType type = null) {
        integrateStoriesInStoryTree([story], currentStoryTree, type)
    }

    static StoryTree integrateStoriesInStoryTree(List<Story> stories, StoryTree currentStoryTree, StoryTreeType type = null) {

        if (currentStoryTree == null) {
            log.trace("I'm integrating ${stories} in a new story tree starting from a type $type")
            log.trace("initializing story tree")
            currentStoryTree = new StoryTree(type: type)
            currentStoryTree.addLeaves(stories)
        }
        else {
            log.trace("I'm integrating ${stories} in $currentStoryTree in a relation of type $type")
            if (currentStoryTree.type == type) {
                log.trace("current story tree type is the same of the one desired")
            } else if (type != null) {
                log.trace("current story tree type is different from the one desired ($type). create an encapsulating story tree.")
                StoryTree parentStoryTree = new StoryTree(type: type)
                currentStoryTree.parent = parentStoryTree
                currentStoryTree = currentStoryTree.parent
            } else {
                log.trace("no desired type. adding to the current content.")
                currentStoryTree.addLeaves(stories)
            }
        }
        currentStoryTree
    }

    // single entry single exit tree decomposer`
    // decompose the result of depth execution of petri net
    // a list of sequences of state and transition event
    // partially overlapping (see Analysis class)
    StoryTree decompose(Analysis analysis) {

        // those stories have been discovered through depth-first search
        // therefore they share a part in common, at least the root
        List<Story> stories = analysis.storyBase.base

        // store the current analysis
        StoryTree currentStoryTree = null
        Integer currentPos

        for (int z = 0; z < stories.size(); z++) {

            log.trace("currentStoryTree: "+currentStoryTree)

            Story first, second
            first = stories[z]
            second = stories[z + 1]

            log.trace("first story: " + first)

            if (currentPos == null) {
                currentPos = first.steps.size() - 1
                log.trace("initializing current pos to "+currentPos)
            } else {
                log.trace("current pos: "+currentPos)
            }

            if (second == null) {
                log.trace("no more stories available, attaching this story to the current decomposition.")
                Story lastStory = cutAndSavePartialStory(first, 0, currentPos)
                return integrateStoryInStoryTree(lastStory, currentStoryTree)
            }

            log.trace("second story: " + second)
            log.trace("second story last step: " + ": " + second.steps.last())

            Integer firstCutPos, secondCutPos

            Integer i
            if (currentPos >= second.steps.size()) {
                log.trace("the first part is longer/equal than the second one.")
                for (i = currentPos - 1; i >= second.steps.size() - 1; i--) {
                    log.trace("first story step: " + i + ": " + first.steps[i])
                    if (second.steps.last() == first.steps[i]) {
                        log.trace("first cut found - i: $i")
                        firstCutPos = i
                        break
                    }
                }
            }

            if (firstCutPos != null) {
                log.trace("the second story provides an internal variation to the first")
                if (firstCutPos < first.steps.size() - 1) {
                    log.trace("and they do not conclude in the same state (cutting from ${firstCutPos} to ${first.steps.size()}).")
                    Story seqStory = cutAndSavePartialStory(first, firstCutPos, -1)
                    currentStoryTree = integrateStoryInStoryTree(seqStory, currentStoryTree, StoryTreeType.SEQ)
                } else {
                    log.trace("but they conclude in the same state.")
                    firstCutPos = -1
                }
            } else {
                log.trace("the two stories do not share the final part")

                if (first.steps.last() == second.steps.last()) {
                    log.trace("although they conclude in the same state.")
                    i = first.steps.size() - 1
                } else {
                    i = first.steps.size()
                }
                firstCutPos = -1
            }

            log.trace("first story cut position: " + firstCutPos)

            if (i == null)
                throw new RuntimeException("You should not be here")

            Integer j
            for (i = i - 1; i >= 0 && (secondCutPos == null); i--) {
                log.trace("first story step: " + i + ": " + first.steps[i])
                for (j = second.steps.size() - 1; j >= 0; j--) {
                    log.trace("second story step: " + j + ": " + second.steps[j])
                    if (second.steps[j] == first.steps[i]) {
                        log.trace("cut found - $i for first story, $j for second story")
                        secondCutPos = i
                        break
                    }
                }
            }

            if (secondCutPos == null) throw new RuntimeException("You shouldn't be here.")

            log.trace("second cut position: " + secondCutPos)

            Story altStory1 = cutAndSavePartialStory(first, secondCutPos, firstCutPos)
            Story altStory2 = cutAndSavePartialStory(second, secondCutPos, -1)

            currentStoryTree = integrateStoriesInStoryTree([altStory1, altStory2], currentStoryTree, StoryTreeType.ALT)
            currentPos = secondCutPos
        }

        currentStoryTree
    }

}
