package org.leibnizcenter.pneu.components.petrinet

import groovy.transform.Immutable

abstract class Token {

    abstract Boolean compare(Token t)

}
