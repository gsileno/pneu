package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.Place

class BasicPlace extends Place {

    List<BasicToken> marking = []

    String toString() {
        if (name != "") return name+" ("+marking.size()+")"
        else return id+" ("+marking.size()+")"
    }

    String toMinString() {
        if (name != "") return name
        else return id
    }

    static Boolean compare(Place p1, Place p2) {
        if (p1 == p2) return true
        if (p1.name != p2.name) return false
        return true
    }

    BasicPlace clone() {
        return new BasicPlace(
                marking: marking.collect()
        )
    }

}
