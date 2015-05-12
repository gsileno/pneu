package org.leibnizcenter.pneu.components.logic

import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Log4j

/**
 * Represents an expression which can be 
 * evaluated into a truth value.
 */
@Log4j @EqualsAndHashCode @AutoClone
class Expression {

    String identifier
    String comment

    // highest abstraction level
    List<Expression> inputExpressions = [] // sub-formulas in input
    Operator symbolicOperator              // last operator

    // low abstraction level (compiled)
    List<ClassicLiteral> inputLiterals = [] // hierarchical input in terms of factors
    Operator compiledOperator               // combination of all operators

    Expression() { }

    //////////////////
    // Builders
    //////////////////

    // atomic formula, i.e. simple logic interface
    static Expression build(Constant atom, InlineOperator op = InlineOperator.EMPTY, List<Integer> params = []) {

        log.trace "creating Formula from Atom: "+atom.toString()

        ClassicLiteral literal = new ClassicLiteral(atom: atom, neg: (op == InlineOperator.NEG))

        // the OR and AND operator applied to a single item correspond to the identity
        // the negation is recorded into the literal
        if (op == InlineOperator.NEG || op == InlineOperator.OR || op == InlineOperator.AND) {
            op = InlineOperator.EMPTY
        }

        new Expression(
           symbolicOperator: Operator.buildInterfaceOperator(op, params),
           compiledOperator: Operator.buildInterfaceOperator(op, params),
           inputLiterals: [literal]
        )
    }

    // literal formula, i.e. simple logic interface
    static Expression build(ClassicLiteral literal) {

        log.trace "creating Formula from Literal: "+literal.toString()

        // Op op = (literal.neg)?Op.NEG:Op.EMPTY
        InlineOperator op = InlineOperator.EMPTY // avoid the redundance: the negation is already in the literal

        new Expression(
           symbolicOperator: Operator.buildInterfaceOperator(op),
           compiledOperator: Operator.buildInterfaceOperator(op),
           inputLiterals: [literal]
        )
    }

    // literal formula, i.e. simple logic interface
    static Expression buildFromLiterals(List<ClassicLiteral> literals, InlineOperator op, List<Integer> params = []) {

        log.trace "creating Formula from Literals: "+(literals*.toString()).join(", ")

        Expression formula = new Expression()

        if (literals.size() > 1 || op == InlineOperator.CHOICE) {
            formula.symbolicOperator = Operator.buildInterfaceOperator(op, params)
            List<Operator> operators = []
            List<Expression> inputs = []
            for (ClassicLiteral literal in literals) {
                Expression f = Expression.build(literal)
                inputs << f
                operators << f.compiledOperator
            }

            // aggregate all operators of the inputs
            formula.compiledOperator = Operator.buildNaryOperator(operators, op, params)
            formula.inputExpressions = inputs

            // aggregate all atoms of the inputs
            formula.inputLiterals = literals
        } else if (op == InlineOperator.NEG) { // with negate, simply inverse the literal
            formula = Expression.build(literals[0].negate())
        } else { // with only one input, AND, OR becomes identity // TODO check the choice operator
            formula = Expression.build(literals[0])
        }

        formula
    }

    // unary formula
    static Expression build(Expression input, InlineOperator op, List<Integer> params = []) {

        log.trace "creating Formula from Formula: "+input.toString()

        new Expression(
           symbolicOperator: Operator.buildInterfaceOperator(op, params),
           compiledOperator: Operator.buildUnaryOperator(input.compiledOperator, op, params),
           inputExpressions: [input],
           inputLiterals: input.inputLiterals
        )
    }

    // binary formula
    static Expression build(Expression leftInput, Expression rightInput, InlineOperator op, List<Integer> params = []) {

        buildFromFormulas([leftInput, rightInput], op, params)

    }

    // n-ary formula
    static Expression buildFromFormulas(List<Expression> inputs, InlineOperator op, List<Integer> params = []) {

        log.trace("creating Formula from formulas: "+inputs+", with operations "+ op)
        if (op == InlineOperator.AND || op == InlineOperator.OR) { // TODO: check CHOICE operator
            if (inputs.size() == 1) // override the operator for just one input
                return inputs.get(0).clone()

            if (op == InlineOperator.AND) { // horizontal handling of AND
                List<Expression> andComponents = []
                for (input in inputs)
                    for (component in input.andComponents())
                        andComponents << component
                inputs = andComponents
            } else { // horizontal handling of OR
                List<Expression> orComponents = []
                for (input in inputs)
                    for (component in input.orComponents())
                        orComponents << component
                inputs = orComponents
            }
        }

        log.trace "creating Formula from Formulas: "+(inputs*.toString()).join(", ")

        Expression formula = new Expression()
        // aggregate all operators of the inputs
        List<Operator> operators = []
        // aggregate all atoms of the inputs
        List<ClassicLiteral> literals = []

        inputs.each() {
            operators << it.compiledOperator
            literals = literals - it.inputLiterals + it.inputLiterals
        }

        formula.symbolicOperator = Operator.buildInterfaceOperator(op, params)
        formula.compiledOperator = Operator.buildNaryOperator(operators, op, params)
        formula.inputExpressions = inputs
        formula.inputLiterals = literals

        formula
    }

    // build a formula from string versions of the terms (eg 010, 1X0), given a dictionary of atoms (eg. [a, b, c]
    // the result is e.g. (-a and b and -c) OR (a and c).
    static Expression buildFromTerms(List<String> stringTerms, List<Constant> atoms) {

        log.trace "creating Formula from Terms: " + (stringTerms.join(", ")) + " (with atom set: " + (atoms*.toString().join(", ")) + ")"

        List<Expression> terms = []
        for (stringTerm in stringTerms) {
          if (stringTerm.length() != atoms.size()) {
              log.error("I was expecting a term of size "+atoms.size()+" (for the atoms "+(atoms*.toString().join(", "))+")")
              return null
          }

          List<ClassicLiteral> conditions = []
          for (Integer i = 0; i < stringTerm.length(); i++) {
            if (stringTerm[i] == '0') {
                conditions << atoms[i].toLiteral().negate()
            } else if (stringTerm[i] == '1') {
                conditions << atoms[i].toLiteral()
            } // else "X" don't do anything
          }

          terms << Expression.buildFromLiterals(conditions, InlineOperator.AND)
        }

        return Expression.buildFromFormulas(terms, InlineOperator.OR)

    }

    ///////////////////////////////////////

    /**
     * disjoint the OR components of a formula
     * a OR b => (a), (b)
     * a AND (c OR b) => (a, c), (a, b)
     * @return logical equivalent list of OR formulas
     */
    List<Expression> disjoin() {
        log.trace("Disjoining formula: "+this.toString())

        if (symbolicOperator.op == InlineOperator.OR) {
            List<Expression> disjoints = []
            for (Expression formula in inputExpressions) {
                for (Expression disjoint in formula.disjoin())
                  if (!disjoints.contains(disjoint)) disjoints << disjoint // recursively disjoin
            }
            log.trace("Disjunction OR, union: "+disjoints)
            disjoints
        } else if (symbolicOperator.op == InlineOperator.AND) {
            List<List<Expression>> conjointParts = []
            for (Expression formula in inputExpressions) {
               conjointParts << formula.disjoin() // recursively disjoin
            }
            log.trace("Disjunction AND, cartesian product of "+conjointParts)
            conjointParts = conjointParts.combinations() // cartesian product of the disjoints

            List<Expression> disjoints = []
            for (List<Expression> formulas in conjointParts) {
                // remove duplicates
                List<Expression> noDuplicates = []
                for (formula in formulas) {
                    if (!noDuplicates.contains(formula)) noDuplicates.add(formula)
                }
                disjoints << Expression.buildFromFormulas(noDuplicates, InlineOperator.AND)
            }
            log.trace("creating conjoint subformulas: "+disjoints)
            disjoints
        } else {
            log.trace(".. no disjunction for "+this.toString()) // when it is not able to reduce
            return [this]
        }
    }

    List<Expression> andComponents() {
        if (symbolicOperator.op == InlineOperator.AND) {
            List<Expression> andComponents = []
            for (input in inputExpressions) {
                for (elem in input.andComponents())
                  andComponents << elem
            }
            return andComponents
        } else {
            return [this]
        }
    }

    Expression negate() {
       if (inputExpressions.size() == 0) { // negation of a literal
          return Expression.build(inputLiterals.get(0).negate())
       } else if (symbolicOperator.op == InlineOperator.NEG) { // negation of a formula // double negation
          if (inputExpressions.size() > 1) log.warn("I expected a unary input here:"+this)
          return inputExpressions.get(0).clone()
       } else return Expression.build(this, InlineOperator.NEG)
    }

    // apply the demorgan's laws on the negation down to the the literal level
    // the boolean parameter negation serves to transmit the negation value in the recursion
    Expression reduceNegation(boolean negation = false) {
        if (inputExpressions.size() == 0) { // we are at the literal level of the formula
            if (!negation) { // negation of a literal
                return this
            } else {
                return Expression.build(inputLiterals.get(0).negate())
            }
        } else { // we are at a higher level than literal
            if (!negation) {
                if (symbolicOperator.op == InlineOperator.NEG) {
                    if (inputExpressions.size() > 1) log.warn("I expected a unary input here:" + this)
                    return inputExpressions.get(0).reduceNegation(true)
                } else {
                    if (inputExpressions.size() > 0) {
                        List<Expression> newInputFormulas = []
                        for (formula in inputExpressions) {
                            newInputFormulas << formula.reduceNegation()
                        }
                        return Expression.buildFromFormulas(newInputFormulas, symbolicOperator.op, symbolicOperator.params)
                    }
                }
            } else {
                if (symbolicOperator.op == InlineOperator.NEG) {
                    if (inputExpressions.size() > 1) log.warn("I expected a unary input here:" + this)
                    return inputExpressions.get(0).clone()
                } else {
                    InlineOperator newOp
                    if (symbolicOperator.op == InlineOperator.OR) newOp = InlineOperator.AND        // De Morgan's law
                    else if (symbolicOperator.op == InlineOperator.AND) newOp = InlineOperator.OR
                    else {
                        log.warn("Operator " + symbolicOperator.op + " not implemented in the negation reduction ")
                        return null
                    }

                    List<Expression> newInputFormulas = []
                    for (formula in inputExpressions) {
                        newInputFormulas << formula.reduceNegation(true)
                    }
                    return Expression.buildFromFormulas(newInputFormulas, newOp)
                }
            }
        }
    }

    List<Expression> orComponents() {
        if (symbolicOperator.op == InlineOperator.OR) {
            List<Expression> orComponents = []
            for (input in inputExpressions) {
                for (elem in input.orComponents())
                    orComponents << elem
            }
            return orComponents
        } else {
            return [this]
        }
    }

    class CompoundRuleException extends Exception{}

    ClassicLiteral toLiteral() throws CompoundRuleException {
        if ((symbolicOperator.op == InlineOperator.EMPTY || symbolicOperator.op == InlineOperator.NEG)
             && inputLiterals.size() == 1) {
            return inputLiterals.get(0)
        }
        else {
            log.error("I was expecting a simple literal, not a compound formula as: "+this)
            throw new CompoundRuleException()
        }
    }

    //////////////////
    // Views
    //////////////////

    String toString() {
        String output

        output = symbolicOperator.opToString()

        Boolean parenthesis = (output != "")

        if (parenthesis) output += "("

        if (inputExpressions.size() > 0) {
          output += inputExpressions.join(',')
        } else {
          if (inputLiterals.size() > 1)
             log.fatal("In this atomic formula there are more atoms !?!")

          output += inputLiterals[0].toString()
        }

        if (parenthesis) output += ")"
        output
    }

    String toASP(Boolean head = false) {
        String output = ""

        Integer n = inputExpressions.size()
        
        switch(symbolicOperator.op) {
            case InlineOperator.EMPTY:
                if (n > 1) return "// NEG: there should be only an atom"
                break
            case InlineOperator.AND:
                if (head) output += n+"{" 
                break
            case InlineOperator.OR:
                output += "1{"
                break
            case InlineOperator.CHOICE:
                if (symbolicOperator.params[0] != 0)
                    output += symbolicOperator.params[0]  
                output += "{"
                break
            case InlineOperator.NEG:
                if (n > 1) return "// NEG: there should be only an atom"
                output += "-"
                break
            case InlineOperator.IMPLIES:
                return "// ASP does not allow for internal implications"
                break
            case InlineOperator.XOR:
                output += "1{"
                break
        }

        if (inputExpressions.size() > 0) {
          output += inputExpressions.join(',')
        } else {
          if (inputLiterals.size() > 1)
             log.fatal("In this atomic formula there are more atoms !?!")

          output += inputLiterals[0].toString()
        }

        switch(symbolicOperator.op) {
            case InlineOperator.EMPTY:
                break
            case InlineOperator.AND:
                if (head) output += "}"+n
                break
            case InlineOperator.OR:
                output += "}"
                break
            case InlineOperator.CHOICE:
                output += "}"
                if (symbolicOperator.params[1] != n) 
                  output += symbolicOperator.params[1]
                break
            case InlineOperator.NEG:
                break
            case InlineOperator.XOR:
                output += "}1"
                break
        }

        output
    }

}
