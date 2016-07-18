package pneu.components.basicpetrinet

import pneu.components.petrinet.Place
import pneu.components.petrinet.Token

class BasicPlace extends Place {

    String name

    List<Token> marking = []

    String toString() {
        if (name != null) return name+" ("+marking.size()+")"
        else return id+(isLink()?"*":"")+" ("+marking.size()+")"
    }

    Boolean isLink() {
        name == null
    }

    String label() {
        if (name != null) name
        else if (isLink()) "*"
        else throw new RuntimeException("You should not be here")
    }

    static Place build(String label) {
        return new BasicPlace(name: label)
    }

    static Boolean compare(Place p1, Place p2) {
        if (p1 == p2) return true

        BasicPlace lp1 = (BasicPlace) p1
        BasicPlace lp2 = (BasicPlace) p2

        if (lp1.name != lp2.name) return false
        if (lp1.id != lp2.id) return false
        return true
    }

    Boolean compare(Place p) {
        compare(this, p)
    }

    // TODO: check negative subsumption (p -> not q)
    Boolean subsumes(Place p, Boolean negateHead = false) {
        BasicPlace lp = (BasicPlace) p

        if (!negateHead) {
            if (name != lp.name) return false
        } else {
            if ("-"+name != lp.name && name != "-"+lp.name) return false // TODO: very basic solution
        }

        return true

    }

    Token createToken() {
        Token token = new BasicToken()
        marking << token
        token
    }

    BasicPlace minimalClone() {
        return new BasicPlace(
                name: name,
                marking: marking.collect(),
                id: id
        )
    }

}
