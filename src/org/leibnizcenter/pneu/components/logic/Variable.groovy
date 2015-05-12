package org.leibnizcenter.pneu.components.logic

import groovy.transform.EqualsAndHashCode

/**
 * A variable is an identifier dynamically associated to an element
 */

@EqualsAndHashCode
class Variable extends Term {

    String name
    String comment // this is metadata !!!

    String toString() { name }

    ClassicLiteral toLiteral() {
        ClassicLiteral.build(this)
    }

    static build(String name) {
        new Variable(name: name)
    }

    boolean isVariable() { return true }
}
