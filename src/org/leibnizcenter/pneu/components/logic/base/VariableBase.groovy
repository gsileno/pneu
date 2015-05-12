package org.leibnizcenter.pneu.components.logic.base

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.logic.Variable

@Log4j
class VariableBase {

    List<Variable> base = []

    List<Variable> list() { base }

    /* Check if the variable exists, if not it adds it
     * @return position in the var base
    */
    Integer add(Variable var) {
        Integer pos = findIndexOf(var)

        log.trace "attempting to add Variable "+var.toString()

        if (pos == -1) {
            pos = base.size()
            base << var

            log.trace "Variable "+var.toString()+" added @ "+pos
        }

        pos
    }

    Integer add(String name) {
        add(new Variable(name: name))
    }

    Variable find(String name) {
        base.find { it.name == name }
    }

    Variable find(Variable var) {
        base.find { it.name == var.name }
    }

    Integer findIndexOf(String name) {
        base.findIndexOf { it.name == name }
    }

    Integer findIndexOf(Variable var) {
        base.findIndexOf { it.name == var.name }
    }

    Variable read(Integer pos) {
        log.trace "reading Variable @ "+pos
        base[pos]
    }

    Variable read(String name) {
        read(find(name))
    }

    Integer size() { base.size() }

    void show() {
        for (Integer i = 0; i < base.size(); i++) {
            println i + ": " + base[i].toString()
        }
    }
}
