package org.leibnizcenter.pneu.components.logic
import groovy.transform.EqualsAndHashCode

/**
 * A (classic) literal is an atom, or a "not" atom considering only strong negation = not
 */

@EqualsAndHashCode
class ClassicLiteral {

    boolean neg      // Strong Negation (classic negation) operator
    Predicate predicate

    String toString() { (neg?"-":"")+predicate }

    Boolean equalsNot(ClassicLiteral other) {
       (predicate == other.predicate) && (neg == !other.neg)
    }

    List<Variable> variables() {
        predicate.variables()
    }

    static ClassicLiteral build(Predicate predicate, neg = false) {
        new ClassicLiteral(predicate: predicate, neg: neg)
    }

    ClassicLiteral negate() {
        return new ClassicLiteral(predicate: predicate, neg: !neg)
    }

    Expression toFormula() {
        return Expression.build(this)
    }

}