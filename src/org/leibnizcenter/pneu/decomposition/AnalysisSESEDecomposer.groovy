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

    // single entry single exit tree decomposer`
    // decompose the result of depth execution of petri net
    // a list of sequences of state and transition event
    // partially overlapping (see Analysis class)
    StoryTree decompose(Analysis analysis, Story topic = null) {

        StoryTree decomposition = new StoryTree(story: topic)

        // those stories have been discovered through depth-first search
        // therefore they share a part in common, at least the root
        List<Story> stories = analysis.storyBase.base

        // store the current analysis
        StoryTree currentStoryTree = null

        for (int z = 0; z < stories.size(); z++) {

            log.trace("currentStoryTree: "+currentStoryTree)

            Story first, second
            first = stories[z]
            second = stories[z + 1]

            log.trace("first story: " + first)

            if (second == null) {
                log.trace("no more stories available, attaching this story to the current decomposition.")
                decomposition.addLeave(first)
                return decomposition
            }

            log.trace("second story: " + second)
            log.trace("second story last step: " + ": " + second.steps.last())

            Integer firstCutPos, secondCutPos

            Integer i

            if (first.steps.size() >= second.steps.size()) {
                log.trace("the first story is longer/equal than the second one.")
                for (i = first.steps.size() - 1; i >= second.steps.size() - 1; i--) {
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
                    log.trace("and they do not conclude in the same state (${firstCutPos}, ${first.steps.size()}).")
                    Story seqStory = cutAndSavePartialStory(first, firstCutPos, -1)
                    StoryTree seqStoryTree = StoryTree.createSeqStoryTree([seqStory])
                    if (currentStoryTree == null) {
                        log.trace("initializating the decomposition tree with a seq")
                        decomposition.addLeave(seqStoryTree)
                    }
                    currentStoryTree = seqStoryTree
                } else {
                    log.trace("but they conclude in the same state.")
                    firstCutPos = -1
                }
                log.trace("first cut position: " + i)

            } else {
                log.trace("the two stories do not share the final part")
                i = first.steps.size()
                firstCutPos = -1
                log.trace("first cut position: " + firstCutPos)
            }

            Integer j
            for (i = i - 1; i >= 0 && (secondCutPos == null); i--) {
                log.trace("first story step: " + i + ": " + first.steps[i])
                for (j = second.steps.size() - 1; j >= 0; j--) {
                    log.trace("second story step: " + j + ": " + second.steps[j])
                    if (second.steps[j] == first.steps[i]) {
                        log.trace("cut found - i: $i, j: $j")
                        secondCutPos = j
                        break
                    }
                }
            }

            if (secondCutPos == null) throw new RuntimeException("You shouldn't be here.")

            log.trace("second cut position: " + secondCutPos)

            Story altStory1 = cutAndSavePartialStory(first, secondCutPos, firstCutPos)
            Story altStory2 = cutAndSavePartialStory(second, secondCutPos, -1)

            // modify after the cut the second story, to avoid to save two times the same
            stories[z + 1] = cutPartialStory(second, 0, secondCutPos)

            // if the current tree has been initialized and
            // we are already in a series of alternative branches, just add the last one
            if (currentStoryTree != null && currentStoryTree.type == StoryTreeType.ALT && firstCutPos != -1) {
//                if (currentStoryTree.leaves.last() != altStory1)
//                    throw new RuntimeException("These stories should be the same: "+currentStoryTree.leaves.last()+" vs. "+altStory1)

                log.trace("add the last alternative branch to the alt")
                currentStoryTree.addLeave(altStory2)
            } else {

                StoryTree altStoryTree = StoryTree.buildAltStoryTree([altStory1, altStory2])
                if (currentStoryTree == null) {
                    log.trace("initializating the decomposition tree with an alt")
                    decomposition.addLeave(altStoryTree)
                } else {
                    log.trace("add the alt tree within the seq")

                    if (firstCutPos == -1) {
                        currentStoryTree = currentStoryTree.parent
                    }
                    if (currentStoryTree.type != StoryTreeType.SEQ) {
                        currentStoryTree.type = StoryTreeType.SEQ
                    }
                    currentStoryTree.addStoryTreeBefore(altStoryTree)
                }

                currentStoryTree = altStoryTree
            }

            // last part
            if (z == stories.size() - 2) {
                Story seqStory = cutAndSavePartialStory(second, 0, secondCutPos)
                if (currentStoryTree.type == StoryTreeType.SEQ) {
                    log.trace("current story tree type is already seq")
                } else if (currentStoryTree.type == StoryTreeType.ALT) {
                    log.trace("current story tree type is alt. backtracking to the parent")
                    currentStoryTree = currentStoryTree.parent
                }
                currentStoryTree.addStoryBefore(seqStory)
                break
            }

        }

        decomposition
    }

}
