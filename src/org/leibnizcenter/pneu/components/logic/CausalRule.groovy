package org.leibnizcenter.pneu.components.logic
import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Log4j

/**
 * Represents a relation of the type [premises -> conclusions] to be interpreted as: [reasons -> conclusion]
 **/

@Log4j @EqualsAndHashCode @AutoClone
public class CausalRule {

    Expression consequent
    Expression antecedent

    // TODO: this should be metadata
    String identifier // the identifier of the rule
    String comment    // a human readable description of the rule
    Integer pos       // temporal/ordering position.


    //////////////////
    // Views
    //////////////////

    String toString() {
        String output
        output = antecedent.toString() + " => " + consequent.toString()
        if (identifier) { output += " ($identifier)" }
        output
    }

}