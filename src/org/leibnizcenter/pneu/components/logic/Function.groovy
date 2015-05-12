package org.leibnizcenter.pneu.components.logic

class Function {

    FunctionSymbol symbol
    List<Term> parameters

    String comment // this is metadata !!!

    String toString() { symbol.toString()+"/"+parameters.size() }

    ClassicLiteral toLiteral() {
        ClassicLiteral.build(this)
    }

    static build(String symbol) {
        new Function(new FunctionSymbol(symbol: symbol))
    }

    List<Variable> variables() {
        List<Variable> variables
        for (parameter in parameters) {
            if (parameter.isVariable())
                variables << parameter
        }
        variables
    }

    boolean isFunction() { return true }
}
