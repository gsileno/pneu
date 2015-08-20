package org.leibnizcenter.pneu.components.petrinet

abstract class Place extends Node {
    List<Token> marking = []

    void flush() {
        marking.clear()
    }

    String toString() {
        if (name != "") return name+" ("+marking.size()+")"
        else return id+" ("+marking.size()+")"
    }

    String toMinString() {
        if (name != "") return name
        else return id
    }

    abstract Place clone()

    abstract Boolean compare(Place p1, Place p2)

}
