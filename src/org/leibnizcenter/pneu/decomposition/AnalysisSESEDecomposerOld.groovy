//package org.leibnizcenter.pneu.decomposition
//
//import groovy.util.logging.Log4j
//import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
//import org.leibnizcenter.pneu.animation.monolithic.analysis.State
//import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
//
//
//@Log4j
//class AnalysisSESEDecomposer {
//
//    Map<State, Map<State, List<Story>>> partialStoriesBase = [:]
//
//    // cut a story from state stepi to state stepj, including transition events
//    static private Story cutPartialStory(Story source, Integer stepi, Integer stepj) {
//
//        log.trace("source story: " + source)
//        log.trace("cutting from $stepi to $stepj")
//        Story newStory = new Story()
//
//        if (stepj == -1)
//            stepj = source.steps.size() - 1
//        if (stepj == null)
//            stepj = source.steps.size() - 1
//
//        for (int z = stepi; z <= stepj; z++) {
//            newStory.addStep(source.steps[z])
//            if (z < source.steps.size() - 1) {
//                if (source.eventsPerStep[z])
//                    newStory.addEvents(source.eventsPerStep[z])
//                else {
//                    throw new RuntimeException("Not well formed story")
//                }
//            }
//        }
//
//        log.trace("cut story: " + newStory)
//        newStory
//    }
//
//    private void save(Story story) {
//        if (story.steps.size() == 0) throw new RuntimeException("You should not be here")
//
//        log.trace("saving story: "+story)
//
//        State x = story.steps.first()
//        State y = story.steps.last()
//        if (!partialStoriesBase.containsKey(x)) {
//            partialStoriesBase[x] = [:]
//        }
//        if (!partialStoriesBase[x].containsKey(y)) {
//            partialStoriesBase[x][y] = []
//        }
//
//        Integer posX, posY, posZ
//        posX = partialStoriesBase.keySet().findIndexOf {it == x}
//        posY = partialStoriesBase[x].keySet().findIndexOf {it == y}
//        posZ = partialStoriesBase[x][y].size()
//
//        story.id = "part_"+x.id+"_"+y.id+"_"+posZ
//        partialStoriesBase[x][y] << story
//
//        log.trace("story id: "+story.id)
//
//
//    }
//
//    private Story cutAndSavePartialStory(Story source, Integer stepi, Integer stepj) {
//        if (source.steps.size() == 0)
//            throw new RuntimeException("You should not be here")
//        Story partialStory = cutPartialStory(source, stepi, stepj)
//        save(partialStory)
//        partialStory
//    }
//
//
//    static StoryTree integrateStoryInStoryTree(Story story, StoryTree currentStoryTree, StoryTreeType type = null) {
//        integrateStoriesInStoryTree([story], currentStoryTree, type)
//    }
//
//    static StoryTree integrateStoriesInStoryTree(List<Story> stories, StoryTree currentStoryTree, StoryTreeType type = null) {
//
//        if (currentStoryTree == null) {
//            log.trace("I'm integrating ${stories} in a new story tree starting from a type $type")
//            log.trace("initializing story tree")
//            currentStoryTree = new StoryTree(type: type)
//            currentStoryTree.addStories(stories)
//        } else {
//            log.trace("I'm integrating ${stories} in $currentStoryTree in a relation of type $type")
//            if (currentStoryTree.type == type) {
//                log.trace("current story tree type is the same of the one desired")
//
//                List<StoryTree> leaves = []
//                for (story in stories) {
//                    leaves << new StoryTree(story: story)
//                }
//                currentStoryTree.addLeavesFromTreesBefore(leaves)
//
//            } else if (type != null) {
//                log.trace("current story tree type is different from the one desired ($type). create an encapsulating story tree.")
//                StoryTree parentStoryTree = new StoryTree(type: type)
//                parentStoryTree.addStories(stories)
//                parentStoryTree.addStory(currentStoryTree)
//                currentStoryTree = parentStoryTree
//            } else {
//                log.trace("no desired type. adding to the current content.")
//                currentStoryTree.addStories(stories)
//            }
//        }
//        currentStoryTree
//    }
//
//    static StoryTree integrateStoryTreesInStoryTree(List<StoryTree> storyTrees, StoryTree currentStoryTree, StoryTreeType type = null) {
//
//        if (currentStoryTree == null) {
//            log.trace("I'm integrating ${storyTrees} in a new story tree starting from a type $type")
//            log.trace("initializing story tree")
//            currentStoryTree = new StoryTree(type: type)
//            currentStoryTree.addLeavesFromTrees(storyTrees)
//        } else {
//            log.trace("I'm integrating ${storyTrees} in $currentStoryTree in a relation of type $type")
//            if (currentStoryTree.type == type) {
//                log.trace("current story tree type is the same of the one desired")
//                currentStoryTree.addLeavesFromTreesBefore(storyTrees)
//
//            } else if (type != null) {
//                log.trace("current story tree type is different from the one desired ($type). create an encapsulating story tree.")
//                StoryTree parentStoryTree = new StoryTree(type: type)
//                parentStoryTree.addLeavesFromTrees(storyTrees)
//                parentStoryTree.addStory(currentStoryTree)
//                currentStoryTree = parentStoryTree
//            } else {
//                log.trace("no desired type. adding to the current content.")
//                currentStoryTree.addLeavesFromTrees(storyTrees)
//            }
//        }
//        currentStoryTree
//    }
//
//    // find the position of the first element in common between two stories
//    private static Integer getFirstCutPos(Story first, Story second) {
//        log.trace("look for the first cut position")
//
//        Story longer, shorter
//
//        if (first.steps.size() >= second.steps.size()) {
//            log.trace("the first story is longer/equal than the second one.")
//            longer = first
//            shorter = second
//        } else {
//            log.trace("the second story is longer than the first one.")
//            longer = second
//            shorter = first
//        }
//
//        Integer firstCutPos = null
//        if (longer.steps.last() == shorter.steps.last()) {
//            log.trace("the two stories share the last element")
//            firstCutPos = getCutPos(first, second)
//        } else {
//            log.trace("the two stories don't share the last element")
//            if (longer.steps.size() != shorter.steps.size()) {
//                log.trace("the two stories don't have the same size")
//                for (int i = longer.steps.size() - 2; i >= 0; i--) {
//                    log.trace("longer story step: " + i + ": " + longer.steps[i])
//                    if (shorter.steps.last() == longer.steps[i]) {
//                        log.trace("first cut found - i: $i")
//                        firstCutPos = i
//                        break
//                    }
//                }
//            } else {
//                firstCutPos = null
//            }
//        }
//        firstCutPos
//    }
//
//    // find the position of the second element in common between two stories
//    private static Integer getCutPos(Story first, Story second, Integer limitFirst = null, Integer limitSecond = null) {
//        if (limitFirst == null) {
//            limitFirst = first.steps.size()
//        } else {
//            limitFirst++
//        }
//        if (limitSecond == null) {
//            limitSecond = second.steps.size()
//        } else {
//            limitSecond++
//        }
//
//        log.trace("look for a second element in common position below limits ${limitFirst} for first story, and ${limitSecond} for second story")
//
//        Integer cutPos = null
//        for (int i = limitFirst - 1; i >= 0; i--) {
//            log.trace("first story step: " + i + ": " + first.steps[i])
//            for (int j = limitSecond - 1; j >= 0; j--) {
//                log.trace("second story step: " + j + ": " + second.steps[j])
//                if (second.steps[j] == first.steps[i]) {
//                    if (i != limitFirst - 1 && j != limitSecond - 1) {
//                        log.trace("cut found - i: $i")
//                        cutPos = i
//                        break
//                    } else {
//                        log.trace("they share the last element: this is not valid")
//                    }
//                }
//            }
//            if (cutPos != null) break
//        }
//        cutPos
//    }
//
//    // single entry single exit tree decomposer`
//    // decompose the result of depth execution of petri net
//    // a list of sequences of state and transition event
//    // partially overlapping (see Analysis class)
//    StoryTree decompose(Analysis analysis) {
//
//        // those stories have been discovered through depth-first search
//        // therefore they share a part in common, at least the root
//        List<Story> stories = analysis.storyBase.base
//
//        // store the current analysis
//        StoryTree currentStoryTree = null
//        Integer currentPos = null
//
//        for (int z = 0; z < stories.size(); z++) {
//
//            log.trace("### ${z} ### currentStoryTree: "+currentStoryTree)
//
//            Story first, second
//            first = stories[z]
//            second = stories[z + 1]
//
//            log.trace("first story: " + first)
//
//            if (currentPos == null) {
//                currentPos = first.steps.size() - 1
//                log.trace("initializing current pos to "+currentPos)
//            } else {
//                log.trace("current pos: "+currentPos)
//            }
//
//            if (second == null) {
//                log.trace("no more stories available, attaching this story to the current decomposition.")
//
//                Story lastStory = cutAndSavePartialStory(first, 0, currentPos)
//                if (z > 0) {
//                    if (stories[z-1].steps[0] != stories[z].steps[0]) {
//                        log.trace("the last story and the previous one do not share the same beginning ")
//                        return integrateStoryInStoryTree(lastStory, currentStoryTree, StoryTreeType.ALT)
//                    } else {
//                        log.trace("the last story and the previous one share the same beginning ")
//                        return integrateStoryInStoryTree(lastStory, currentStoryTree, StoryTreeType.SEQ)
//                    }
//                } else {
//                    return integrateStoryInStoryTree(lastStory, currentStoryTree)
//                }
//            }
//
//            log.trace("second story: " + second)
//            log.trace("second story last step: " + ": " + second.steps.last())
//
//            Integer firstCutPos, secondCutPos
//
//            firstCutPos = getFirstCutPos(first, second)
//            log.trace("first cut position: " + firstCutPos)
//
//            Story seqStory, altStory1, altStory2
//            if (first.steps.last() == second.steps.last()) {
//                log.trace("the two stories share the last element")
//
//                altStory1 = cutAndSavePartialStory(first, firstCutPos, -1)
//                altStory2 = cutAndSavePartialStory(second, firstCutPos, -1)
//
//                currentStoryTree = integrateStoriesInStoryTree([altStory1, altStory2], currentStoryTree, StoryTreeType.ALT)
//
//                currentPos = firstCutPos
//            } else {
//                log.trace("the two stories don't share the last element")
//
//                secondCutPos = getCutPos(first, second, firstCutPos)
//                if (secondCutPos == null) throw new RuntimeException("You shouldn't be here.")
//                log.trace("second cut position: " + secondCutPos)
//
//                altStory1 = cutAndSavePartialStory(first, secondCutPos, firstCutPos)
//                altStory2 = cutAndSavePartialStory(second, secondCutPos, -1)
//
//                if (firstCutPos != null) {
//                    log.trace("one story is an internal variation of the other")
//                    if (first.steps.size() >= second.steps.size()) {
//                        log.trace("current pos ($currentPos) is longer than the size of the second")
//                        seqStory = cutAndSavePartialStory(first, firstCutPos, -1)
//                    }
//                    else {
//                        log.trace("the second story is longer than current pos")
//                        seqStory = cutAndSavePartialStory(second, firstCutPos, -1)
//                    }
//
//                    StoryTree seqStoryTree = new StoryTree(story: seqStory)
//                    StoryTree altStoryTree = StoryTree.buildAltStoryTree([altStory1, altStory2])
//                    currentStoryTree = integrateStoryTreesInStoryTree([altStoryTree, seqStoryTree], currentStoryTree, StoryTreeType.SEQ)
//                } else {
//                    currentStoryTree = integrateStoriesInStoryTree([altStory1, altStory2], currentStoryTree, StoryTreeType.ALT)
//                }
//
//                currentPos = secondCutPos
//            }
//
//        }
//
//        currentStoryTree
//    }
//
//}
