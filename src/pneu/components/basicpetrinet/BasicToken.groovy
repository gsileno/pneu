package pneu.components.basicpetrinet

import pneu.components.petrinet.Token

class BasicToken extends Token {

    // basic net tokens are all the same
    Boolean compare(Token t) {
        true
    }

    // TODO: check negative subsumption (p -> not q)
    Boolean subsumes(Token t, Map<String, Map<String, String>> mapAnonymousIdentifiers = [:], Boolean negated = false) {
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
