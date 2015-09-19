package org.leibnizcenter.pneu.components.petrinet

abstract class Place extends Node {

    List<Token> marking = []

    // operational semantics
    void flush() {
        marking.clear()
    }

    // useful functions
    abstract Place minimalClone()
    abstract Boolean compare(Place other)
    abstract Boolean subsumes(Place other)
    abstract Boolean subsumes(Place other, Boolean negateHead)
    abstract Token createToken()

}
