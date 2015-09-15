

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

    static StoryTree addAltStory(Story story, StoryTree currentStoryTree) {
        if (currentStoryTree.type == StoryTreeType.SEQ) {
            throw new RuntimeException("You should not be here")
        } else {
            currentStoryTree.addStory(story)
        }
        currentStoryTree
    }

    static StoryTree addSeqStory(Story story, StoryTree currentStoryTree) {
        if (currentStoryTree.type == StoryTreeType.SEQ) {
            currentStoryTree.addStory(story)
        }  else {
            if (currentStoryTree.parent != null) {
                log.trace("but it has a parent: backtrack!")
                currentStoryTree = currentStoryTree.parent
            } else {
                log.trace("and it has not a parent. create it, and backtrack!")
                StoryTree parentStoryTree = new StoryTree(type: StoryTreeType.SEQ)
                parentStoryTree.addLeave(currentStoryTree)
                currentStoryTree = parentStoryTree
            }
            currentStoryTree.addStory(story)
        }
        currentStoryTree
    }

    static StoryTree addSeqStoryBefore(Story story, StoryTree currentStoryTree) {
        if (currentStoryTree.type == StoryTreeType.SEQ) {
            currentStoryTree.addStoryBefore(story)
        } else {
            if (currentStoryTree.parent != null) {
                log.trace("but it has a parent: backtrack!")
                currentStoryTree = currentStoryTree.parent
            } else {
                log.trace("and it has not a parent. create it, and backtrack!")
                StoryTree parentStoryTree = new StoryTree(type: StoryTreeType.SEQ)
                parentStoryTree.addLeave(currentStoryTree)
                currentStoryTree = parentStoryTree
            }
            currentStoryTree.addStoryBefore(story)
        }
        currentStoryTree
    }

    static StoryTree createAltBranch(List<Story> stories, StoryTree currentStoryTree) {
        if (currentStoryTree == null) {
            log.trace("I'm integrating ${stories} in a new story tree starting of type ALT")
            log.trace("initializing story tree with ${stories}")
            currentStoryTree = StoryTree.buildAltStoryTree(stories)
        } else {
            if (currentStoryTree.type != StoryTreeType.SEQ) {
                log.trace("current story tree is not a SEQ")
                if (currentStoryTree.parent != null) {
                    log.trace("but it has a parent: backtrack!")
                    currentStoryTree = currentStoryTree.parent
                } else {
                    log.trace("and it has not a parent. create it, and backtrack!")
                    StoryTree parentStoryTree = new StoryTree(type: StoryTreeType.SEQ)
                    parentStoryTree.addLeave(currentStoryTree)
                    currentStoryTree = parentStoryTree
                }
            } else {
                log.trace("current story tree is a SEQ already")
            }

            StoryTree altStoryTree = StoryTree.buildAltStoryTree(stories)
            log.trace("adding ${altStoryTree} to ${currentStoryTree}")
            currentStoryTree.addLeave(altStoryTree)
        }
        log.trace("currentStoryTree: ${currentStoryTree}")
        currentStoryTree
    }

    // find the position of the second element in common between two stories
    private static Integer getCutPos(Story first, Story second, Integer currentPos = null) {
        if (currentPos == null) {
            currentPos = 0
        }

        log.trace("look for an element in common, with position below limits ${currentPos} for first story, and ${limitSecond} for second story")

        Integer cutPos = null
        for (int i = currentPos; i < first.steps.size() - 1; i++) {
            log.trace("first story step: " + i + ": " + first.steps[i])
            for (int j =  currentPos; j < second.steps.size() - 1; j++) {
                log.trace("second story step: " + j + ": " + second.steps[j])
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
        Integer currentPos = null

        for (int z = stories.size(); z >= 0; z--) {

            log.trace("### ${z} ### currentStoryTree: " + currentStoryTree)

            Story first, second
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
                    if (stories[z - 1].steps[currentPos] != stories[z].steps[currentPos]) {
                        log.trace("the last story and the previous one do not share the same beginning ")
                        return addAltStory(lastStory, currentStoryTree)
                    } else {
                        log.trace("the last story and the previous one share the same beginning ")
                        return addSeqStory(lastStory, currentStoryTree)
                    }
                } else {
                    log.trace("there is only one story, no need to specify the type.")
                    return new StoryTree(story: first)
                }
            }

            second = stories[z - 1]

            log.trace("second story: " + second)

            Story seqStory, altStory1, altStory2
            Integer cutPos = getCutPos(first, second)

            if (cutPos == currentPos) {
                log.trace("the first cut occurs where the current pos is ")

                altStory1 = cutAndSavePartialStory(second, cutPos, -1)
                currentStoryTree = addAltStory(altStory1, currentStoryTree)

            } else if (cutPos < currentPos) {
                log.trace("the first cut occurs before the current pos")

                if (cutPos == second.steps.size() - 1) {
                    seqStory = cutAndSavePartialStory(first, cutPos, currentPos)
                    currentPos = cutPos
                    log.trace("additional research of other cut point")
                    cutPos = getCutPos(first, second, cutPos)
                    altStory1 = cutAndSavePartialStory(first, cutPos, currentPos)
                    altStory2 = cutAndSavePartialStory(second, cutPos, -1)
                    log.trace("attaching alt branch before")
                    currentStoryTree = createAltBranch([altStory1, altStory2], currentStoryTree)
                    log.trace("attaching seq branch after")
                    currentStoryTree = addSeqStory(seqStory, currentStoryTree)
                } else {
                    log.trace("creating alternative branch")
                    altStory1 = cutAndSavePartialStory(first, cutPos, currentPos)
                    altStory2 = cutAndSavePartialStory(second, cutPos, -1)
                    currentStoryTree = createAltBranch([altStory1, altStory2], currentStoryTree)
                }

                currentPos = cutPos

            } else {
                throw new RuntimeException("You should not be here")
            }

        }



        currentStoryTree
    }

}
