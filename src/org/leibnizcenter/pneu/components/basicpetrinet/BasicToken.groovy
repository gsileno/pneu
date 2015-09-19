package org.leibnizcenter.pneu.components.basicpetrinet

import groovy.transform.Immutable
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition

class BasicToken extends Token {

    // basic net tokens are all the same
    Boolean compare(Token t) {
        true
    }

    // TODO: check negative subsumption (p -> not q)
    Boolean subsumes(Token t, Map<String, Map<String, String>> mapAnonymousIdentifiers = [:], Boolean negateHead = false) {
        true
    }

    static Boolean compare(Token t1, Token t2) {
        t1.compare(t2)
    }

    Token minimalClone() {
        new BasicToken()
    }

    static Token createToken() {
        new BasicToken()
    }

    String toString() { "*" }

    String label() { "" }

}
