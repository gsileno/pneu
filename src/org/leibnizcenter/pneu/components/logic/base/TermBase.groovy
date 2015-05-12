package org.leibnizcenter.pneu.components.logic.base

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.logic.Term

@Log4j
class TermBase {

    List<Term> base = []

    List<Term> list() { base }

    Integer size() { base.size() }

    void show() {
        for (Integer i = 0; i < base.size(); i++) {
            println i + ": " + base[i].toString()
        }
    }
}
