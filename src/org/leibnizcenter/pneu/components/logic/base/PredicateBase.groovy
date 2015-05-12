package org.leibnizcenter.pneu.components.logic.base

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.logic.Constant
import org.leibnizcenter.pneu.components.logic.Predicate
import org.leibnizcenter.pneu.components.logic.Variable

@Log4j
class PredicateBase {

    List<Predicate> base = []

    List<Predicate> list() { base }

    Integer size() { base.size() }

    void show() {
        for (Integer i = 0; i < base.size(); i++) {
            println i + ": " + base[i].toString()
        }
    }
}
