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

        log.trace("look for the first distinct element, with position starting from ${currentPos} in \n $first and \n $second")

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

    // returns the positions of the second element in common between two stories
    private static List<Integer> getCommonCutPos(Story first, Story second, Integer currentPos = null) {
        if (currentPos == null) {
            currentPos = 0
        }
        log.trace("look for an element in common, with position starting from ${currentPos}")

        Integer cutFirstPos = null, cutSecondPos = null
        for (int i = currentPos; i < first.steps.size(); i++) {
            log.trace("first story, step $i: " + i + ": " + first.steps[i])
            for (int j = currentPos; j < second.steps.size(); j++) {
                log.trace("second story, step $j: " + j + ": " + second.steps[j])

                if (i == currentPos && j == currentPos) {
                    if (first.steps[i] != second.steps[j]) {
                        throw new RuntimeException("You should not be here")
                    }
                }
                if (second.steps[j] == first.steps[i]) {
                    if (i != currentPos || j != currentPos) {
                        log.trace("common cut found - i: $i")
                        cutFirstPos = i
                        cutSecondPos = j
                        break
                    } else {
                        log.trace("they share the first element: this is not valid")
                    }
                }
            }
            if (cutFirstPos != null) break
        }
        log.trace("common cut found: $cutFirstPos, $cutSecondPos")
        return [cutFirstPos, cutSecondPos]
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
        Integer currentPos = null, nextCutPos

        for (int z = stories.size() - 1; z >= 0; z--) {

            log.trace("### ${z} ################# root: " + currentStoryTree.root())
            log.trace("### ${z} ### current story tree: " + currentStoryTree)

            Story first, second
            Story seqStory, altStory

            first = stories[z]

            if (currentPos == null) {
                currentPos = 0
                log.trace("initializing current pos to " + currentPos)
            } else {
                log.trace("current pos: " + currentPos)
            }

            log.trace("first story: " + first)

            if (nextCutPos != null) {
                log.trace("I already now I have to cut at ${nextCutPos}")
                altStory = cutAndSavePartialStory(first, currentPos, nextCutPos)
                currentStoryTree = currentStoryTree.addAltStory(altStory)
                log.trace("backtracking to $currentStoryTree.parent")
                currentStoryTree = currentStoryTree.parent
                currentPos = nextCutPos
                log.trace("new current pos: ${currentPos}")
            }

            if (z == 0) {
                log.trace("no more stories available, attaching this story to the current decomposition.")

                if (stories.size() > 1) {
                    log.trace("checking if there is a remaining part (${first.steps.size() - 1} vs $currentPos)")
                    if (first.steps.size() - 1 != currentPos) {
                        log.trace("adding the remaining part")
                        Story lastStory = cutAndSavePartialStory(first, currentPos, -1)
                        if (nextCutPos != null) {
                            log.trace("as SEQ on ${currentStoryTree}")
                            currentStoryTree = currentStoryTree.addSeqStory(lastStory)
                        } else {
                            log.trace("as ALT on ${currentStoryTree}")
                            currentStoryTree = currentStoryTree.addAltStory(lastStory)
                        }
                    }
                } else {
                    log.trace("there is only one story, no need to specify the type.")
                    currentStoryTree = new StoryTree(story: first)
                }
            } else {

                nextCutPos = null
                second = stories[z - 1]

                log.trace("second story: " + second)

                log.trace("current pos: " + currentPos)

                Integer cutDistinctPos = getDistinctCutPos(first, second)

                if (cutDistinctPos < currentPos) {
                    log.trace("the current branch is presumably finished")
                    log.trace("checking if there is a remaining part (${first.steps.size() - 1} vs $currentPos)")
                    if (first.steps.size() - 1 != currentPos) {
                        log.trace("adding the remaining part")
                        Story lastStory = cutAndSavePartialStory(first, currentPos, -1)
                        log.trace("as SEQ on ${currentStoryTree}")
                        currentStoryTree = currentStoryTree.addSeqStory(lastStory)
//                        } else {
//                            log.trace("as ALT on ${currentStoryTree}")
//                            currentStoryTree = currentStoryTree.addAltStory(lastStory)
//                        }
                    }

                    State linkState = stories[z].steps[cutDistinctPos]
                    log.trace("I have to backtrack as to find the node " + linkState)

                    List<StoryTree> leaves = []
                    for (int i = currentStoryTree.leaves.size() - 1; i >= 0; i--) {
                        StoryTree leave = currentStoryTree.leaves[i]
                        leaves << leave
                        log.trace("check with state: "+leave.getFirstStep())
                        if (linkState == leave.getFirstStep()) {
                            log.trace("I have found it here!" + leave)
                            log.trace("create new alt branch in its place")

                            StoryTree altStoryTree = new StoryTree(type: StoryTreeType.ALT, id: leave.id)
                            if (leaves.size() == 1) {
                                altStoryTree.addLeave(leave)
                            } else {
                                StoryTree seqStoryTree = new StoryTree(type: StoryTreeType.SEQ)
                                seqStoryTree.addLeaves(leaves.reverse())
                                altStoryTree.addLeave(seqStoryTree)
                            }
                            currentStoryTree.leaves[i] = altStoryTree
                            altStoryTree.parent = currentStoryTree
                            currentStoryTree = altStoryTree
                            break
                        } else {
                            currentStoryTree.leaves.remove(i)
                        }
                    }
                    currentPos = cutDistinctPos

                } else if (cutDistinctPos == currentPos) {
                    log.trace("the distinction cut ($cutDistinctPos) occurs where the current pos is ($currentPos)")

                    altStory = cutAndSavePartialStory(first, cutDistinctPos, -1)
                    currentStoryTree = currentStoryTree.addAltStory(altStory)

                } else if (cutDistinctPos > currentPos) {
                    log.trace("the distinction cut ($cutDistinctPos) occurs after the current pos ($currentPos)")

                    seqStory = cutAndSavePartialStory(first, currentPos, cutDistinctPos)
                    log.trace("story presumably to be added as common part (SEQ) " + seqStory)

                    Story lastStory = currentStoryTree.getLastStory()
                    log.trace("last story added" + lastStory)

                    currentStoryTree = currentStoryTree.addSeqStory(seqStory)

                    Integer cutCommonPos
                    (cutCommonPos, nextCutPos) = getCommonCutPos(first, second, cutDistinctPos)

                    if (cutCommonPos != null) {
                        log.trace("a common cut ($cutCommonPos, $nextCutPos) exists")
                        altStory = cutAndSavePartialStory(first, cutDistinctPos, cutCommonPos)
                        currentStoryTree = currentStoryTree.addAltStory(altStory)
                    } else {
                        log.trace("a common cut does not exist")
                        altStory = cutAndSavePartialStory(first, cutDistinctPos, -1)
                        currentStoryTree = currentStoryTree.addAltStory(altStory)
                    }

                    log.trace("bringing current pos from $currentPos to $cutDistinctPos")
                    currentPos = cutDistinctPos

                }

            }

        }

        // return to the root of the tree
        currentStoryTree.root()
    }

//    static Analysis innerCompose(StoryTree tree) {
//        Analysis analysis = new Analysis()
//        analysis.storyBase = []
//
//        log.trace(tree.id + " || compose ${tree}")
//
//        if (tree.story) {
//            analysis.storyBase.addStory(tree.story.minimalClone())
//        } else {
//            if (tree.type == StoryTreeType.ALT) {
//                for (leave in tree.leaves) {
//                    Analysis innerAnalysis = innerCompose(leave)
//                    for (int i = 0; i < innerAnalysis.storyBase.base.size(); i++) {
//                        Story newStory = innerAnalysis.storyBase.base[i]
//                        log.trace(tree.id + " || adding story in ALT: "+newStory)
//                        analysis.storyBase.addStory(newStory)
//                    }
//                }
//            } else if (tree.type == StoryTreeType.SEQ) {
//                Story currentStory = null, newCurrentStory = null
//                for (leave in tree.leaves) {
//                    log.trace(tree.id + " || current story in SEQ: "+currentStory)
//                    Analysis innerAnalysis = innerCompose(leave)
//                    for (int i = 0; i < innerAnalysis.storyBase.base.size(); i++) {
//                        newCurrentStory = Story.append(currentStory, innerAnalysis.storyBase.base[i])
//                    }
//                    currentStory = newCurrentStory
//                }
//                analysis.storyBase.addStory(currentStory)
//            }
//        }
//
//        log.trace(tree.id + " || result analysis: "+analysis.storyBase.toLog())
//
//        analysis
//    }
//
//    static Analysis compose(StoryTree tree) {
//
//        Analysis analysis = new Analysis()
//        analysis.storyBase = []
//
//        analysis = innerCompose(tree)
//        analysis.storyBase.base = analysis.storyBase.base.reverse()
//
//        analysis
//    }

}
