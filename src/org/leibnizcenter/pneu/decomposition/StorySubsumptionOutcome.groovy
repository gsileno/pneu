package org.leibnizcenter.pneu.decomposition

import org.leibnizcenter.pneu.animation.monolithic.analysis.Story


class StorySubsumptionOutcome {

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

    Type type() {
        if (leftGeneralLimit == 0 && rightGeneralLimit == generalStory.steps.size() - 1 &&
                leftSpecificLimit == 0 && rightSpecificLimit == specificStory.steps.size() - 1)
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

        output += generalStory.toString() + " <- \n" + specificStory.toString() + "\n"+ "? "+type()

        if (type() == Type.PARTIALLY_SUBSUMES) {
            output += " (" + leftGeneralLimit + ", " + rightGeneralLimit + ") <- " + "(" + leftSpecificLimit + ", " + rightSpecificLimit + ")\n"
        }

        output
    }

}

