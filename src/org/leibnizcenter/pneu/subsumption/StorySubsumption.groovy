package org.leibnizcenter.pneu.subsumption

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story

@Log4j
class StorySubsumption {

    Story generalStory
    Story specificStory

    // boundaries for subsumption
    Integer leftGeneralLimit
    Integer rightGeneralLimit
    Integer leftSpecificLimit
    Integer rightSpecificLimit

    enum Type {
        NONE,
        SUBSUMES,
        PARTIALLY_SUBSUMES
    }

    // to check whether a "general" story subsumes a "specific" one
    StorySubsumption(Story inputGeneralStory, Story inputSpecificStory, Map<String, Map<String, String>> mapIdentifiers = [:]) {

        generalStory = inputGeneralStory
        specificStory = inputSpecificStory

        log.trace("does story " + generalStory + " subsume " + specificStory + "?")

        Boolean firstSpecific = true
        Boolean firstGeneral = true
        Boolean concludedSpecific = false
        Boolean concludedGeneral = false

        Integer curGeneralPos = 0, curSpecificPos = 0
        for (int i = curGeneralPos; i < generalStory.steps.size(); i++) {

            log.trace("general story attempts to pass to step " + i)
            Boolean alignmentFound = false

            for (int j = curSpecificPos; j < specificStory.steps.size(); j++) {
                log.trace("specific story attempts to pass to step " + j)

                StateSubsumption stateSubsumption = new StateSubsumption(generalStory.steps[i], specificStory.steps[j], mapIdentifiers)

                // check for subsumption of local states
                if (stateSubsumption.type == StateSubsumption.Type.SUBSUMES) {
                    if (j == specificStory.steps.size() - 1) { // if we are in the last specific step
                        log.trace("specific story has concluded. we cannot proceed further")
                        alignmentFound = true
                        concludedSpecific = true
                    } else if (i == generalStory.steps.size() - 1) { // if we are in the last general step
                        log.trace("general story has concluded. we cannot proceed further")
                        alignmentFound = true
                        concludedGeneral = true
                    } else { // in the general case

                        // if there is a transition link then jump to the next step
                        if (generalStory.eventsPerStep[i][0].transition.isLink()) {
                            log.trace("I have found a link transition on the general model, I jump the event.")
                            if (specificStory.eventsPerStep.get(j) != null) {
                                log.trace("the specific story has an event as well. we can override it.")
                                alignmentFound = true
                            } else {
                                log.trace("the specific story has no remaining events. it cannot be subsumed.")
                                throw new RuntimeException("To be checked")
                            }
                        } else {
                            for (; j < specificStory.steps.size() - 1; j++) {
                                log.trace("checking events between general step $i and specific step $j")
                                if (!specificStory.eventsPerStep[j][0].transition.isLink()) {
                                    EventSubsumption eventSubsumption = new EventSubsumption(generalStory.eventsPerStep[i][0], specificStory.eventsPerStep[j][0], mapIdentifiers)

                                    if (eventSubsumption.type == EventSubsumption.Type.SUBSUMES) {
                                        alignmentFound = true
                                        break
                                    }
                                } else {
                                    log.trace("I have found a link transition on the specific model, I jump it.")
                                    // there is an assumption there: that the subsumed model is at lower granularity than
                                    // the other one. therefore where link transitions appears in the subsumed model,
                                    // certainly there won't be specified transitions in the subsuming one.
                                    // however, in general we could have mixed situations. TODO: to be better reflected
                                }
                            }
                        }
                    }
                }

                if (alignmentFound) {
                    log.trace("alignment found: general $i vs specific $j")

                    curGeneralPos = i
                    curSpecificPos = j

                    if (firstGeneral) {
                        leftGeneralLimit = curGeneralPos
                        firstGeneral = false
                    }
                    if (firstSpecific) {
                        leftSpecificLimit = curSpecificPos
                        firstSpecific = false
                    }

                    rightGeneralLimit = curGeneralPos
                    rightSpecificLimit = curSpecificPos

                    if (concludedSpecific) {
                        return
                    } else {
                        if (!concludedGeneral) curSpecificPos++  // adjustment for the next cycle
                    }
                    break
                }
            }

            if (!alignmentFound) {
                if (firstGeneral && (i < generalStory.steps.size() - 1)) {
                    log.trace("no, story " + generalStory + " does not subsume " + specificStory + " from this step.")
                    log.trace("attempting by changing the left general limit.")
                } else {
                    log.trace("no, story " + generalStory + " does not subsume " + specificStory + ".")

                    if (leftGeneralLimit != null && rightGeneralLimit != null && leftSpecificLimit != null && rightSpecificLimit != null)
                        log.trace("but well, story " + generalStory + " partially subsume " + specificStory + ": (" +
                                leftGeneralLimit + ", " + rightGeneralLimit + ") vs (" +
                                leftSpecificLimit + ", " + rightSpecificLimit + ")")
                    log.trace("I cannot find correspondences for step " + generalStory.steps[i].placesToString() + " / event " + generalStory.eventsPerStep[i])
                    return
                }
            }
        }

        if (leftGeneralLimit != null && rightGeneralLimit != null && leftSpecificLimit != null && rightSpecificLimit != null) {
            if (leftGeneralLimit == 0 && rightGeneralLimit == generalStory.steps.size() - 1) {
                log.trace("yes, story " + generalStory + " subsumes " + specificStory + ".")
                if (leftSpecificLimit > 0 || rightSpecificLimit < specificStory.steps.size() - 1) {
                    log.trace("however, boundaries details have been neglected: (" +
                            leftSpecificLimit + ", " + rightSpecificLimit + "), intead of (0, "
                            + specificStory.steps.size() + ")")
                }
            } else {
                log.trace("well, story " + generalStory + " partially subsume " + specificStory + ": (" +
                        leftGeneralLimit + ", " + rightGeneralLimit + ") vs (" +
                        leftSpecificLimit + ", " + rightSpecificLimit + ")")
            }
        } else {
            throw new RuntimeException("You should not be here.")
        }
    }

    Type type() {
        if (leftGeneralLimit == 0 && rightGeneralLimit == generalStory.steps.size() - 1)
            return Type.SUBSUMES
        else if (leftGeneralLimit == null || rightGeneralLimit == null &&
                leftSpecificLimit == null || rightSpecificLimit == null)
            return Type.NONE
        else
            return Type.PARTIALLY_SUBSUMES
    }

    String toLog() {
        String output = ""

        output += ""

        output += generalStory.toString() + " <- " + specificStory.toString() + " ? " + type()

        Type type = type()

        if (type == Type.PARTIALLY_SUBSUMES || (type == Type.SUBSUMES && (leftSpecificLimit > 0 || rightSpecificLimit < specificStory.steps.size() - 1))) {
            output += " (" + leftGeneralLimit + ", " + rightGeneralLimit + ") <- " + "(" + leftSpecificLimit + ", " + rightSpecificLimit + ")"
        }

        output
    }

}

