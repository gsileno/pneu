package pneu.subsumption

import groovy.util.logging.Log4j
import pneu.animation.monolithic.analysis.State
import pneu.components.petrinet.Place

@Log4j
class StateSubsumption {

    State generalState
    State specificState
    State ancillarySpecificState

    Type type

    enum Type {
        NONE,
        SUBSUMES,
        ANCILLARY_SUBSUMES
    }

    // to check whether a "general" state subsumes a "specific" one
    // as we are considering executions, we are not any more dealing with potential transitions
    // but we can check only the steps
    StateSubsumption(State inputGeneralState, State inputSpecificState, Map<String, Map<String, String>> mapIdentifiers) {
        generalState = inputGeneralState
        specificState = inputSpecificState

        log.trace("does state " + generalState.placesToString() + " subsume " + specificState.placesToString() + "?")

        List<Place> specificPlaces = specificState.placeList
        List<Place> missingPlaces = []

        // strong or sufficient condition:
        // for all general places, there should be at least a *distinct* specific place which is subsumed
        for (generalPlace in generalState.placeList) {

            log.trace("checking general place: " + generalPlace)

            if (generalPlace.marking.size() == 0) {
                log.trace("the place is empty, jumping...")
            } else if (generalPlace.isLink()) {
                log.trace("the place is a link place, jumping...")
            } else {
                Place p

                for (specificPlace in specificPlaces) {
                    log.trace("considering specific place: " + specificPlace)

                    LocalStateSubsumption localStateSubsumption = new LocalStateSubsumption(generalPlace, specificPlace, mapIdentifiers)

                    if (localStateSubsumption.type == LocalStateSubsumption.Type.SUBSUMES) {
                        p = specificPlace
                        break
                    }
                }
                if (p == null) {
                    log.trace("no, general state " + generalState + " does not subsume specific state " + specificState + ".")
                    type = Type.ANCILLARY_SUBSUMES
                    missingPlaces << generalPlace
                }
                specificPlaces = specificPlaces - p
            }
        }


        if (missingPlaces.size() == 0) {
            log.trace("yes, general state " + generalState + " subsumes specific state " + specificState + ".")
            type = Type.SUBSUMES
        } else {
            // type = Type.NONE

            // weak or necessary condition:
            // for all general places there should be no specific place which subsume its negation

            // I can consider only the missing places
            // the problem is that we cannot define negation in basic petri nets!!!

            for (generalPlace in missingPlaces) {
                for (specificPlace in specificPlaces) {

                    LocalStateSubsumption localStateSubsumption = new LocalStateSubsumption(generalPlace, specificPlace, mapIdentifiers, true)

                    if (localStateSubsumption.type == LocalStateSubsumption.Type.SUBSUMES) {
                        log.trace("no, general state " + generalState + " is explicitly refuted by specific state " + specificState + ".")
                        type = Type.NONE
                        return
                    }
                }
            }

            // the remaining places can be used for ancillary alignment
            // i.e. specific places are created using the functors from the general places,
            // and the mapping from the specific one

            List<Place> ancillaryPlaces = []
            for (generalPlace in missingPlaces) {
                Place ancillaryPlace = generalPlace.minimalClone()
                ancillaryPlace.createToken() // TO BE DONE: use mapIdentifiers
                ancillaryPlaces << ancillaryPlace
            }

            ancillarySpecificState = new State(ancillaryPlaces)
        }
    }

    String toLog() {
        String output = ""

        output += ""

        output += generalState.toString() + " <- " + specificState.toString() + " ? " + type

        if (type == Type.ANCILLARY_SUBSUMES) {
            output += " (ancillary to specific: ${ancillarySpecificState.toString()})"
        }

        output
    }

}

