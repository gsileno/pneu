package org.leibnizcenter.pneu.components.logic.base

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.logic.InlineOperator
import org.leibnizcenter.pneu.components.logic.Operator

@Log4j
class OperatorBase {
    List<Operator> base = []

    public OperatorBase() {}

    Operator add(Operator operator) {

        String name = (operator.toString().length()>0)?operator.toString():"EMPTY"

        log.trace "attempting to add Operator $name"

        Integer pos = findIndexOf(operator)

        if (pos == -1) {
            pos = base.size()
            base << operator

            log.trace "Operator $name added @ $pos"
        } else {
            operator = read(pos)
        }

        operator
    }

    Operator getInterfaceOperator(InlineOperator op, List<Integer> params = []) {
        add(Operator.buildInterfaceOperator(op, params))
    }

    Operator addUnaryOperator(Operator input, InlineOperator op, List<Integer> params = []) {
        add(Operator.buildUnaryOperator(input, op, params))
    }

    Operator addBinaryOperator(Operator leftInput, Operator rightInput, InlineOperator op, List<Integer> params = []) {
        add(Operator.buildBinaryOperator(leftInput, rightInput, op, params))
    }

    Operator addNaryOperator(List<Operator> inputs, InlineOperator op, List<Integer> params = []) {
        add(Operator.buildNaryOperator(inputs, op, params))
    }

    Operator find(Operator operator) {
        base.find {
            (it.inputOperators == operator.inputOperators &&
             it.op == operator.op &&
             it.params == operator.params)
        }
    }

    Integer findIndexOf(Operator operator) {
       base.findIndexOf {
            (it.inputOperators == operator.inputOperators &&
             it.op == operator.op &&
             it.params == operator.params)
        }
    }

    Operator read(Integer pos) { return base[pos] }

    Integer size() { return base.size() }

    void print() {
        base.eachWithIndex() { elem, i ->
            println i + ": " + elem.toString()
        }
    }
}