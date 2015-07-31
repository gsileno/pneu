package org.leibnizcenter.pneu.components.petrinet

class Place extends Node {
    List<Token> marking = []

    String toString() {
        if (name != "") return name+" ("+marking.size()+")"
        else return id+" ("+marking.size()+")"
    }

    String toMinString() {
        if (name != "") return name
        else return id
    }

    void flush() {
        marking.clear()
    }

}
