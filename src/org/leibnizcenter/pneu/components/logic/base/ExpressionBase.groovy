//// ----------------------------------------------------------------------------
//// Copyright (C) 2014 G. Sileno
////
//// This library is free software; you can redistribute it and/or
//// modify it under the terms of the GNU Lesser General Public
//// License as published by the Free Software Foundation; either
//// version 2.1 of the License, or (at your option) any later version.
////
//// This library is distributed in the hope that it will be useful,
//// but WITHOUT ANY WARRANTY; without even the implied warranty of
//// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
//// Lesser General Public License for more details.
////
//// You should have received a copy of the GNU Lesser General Public
//// License along with this library; if not, write to the Free Software
//// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
////
//// To contact the authors:
//// http://www.leibnizcenter.org/~sileno
////----------------------------------------------------------------------------
//
//package org.leibnizcenter.pneu.components.logic.base
//
//import groovy.util.logging.Log4j
//import org.leibnizcenter.pneu.components.logic.Expression
//
//@Log4j
//class ExpressionBase {
//    List<Expression> base = []
//
//    ConstantBase constantBase
//
//    public ExpressionBase() {
//        constantBase = new ConstantBase()
//        variableBase = new VariableBase()
//
//    }
//
//    public ConstantBase getConstantBase() { constantBase }
//
//    List<Expression> list() { return base }
//
//    /* Check if the formula exists, if not it adds it,
//     * but before, it adds all the sub-formulas
//     * @return position in the rule base
//     */
//    Integer add(Expression formula) {
//        Integer pos = findIndexOf(formula)
//
//        log.trace "attempting to add Expression "+formula.toString()
//
//        operatorBase.add(formula.symbolicOperator)
//        operatorBase.add(formula.compiledOperator)
//
//        if (pos == -1) {
//
//            if (formula.inputExpressions.size() == 0) { // add literals if there are no sub-formulas
//                for (Literal literal in formula.inputLiterals) {
//                    constantBase.add(literal.constant)
//                }
//            } else { // add sub-formulas before
//              for (Expression f in formula.inputExpressions) {
//                 add(f)
//              }
//            }
//
//            pos = base.size()
//            base << formula
//
//            log.trace "Expression "+formula.toString()+" added @ "+pos
//        }
//
//        pos
//    }
//
//    List<Integer> add(List<Expression> formulas) {
//        List<Integer> formulaIds = []
//        for (formula in formulas) {
//            formulaIds.add(add(formula))
//        }
//        formulaIds
//    }
//
//    // constantic formula, i.e. simple logic interface
//    Integer add(Constant constant, Op op = Op.EMPTY, List<Integer> params = []) {
//        Expression formula = Expression.build(constant, op, params)
//        add(formula)
//    }
//
//    // simple literal
//    Integer add(Literal literal) {
//        Expression formula = Expression.build(literal)
//        add(formula)
//    }
//
//    // unary formula
//    Integer add(Expression input, Op op, List<Integer> params = []) {
//        add(input)
//        Expression formula = Expression.build(input, op, params)
//        add(formula)
//    }
//
//    Integer add(Integer inputId, Op op, List<Integer> params = []) {
//        add(read(inputId), op, params)
//    }
//
//    // binary formula
//    Integer add(Expression leftInput, Expression rightInput, Op op, List<Integer> params = []) {
//        add(leftInput); add(rightInput)
//        Expression formula = Expression.build(leftInput, rightInput, op, params)
//        add(formula)
//    }
//
//    Integer add(Integer leftInputId, Integer rightInputId, Op op, List<Integer> params = []) {
//        add(read(leftInputId), read(rightInputId), op, params)
//    }
//
//    // n-ary formula
//    Integer add(List<Expression> inputs, Op op, List<Integer> params = []) {
//        for (input in inputs) { add(input) }
//        Expression formula = Expression.buildFromExpressions(inputs, op, params)
//        add(formula)
//    }
//
//    Expression find(Expression formula) {
//        base.find {
//          (it.symbolicOperator == formula.symbolicOperator) &&
//          (it.inputExpressions == formula.inputExpressions)
//        }
//    }
//
//    Integer findIndexOf(Expression formula) {
//        base.findIndexOf {
//          (it.symbolicOperator == formula.symbolicOperator) &&
//          (it.inputExpressions == formula.inputExpressions) &&
//          (it.inputLiterals == formula.inputLiterals)  // for constantic formulas
//        }
//    }
//
//    Expression read(Integer pos) { base[pos] }
//
//    Integer size() { base.size() }
//
//    void print() {
//        base.eachWithIndex() { elem, i ->
//            println i + ": " + elem.toString()
//        }
//    }
//}
