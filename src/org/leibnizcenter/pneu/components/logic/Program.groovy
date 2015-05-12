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
//package org.leibnizcenter.pneu.components.logic
//
//import org.leibnizcenter.pneu.components.logic.base.CausalRuleBase
//import org.leibnizcenter.pneu.components.logic.base.ConstantBase
//import org.leibnizcenter.pneu.components.logic.base.ExpressionBase
//import org.leibnizcenter.pneu.components.logic.base.LogicRuleBase
//import org.leibnizcenter.pneu.components.logic.base.PredicateBase
//
//class Program {
//    CausalRuleBase causalRuleBase
//    ExpressionBase causalExpressionBase
//    LogicRuleBase logicRuleBase
//    ExpressionBase logicExpressionBase
//    PredicateBase predicateBase
//    ConstantBase constantBase
//
//    public Program() {
//
//        predicateBase = new PredicateBase()
//
//        constantBase = predicateBase.getConstantBase()
//
//        causalRuleBase = new causalRuleBase(predicateBase: predicateBase)
//        causalExpressionBase = causalRuleBase.getExpressionBase()
//
//        logicRuleBase = new logicRuleBase(predicateBase: predicateBase)
//        logicExpressionBase = logicRuleBase.getExpressionBase()
//
//    }
//
//    void print() {
//        println("--- Logic Rule base -------------------------- ")
//        logicRuleBase.print()
//        println("--- Logic Expression base ----------------------- ")
//        logicExpressionBase.print()
//        println("--- Causal Rule base -------------------------- ")
//        causalRuleBase.print()
//        println("--- Causal Expression base ----------------------- ")
//        causalExpressionBase.print()
//        println("--- Predicate base -------------------------- ")
//        predicateBase.print()
//        println("--- Constant base -------------------------- ")
//        constantBase.print()
//    }
//
//}
