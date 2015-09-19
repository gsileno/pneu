package org.leibnizcenter.pneu.subsumption

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.TransitionEvent

@Log4j
class EventSubsumption {

    TransitionEvent generalEvent
    TransitionEvent specificEvent
    // TransitionEvent ancillaryEvent

    Type type

    enum Type {
        NONE,
        SUBSUMES
    }

    // to check whether a "general" event subsumes a "specific" one
    EventSubsumption(TransitionEvent inputGeneralEvent,
                     TransitionEvent inputSpecificEvent,
                     Map<String, Map<String, String>> mapIdentifiers) {

        generalEvent = inputGeneralEvent
        specificEvent = inputSpecificEvent

        log.trace("does event " + generalEvent.label() + " subsume " + specificEvent.label() + "?")

        if (!generalEvent.transition.subsumes(specificEvent.transition)) {
            log.trace("no, event " + generalEvent.label() + " does not subsume " + specificEvent.label() + " (because of its structure).")
            type = Type.NONE
            return
        }

        if (!generalEvent.token.subsumes(specificEvent.token, mapIdentifiers)) {
            log.trace("no, event " + generalEvent.label() + " does not subsume " + specificEvent.label() + " (because of its content).")
            type = Type.NONE
            return
        }
        log.trace("yes, event " + generalEvent.label() + " subsumes " + specificEvent.label() + ".")
        type = Type.SUBSUMES
    }


}

