package pneu.subsumption

import groovy.util.logging.Log4j
import pneu.components.petrinet.Place
import pneu.components.petrinet.Token

@Log4j
class LocalStateSubsumption {

    Place generalPlace
    Place specificPlace
    boolean negated = false

    Place ancillaryPlace

    Type type

    enum Type {
        NONE,
        SUBSUMES,
        ANCILLARY_SUBSUMES
    }

    // to check whether a "general" position (local state) concreteSubsumption a "specific" one
    LocalStateSubsumption(Place inputGeneralPlace,
                          Place inputSpecificPlace,
                          Map<String, Map<String, String>> mapIdentifiers,
                          Boolean inputNegated = false) {

        generalPlace = inputGeneralPlace
        specificPlace = inputSpecificPlace
        negated = inputNegated

        log.trace("does local state " + generalPlace.label() + " subsume " + specificPlace.label() + "?")

        if (!generalPlace.subsumes(specificPlace, negated)) {
            log.trace("no, local state " + generalPlace.label() + " does not subsume " + specificPlace.label() + " (because of its structure).")
            return
        }

        List<Token> generalTokens = generalPlace.marking
        List<Token> specificTokens = specificPlace.marking

        if (specificTokens.size() < generalTokens.size()) {
            log.trace("no, as the specific state has less tokens than the generic one.")
            type = Type.NONE
        } else if (generalTokens.size() == 0) {
            throw new RuntimeException("Not yet considered.")
        } else {
            // for all general tokens, there should be at least a *distinct* specific token which is subsumed
            for (generalToken in generalPlace.marking) {
                Token token
                for (specificToken in specificTokens) {
                    if (generalToken.subsumes(specificToken, mapIdentifiers, negated)) {
                        token = specificToken
                    }
                }
                if (token == null) {
                    log.trace("no, local state " + generalPlace.label() + " does not subsume " + specificPlace.label() + " (because of its content).")
                    type = Type.NONE
                    return
                }
                specificTokens = specificTokens - token
            }
            log.trace("yes, local state " + generalPlace.label() + " subsumes " + specificPlace.label() + ".")
            type = Type.SUBSUMES
        }
    }
}

