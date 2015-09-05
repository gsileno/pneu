package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story


@Log4j
class SimpleSESEDecomposer {

    // for each story in a storybase after the analysis

    // start from the end

    Map<State, Map<State, List<Story>>> partialStoriesBase = [:]

    static private Story cutPartialStory(Story source, Integer stepi, Integer stepj) {

        log.trace("source story: "+source)
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

        log.trace("cut story: "+newStory)

        newStory
    }

    private void save(Story story) {
        State x = story.steps.first()
        State y = story.steps.last()
        if (!partialStoriesBase.containsKey(x)) {
            partialStoriesBase[x] = [:]
        }
        if (!partialStoriesBase[x].containsKey(y)) {
            partialStoriesBase[x][y] = []
        }
        partialStoriesBase[x][y] << story
    }

    private void cutAndSavePartialStory(Story source, Integer stepi, Integer stepj) {
        Story partialStory = cutPartialStory(source, stepi, stepj)
        save(partialStory)
    }

    // single entry single exit tree decomposer`
    // decompose the result of depth execution of petri net
    // a list of sequences of state and transition event
    // partially overlapping (see Analysis class)
    void decompose(Analysis analysis) {

        List<Story> stories = analysis.storyBase.base


        Integer firstCutPos, secondCutPos

        for (int z = 0; z < stories.size(); z++) {

            Story first = stories[z]
            Story second = stories[z + 1]

            log.trace("first story: " + first)
            log.trace("second story: " + second)

            log.trace("second story last step: " + ": " + second.steps.last())

            Integer i
            for (i = first.steps.size() - 1; i >= 0; i--) {
                log.trace("first story step: " + i + ": " + first.steps[i])
                if (second.steps.last() == first.steps[i]) {
                    log.trace("first cut found - i: $i")
                    firstCutPos = i
                    break
                }
            }

            if (firstCutPos == null) throw new RuntimeException("You shouldn't be here.")

            log.trace("first cut position: "+i)
            if (firstCutPos < first.steps.size()) {
                log.trace("True cut")
                cutAndSavePartialStory(first, i, -1)
            } else {
                log.trace("0 pos => No cut")
            }

            Integer j
            for (i = i - 1; i >= 0 && (secondCutPos == null); i--) {
                log.trace("first story step: " + i + ": " + first.steps[i])
                for (j = second.steps.size() - 2; j >= 0; j--) {
                    log.trace("second story step: " + j + ": " + second.steps[i])
                    if (second.steps[j] == first.steps[i]) {
                        log.trace("second cut found - i: $i, j: $j")
                        secondCutPos = j
                        break
                    }
                }
            }

            if (secondCutPos == null) throw new RuntimeException("You shouldn't be here.")

            log.trace("second cut position: "+secondCutPos)
            cutAndSavePartialStory(first, secondCutPos, firstCutPos)
            cutAndSavePartialStory(second, secondCutPos, -1)

            // last part
            if (z == stories.size() - 2) {
                cutAndSavePartialStory(second, 0, secondCutPos)
                break
            }

        }

        println partialStoriesBase
    }

}
