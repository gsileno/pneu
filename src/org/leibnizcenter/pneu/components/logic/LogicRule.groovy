//package org.leibnizcenter.pneu.components.logic
//
//class LogicRule {
//
//    Expression head  // the consequent of the rule
//    Expression body  // the antecedent of the rule
//
//    // TODO: this should be metadata
//    String identifier // the identifier of the rule
//    String comment    // a human readable description of the rule
//    Integer pos       // temporal/ordering position.
//
//    //////////////////
//    // Builders
//    //////////////////
//
//    static Rule build(Expression head, Expression body) {
//        return new Rule(head: head, body: body)
//    }
//
//    static Rule build(ClassicLiteral conclusion, Expression body) {
//        Expression head = Expression.build(conclusion);
//        return new Rule(head: head, body: body)
//    }
//
//    static Rule build(ClassicLiteral conclusion, List<ClassicLiteral> premises) {
//        log.debug("Building rule: "+ premises +" -> "+conclusion)
//
//        Expression head = Expression.build(conclusion);
//        Expression body = Expression.buildFromLiterals(premises, InlineOperator.AND);
//        return new Rule(head: head, body: body)
//    }
//
//    static Rule build(List<ClassicLiteral> conclusions, List<ClassicLiteral> premises) {
//        Expression head = Expression.buildFromLiterals(conclusions, InlineOperator.AND);
//        Expression body = Expression.buildFromLiterals(premises, InlineOperator.AND);
//        return new Rule(head: head, body: body)
//    }
//
//    ///////////////////////////////////////
//
//    Expression toFormula() {
//        Expression.build(body, head, InlineOperator.IMPLIES)
//    }
//
//    // TODO invent something to bridge the comments and identifiers
//
//    /**
//     * separates the OR inputs in the antecedent
//     * a OR b -> c => (a -> c) AND (b -> c)  [antecedent disjunction]*
//     * @return logical equivalent list of AND rules
//     */
//    List<Rule> bodyDisjunction() {
//        List<Expression> disjoints = body.disjoin()
//        List<Rule> elems = []
//
//        disjoints.each() {
//            elems << new Rule(body: it, head: head)
//        }
//
//        elems
//    }
//
//    /**
//     * brings the negation from outside the operator downto the literals
//     * @return logical equivalent rule with negation only at the literal input level
//     */
//    Rule reduceNegation() {
//        return Rule.build(head.reduceNegation(), body.reduceNegation())
//    }
//
//    /**
//     * separates the higher order AND inputs in the consequent
//     * a -> c AND d => (a -> c) AND (a -> d) [consequent disjunction]
//     * @return logically equivalent list of AND rules
//     */
//    List<Rule> headDisjunction() {
//        if (head.symbolicOperator.op == InlineOperator.AND) {
//            List<Rule> elems = []
//
//            head.inputExpressions.each() {
//                elems << new Rule(body: body, head: it)
//            }
//            elems
//        } else {
//            [new Rule(body: body, head: head)]
//        }
//    }
//
//    /**
//     * it returns the list of component rules.
//     * reduction is operated as:
//     * a OR b -> c => (a -> c) AND (b -> c)  [antecedent disjunction]
//     * a -> c AND d => (a -> c) AND (a -> d) [consequent disjunction]
//     * @return list of component rules
//     */
//    List<Rule> components() {
//
//        List<Rule> components = []
//        List<Rule> orComponents = bodyDisjunction()
//
//        for (orComponent in orComponents) {
//            components.addAll(orComponent.headDisjunction())
//        }
//
//        components
//    }
//
//    /**
//     * return the list of premises (as literals)
//     * @return list of premises
//     */
//    List<Expression> premises() {
//        body.andComponents()
//    }
//
//    /**
//     * return the list of conclusions (as literals)
//     * @return list of conclusions
//     */
//    List<Expression> conclusions() {
//        head.andComponents()
//    }
//
//    /**
//     * return the list of premises (as literals)
//     * @return list of premises
//     */
//    List<ClassicLiteral> literalPremises() {
//        List<ClassicLiteral> literalPremises = []
//        for (Expression formula in premises()) {
//            ClassicLiteral literal = formula.toLiteral()
//            if (literal != false)
//                literalPremises << literal
//            else
//                return false
//        }
//        literalPremises
//    }
//
//    /**
//     * return the list of conclusions (as literals)
//     * @return list of conclusions
//     */
//    List<ClassicLiteral> literalConclusions() {
//        List<ClassicLiteral> literalConclusions = []
//        for (Expression formula in conclusions()) {
//            ClassicLiteral literal = formula.toLiteral()
//            if (literal != false)
//                literalConclusions << literal
//            else
//                return false
//        }
//        literalConclusions
//    }
//
//    /**
//     * return the conclusion (as literal). for simple rules.
//     * @return conclusion
//     */
//    ClassicLiteral conclusion() {
//        List<ClassicLiteral> conclusions = literalConclusions()
//        if (conclusions.size() > 1) {
//            log.warn("I was expecting a simple rule, with only one conclusion.")
//        }
//        conclusions[0]
//    }
//
//    /**
//     * return the list of relevant factors (as atoms)
//     * @return
//     */
//    List<Constant> relevant() {
//        List<Constant> atoms = []
//        body.inputLiterals.each() {
//            atoms << it.getAtom()
//        }
//        atoms
//    }
//
//    ////////////////////////////////////////
//
//    boolean isFact() { (body == null) }
//    boolean isConstraint() { (head == null) }
//
//    //////////////////
//    // Views
//    //////////////////
//
//    String toString() {
//        String output
//        output = body.toString() + " -> " + head.toString()
//        if (identifier) { output += " ($identifier)" }
//        output
//    }
//
//    String toASP() {
//        String output
//        output = head.toASP(true) + " :- " + body.toASP() + "."
//        output
//    }
//
//}
