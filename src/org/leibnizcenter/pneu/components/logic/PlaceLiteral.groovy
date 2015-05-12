package org.leibnizcenter.pneu.components.logic
import groovy.transform.EqualsAndHashCode

/**
 * A place literal is a possible label for places
 */

@EqualsAndHashCode
class PlaceLiteral {
    boolean naf      // Default negation (or negation as failure)
    ClassicLiteral literal

    List<Variable> variables() {
        literal.variables()
    }

}