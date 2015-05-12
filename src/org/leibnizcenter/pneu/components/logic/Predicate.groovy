package org.leibnizcenter.pneu.components.logic

/**
 * A predicate can be seen as a boolean function
 */

class Predicate extends Function {

    PredicateSymbol symbol

    static build(String symbol) {
        new Predicate(new PredicateSymbol(symbol: symbol))
    }

    boolean isPredicate() { return true }
}
