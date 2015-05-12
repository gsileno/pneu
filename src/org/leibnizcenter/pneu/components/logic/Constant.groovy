package org.leibnizcenter.pneu.components.logic

import groovy.transform.EqualsAndHashCode

/**
 * A constant is an identifier statically associated to an element
 */

@EqualsAndHashCode
class Constant extends Term {

    String name
    String comment // this is metadata !!!

    String toString() { name }

    ClassicLiteral toLiteral() {
        ClassicLiteral.build(this)
    }

    static build(String name) {
        new Constant(name: name)
    }

    boolean isConstant() { return true }
}
