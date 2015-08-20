package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.Place

class BasicPlace extends Place {

    String toString() {
        if (name != "") return name+" ("+marking.size()+")"
        else return id+" ("+marking.size()+")"
    }

    String toMinString() {
        if (name != "") return name
        else return id
    }

}
