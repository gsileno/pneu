package org.leibnizcenter.pneu.components.petrinet

abstract class Place extends Node {
    List<Token> marking = []

    void flush() {
        marking.clear()
    }

    abstract Place clone()

    abstract Boolean compare(Place p1, Place p2)

}
