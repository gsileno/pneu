package org.leibnizcenter.pneu.components.petrinet

import groovy.transform.Immutable

abstract class Token {

    abstract Boolean compare(Token t)

    abstract Token minimalClone()

    abstract String label()

    abstract Boolean subsumes(Token t)

}
