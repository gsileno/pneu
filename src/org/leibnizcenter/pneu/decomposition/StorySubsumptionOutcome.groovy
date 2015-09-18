package org.leibnizcenter.pneu.decomposition

import org.leibnizcenter.pneu.animation.monolithic.analysis.Story


class StorySubsumptionOutcome {

    Story generalStory
    Story specificStory

    // TODO: multiple mapping of identifiers may be possible
    Map<String, Map<String, String>> mapIdentifiers

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

