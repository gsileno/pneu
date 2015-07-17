package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.components.petrinet.*

interface PN2ASP {

    static String buildPlace(Place p);
    static String buildTransition(Transition t);
    static String buildArc(Arc a);
    static String buildTime(Integer maxSteps);
    static String buildTokens(Integer nTokens);
    static String buildEnablingRules();
    static String buildFiringRules();
    static String buildProductionRules();

}
