package org.leibnizcenter.pneu.components.logic.base

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.logic.Constant

@Log4j
class ConstantBase {

    List<Constant> base = []

    List<Constant> list() { base }

    /* Check if the constant exists, if not it adds it
     * @return position in the const base
    */
//    Integer add(Constant const) {
//        Integer pos = findIndexOf(const)
//
//        log.trace "attempting to add Constant "+const.toString()
//
//        if (pos == -1) {
//            pos = base.size()
//            base << const
//
//            log.trace "Constant "+const.toString()+" added @ "+pos
//        }
//
//        pos
//    }

    Integer add(String name) {
        add(new Constant(name: name))
    }

    Constant find(String name) {
        base.find { it.name == name }
    }

//    Constant find(Constant const) {
//        base.find { it.name == const.name }
//    }

    Integer findIndexOf(String name) {
        base.findIndexOf { it.name == name }
    }

//    Integer findIndexOf(Constant const) {
//        base.findIndexOf { it.name == const.name }
//    }

    Constant read(Integer pos) {
        log.trace "reading Constant @ "+pos
        base[pos]
    }

    Constant read(String name) {
        read(find(name))
    }

    Integer size() { base.size() }

    void show() {
        for (Integer i = 0; i < base.size(); i++) {
            println i + ": " + base[i].toString()
        }
    }
}
