package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Token

class BasicPlace extends Place {

    String name

    List<Token> marking = []

    String toString() {
        if (name != null) return name+" ("+marking.size()+")"
        else return id+" ("+marking.size()+")"
    }

    String label() {
        if (name != null) name
        else ""
    }

    static Place build(String label) {
        return new BasicPlace(name: label)
    }

    // subsumes is a only about the label! not about the marking!
    Boolean subsumes(Place p) {
        if (name == ((BasicPlace) p).name) return true
        return false
    }

    static Boolean compare(Place p1, Place p2) {
        if (p1 == p2) return true
        if (((BasicPlace) p1).name != ((BasicPlace) p2).name) return false
        return true
    }

    Boolean compare(Place p) {
        compare(this, p)
    }

    Token createToken() {
        Token token = new BasicToken()
        marking << token
        token
    }

    BasicPlace minimalClone() {
        return new BasicPlace(
                marking: marking.collect()
        )
    }

}
