package org.leibnizcenter.pneu.components.logic.base

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.logic.Expression
import org.leibnizcenter.pneu.components.logic.InlineOperator
import org.leibnizcenter.pneu.components.logic.LogicRule

// import util.minterms.Expression

/**
* Simple data structure to maintain rules
**/

@Log4j
class LogicRuleBase {
    List<LogicRule> base = []

    ExpressionBase expressionBase

    public LogicRuleBase() {
        expressionBase = new ExpressionBase()
    }

    List<LogicRule> list() { return base }

//    /* Add the rule to the rule base.
//     * For the constraint-based check if the rule exists, if not it adds it
//     * @return position in the rule base
//     */
//    Integer add(LogicRule rule, String identifier = null) {
//
//        Integer pos
//
//        rule = rule.clone()
//
//        log.trace "attempting to add Rule "+rule.toString()
//
//        pos = base.size()
//        if (identifier == null) rule.identifier = "_r"+pos
//        rule.pos = pos
//
//        base << rule
//
//        log.trace "Rule "+rule.toString()+" added @ "+base.size()
//
//        // automatically add the formula associated to the rule
//        expressionBase.add(rule.body, rule.head, InlineOperator.IMPLIES);
//
//        pos
//    }
//
//    /* Remove the rule from the rule base
//     * @return past position in the rule base of the removed rule
//     */
//    Integer remove(LogicRule rule) {
//
//        Integer pos
//
//        log.trace "attempting to remove Rule "+rule.toString()
//
//        pos = findIndexOf(rule)
//        if (pos != -1) return pos
//
//        base.remove(pos)
//
//        log.trace "Rule "+rule.toString()+" removed from @ "+pos
//
//        pos
//    }
//
//    List<Integer> add(List<LogicRule> rules) {
//        List<Integer> ruleIds = []
//        for (rule in rules) {
//            ruleIds.add(add(rule))
//        }
//        ruleIds
//    }
//
//    Integer add(Expression head, Expression body) {
//        add(LogicRule.build(head, body))
//    }
//
//    Integer add(Integer headId, Integer bodyId) {
//        add(expressionBase.read(headId), expressionBase.read(bodyId))
//    }
//
//    LogicRule find(LogicRule rule) {
//        base.find { (it.head == rule.head) && (it.body == rule.body) }
//    }
//
//    Integer findIndexOf(LogicRule rule) {
//        base.findIndexOf { (it.head == rule.head) && (it.body == rule.body) }
//    }
//
//    LogicRule read(Integer pos) {
//        base[pos]
//    }
//
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
