// ----------------------------------------------------------------------------
// Copyright (C) 2014 G. Sileno
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
// To contact the authors:
// http://www.leibnizcenter.org/~sileno
//----------------------------------------------------------------------------

package org.leibnizcenter.pneu.components.logic.base

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.logic.CausalRule
import org.leibnizcenter.pneu.components.logic.Expression
import org.leibnizcenter.pneu.components.logic.InlineOperator

// import util.minterms.Expression

/**
 * Simple data structure to maintain rules
 **/

@Log4j
class CausalRuleBase {
    List<CausalRule> base = []

    ExpressionBase expressionBase

    public CausalRuleBase() {
        expressionBase = new ExpressionBase()
    }

    List<CausalRule> list() { return base }

    /* Add the rule to the rule base.
     * For the constraint-based check if the rule exists, if not it adds it
     * @return position in the rule base
     */
    Integer add(CausalRule rule, String identifier = null) {

        Integer pos

        rule = rule.clone()

        log.trace "attempting to add Rule "+rule.toString()

        pos = base.size()
        if (identifier == null) rule.identifier = "_r"+pos
        rule.pos = pos

        base << rule

        log.trace "Rule "+rule.toString()+" added @ "+base.size()

        // automatically add the formula associated to the rule
        expressionBase.add(rule.antecedent, rule.consequent, InlineOperator.CAUSES);

        pos
    }

    /* Remove the rule from the rule base
     * @return past position in the rule base of the removed rule
     */
    Integer remove(CausalRule rule) {

        Integer pos

        log.trace "attempting to remove Rule "+rule.toString()

        pos = findIndexOf(rule)
        if (pos != -1) return pos

        base.remove(pos)

        log.trace "Rule "+rule.toString()+" removed from @ "+pos

        pos
    }

    List<Integer> add(List<CausalRule> rules) {
        List<Integer> ruleIds = []
        for (rule in rules) {
            ruleIds.add(add(rule))
        }
        ruleIds
    }

    Integer add(Expression consequent, Expression antecedent) {
        add(CausalRule.build(consequent, antecedent))
    }

    Integer add(Integer consequentId, Integer antecedentId) {
        add(expressionBase.read(consequentId), expressionBase.read(antecedentId))
    }

    CausalRule find(CausalRule rule) {
        base.find { (it.consequent == rule.consequent) && (it.antecedent == rule.antecedent) }
    }

    Integer findIndexOf(CausalRule rule) {
        base.findIndexOf { (it.consequent == rule.consequent) && (it.antecedent == rule.antecedent) }
    }

    CausalRule read(Integer pos) {
        base[pos]
    }

    Integer size() {
        base.size()
    }


    //////////////////////////
    // Views
    ////////////////////////


    String toString() {
        String output = "["
        for (rule in base) {
            output += rule.toString() + " # "
        }
        output += "]"
        output
    }

    void view() {
        base.eachWithIndex() { elem, i ->
            println i + ": " + elem.toString()
        }
    }

}
