package org.leibnizcenter.pneu.components.logic
import groovy.transform.EqualsAndHashCode

// triggering types:
// hidden: no topological constraints
// start: a place should follow with the same literal
// stop: a place should precede with the same literal
enum TriggerType { HIDDEN, START, STOP }

/**
 * A transition literal is a possible label for places
 */

@EqualsAndHashCode
class TransitionLiteral {
    TriggerType trigger
    ClassicLiteral literal

    List<Variable> variables() {
        literal.variables()
    }
}