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
        log.trace("cutting from $stepi to $stepj")
        Story newStory = new Story()

        if (stepj == -1)
            stepj = source.steps.size() - 1
        if (stepj == null)
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

        log.trace("saving story: " + story)

        State x = story.steps.first()
        State y = story.steps.last()
        if (!partialStoriesBase.containsKey(x)) {
            partialStoriesBase[x] = [:]
        }
        if (!partialStoriesBase[x].containsKey(y)) {
            partialStoriesBase[x][y] = []
        }

        Integer posX, posY, posZ
        posX = partialStoriesBase.keySet().findIndexOf { it == x }
        posY = partialStoriesBase[x].keySet().findIndexOf { it == y }
        posZ = partialStoriesBase[x][y].size()

        story.id = "part_" + x.id + "_" + y.id + "_" + posZ
        partialStoriesBase[x][y] << story

        log.trace("story id: " + story.id)


    }

    private Story cutAndSavePartialStory(Story source, Integer stepi, Integer stepj) {
        if (source.steps.size() == 0)
            throw new RuntimeException("You should not be here")
        Story partialStory = cutPartialStory(source, stepi, stepj)
        save(partialStory)
        partialStory
    }


    // find the position of the second element in common between two stories
    private static Integer getDistinctCutPos(Story first, Story second, Integer currentPos = null) {
        if (currentPos == null) {
            currentPos = 0
        }

        log.trace("look for the first distinct element, with position starting from ${currentPos}")

        Integer cutPos = null
        for (int i = currentPos; i < Math.min(first.steps.size(), second.steps.size()) - 1; i++) {
            log.trace("first story step: " + i + ": " + first.steps[i])
            log.trace("second story step: " + i + ": " + second.steps[i])
            if (second.steps[i] != first.steps[i] || second.eventsPerStep[i][0] != first.eventsPerStep[i][0]) {
                // TODO: multiple events
                log.trace("cut found - i: $i")
                cutPos = i
                break
            }
        }
        cutPos
    }

    // find the position of the second element in common between two stories
    private static Integer getCommonCutPos(Story first, Story second, Integer currentPos = null) {
        if (currentPos == null) {
            currentPos = 0
        }
        log.trace("look for an element in common, with position starting from ${currentPos}")

        Integer cutPos = null
        for (int i = currentPos; i < first.steps.size() - 1; i++) {
            log.trace("first story, step $i: " + i + ": " + first.steps[i])
            for (int j =  currentPos; j < second.steps.size() - 1; j++) {
                log.trace("second story, step $j: " + j + ": " + second.steps[j])

                if (i == currentPos && j == currentPos) {
                  if (first.steps[i] != second.steps[j]) {
                      throw new RuntimeException("You should not be here")
                  }
                }
                if (second.steps[j] == first.steps[i]) {
                    if (i != currentPos || j != currentPos) {
                        log.trace("cut found - i: $i")
                        cutPos = i
                        break
                    } else {
                        log.trace("they share the first element: this is not valid")
                    }
                }
            }
            if (cutPos != null) break
        }
        cutPos
        cutPos
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
        StoryTree currentStoryTree = new StoryTree()
        Integer currentPos = null

        for (int z = stories.size() - 1; z >= 0; z--) {

            log.trace("### ${z} ### currentStoryTree: " + currentStoryTree)

            Story first, second
            Story seqStory, altStory

            first = stories[z]

            log.trace("first story: " + first)

            if (currentPos == null) {
                currentPos = 0
                log.trace("initializing current pos to " + currentPos)
            } else {
                log.trace("current pos: " + currentPos)
            }

            if (z == 0) {
                log.trace("no more stories available, attaching this story to the current decomposition.")

                Story lastStory = cutAndSavePartialStory(first, currentPos, -1)
                if (stories.size() > 1) {
                    log.trace("considering the previous story and creating the ALT ")
                    currentStoryTree.addAltStory(lastStory)
                    return currentStoryTree
                } else {
                    log.trace("there is only one story, no need to specify the type.")
                    return new StoryTree(story: first)
                }
            }

            second = stories[z - 1]

            log.trace("second story: " + second)

            Integer cutDistinctPos = getDistinctCutPos(first, second, currentPos)

            if (cutDistinctPos == currentPos) {
                log.trace("the distinction cut ($cutDistinctPos) occurs where the current pos is ($currentPos)")

                altStory = cutAndSavePartialStory(first, cutDistinctPos, -1)
                currentStoryTree.addAltStory(altStory)

            } else if (cutDistinctPos > currentPos) {
                log.trace("the distinction cut ($cutDistinctPos) occurs after the current pos ($currentPos)")

                seqStory = cutAndSavePartialStory(first, currentPos, cutDistinctPos)
                currentStoryTree.addSeqStory(seqStory)

                Integer cutCommonPos = getCommonCutPos(first, second, cutDistinctPos)
                if (cutCommonPos != null) {
                    log.trace("a common cut ($cutCommonPos) exists")

                    altStory = cutAndSavePartialStory(first, cutDistinctPos, cutCommonPos)
                    currentStoryTree.addAltStory(altStory)

                }


//                if (cutPos == second.steps.size() - 1) {
//                    seqStory = cutAndSavePartialStory(first, cutPos, currentPos)
//                    currentPos = cutPos
//                    log.trace("additional research of other cut point")
//                    cutPos = getCutPos(first, second, cutPos)
//                    altStory1 = cutAndSavePartialStory(first, cutPos, currentPos)
//                    altStory2 = cutAndSavePartialStory(second, cutPos, -1)
//                    log.trace("attaching alt branch before")
//                    currentStoryTree = createAltBranch([altStory1, altStory2], currentStoryTree)
//                    log.trace("attaching seq branch after")
//                    currentStoryTree = createSeqBranch(seqStory, currentStoryTree)
//                } else {
//                    log.trace("creating alternative branch")
//                    altStory1 = cutAndSavePartialStory(first, cutPos, currentPos)
//                    altStory2 = cutAndSavePartialStory(second, cutPos, -1)
//                    currentStoryTree = createAltBranch([altStory1, altStory2], currentStoryTree)
//                }

                currentPos = cutDistinctPos

            } else {
                throw new RuntimeException("You should not be here")
            }

        }



        currentStoryTree
    }

}
