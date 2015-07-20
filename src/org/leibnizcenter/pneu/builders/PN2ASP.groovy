package org.leibnizcenter.pneu.builders

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.*

@Log4j
class PN2ASP {

    static String buildAxioms() {
        return """% simple Event Calculus axioms adjusted for petri nets
#domain fluent(F), time(N), time(N1), time(N2), time(N3).
#domain place(P), trans(T).
holdsAt(F, P, N) :- initially(F, P), not clipped(0, F, P, N).
holdsAt(F, P, N2) :- fires(T, N1), initiates(T, F, P, N1), N1 < N2, not clipped(N1, F, P, N2).
clipped(N1, F, P, N2) :- fires(T, N), N1 <= N, N < N2, terminates(T, F, P, N).

"""
    }

    static String buildBinaryPetriNetFluent() {
        return """% for basic petri nets we account only one color
fluent(filled).

"""
    }

    // type places
    static String buildTypePlaces(List<Place> placeList) {
        String output = ""

        if (placeList.size() > 0) {
            output += "% place entities\n"
            for (p in placeList) {
                output += "place(${p.id}). "
            }
            output += "\n\n"
        }
        output
    }

    // say if a place is initially filled
    static String buildInitialMarkingPlaces(List<Place> placeList) {
        String output = ""

        if (placeList.size() > 0) {
            output += "% set the initial marking for places\n"
            for (p in placeList) {
                if (p.marking.size() >= 1) {
                    if (p.marking.size() > 1) log.warn("This is not a binary petri net as expected")
                    output += "initially(filled, ${p.id}).\n"
                }
            }
            output += "\n"
        }
        output
    }

    // type a transition
    static String buildTypeTransitions(List<Transition> transitionList) {

        String output = ""

        if (transitionList.size() > 0) {
            output += "% transition entities\n"
            for (t in transitionList) {
                output += "trans(${t.id}). "
            }
            output += "\n\n"
        }

        output
    }

    // define the condition of a transition for being enabled
    // i.e. all inputs place must be filled
    static String buildEnablingConditionTransitions(List<Transition> transitionList) {

        String output = ""

        if (transitionList.size() > 0) {
            output += "% define the condition of a transition for being enabled\n"
            for (t in transitionList) {
                if (t.inputs.size() > 0) {
                    output += "enabled(${t.id}, N) :- "
                    for (a in t.inputs) {
                        output += "holdsAt(filled, ${a.source.id}, N), "
                    }
                    output = output[0..-3] + ".\n" // remove last comma and add the dot
                }

            }
            output += "\n"
        }

        output
    }

    // execution semantics:
    // all enabled transition may fire or not..
    static String buildExecutionSemantics() {

        String output = ""

        output += "% all enabled transition may fire or not..\n"
        output += "{fires(T, N)} :- enabled(T, N).\n"

        output + "\n"
    }

    // execution constraint (A): at least a transition has to fire..
    // it is impossible that for each transition, it does not fire.
    static String buildExecutionConstraintAtLeastOneTransitionFires(List<Transition> transitionList) {

        String output = ""

        if (transitionList.size() > 0) {
            output += "% at least a transition has to fire at each step..\n"

            output += ":- "
            for (t in transitionList) {
                output += "not fires(${t.id}, N), "
            }
            output = output[0..-3] + ".\n\n" // remove last comma and add the dot
        }

        output
    }

    // execution constraint (B): only one a transition has to fire..
    // it is impossible that two different transitions fires at the same time
    static String buildExecutionConstraintOnlyOneTransitionFires(List<Transition> transitionList) {

        String output = ""

        if (transitionList.size() > 0) {
            output += "% only one transition has to fire at each step..\n"
            output += ":- fires(T1, N), fires(T2, N), T1 != T2, trans(T1), trans(T2).\n\n"
        }

        output
    }

    static String buildTransitionProductionRules(List<Transition> transitionList) {
        String output = ""

        if (transitionList.size() > 0) {
            output += "% production rules per transition\n"

            for (t in transitionList) {
                for (a in t.outputs) {
                    output += "initiates(${t.id}, filled, ${a.target.id}, N) :- enabled(${t.id}, N).\n"
                }
            }

            output += "\n"
        }

        output
    }

    static String buildTransitionConsumptionRules(List<Transition> transitionList) {
        String output = ""

        if (transitionList.size() > 0) {
            output += "% consumption rules per transition\n"

            for (t in transitionList) {
                for (a in t.inputs) {
                    output += "terminates(${t.id}, filled, ${a.source.id}, N) :- enabled(${t.id}, N).\n"
                }
            }
            output += "\n"
        }

        output
    }

    static String buildPlaceConsumptionConstraints(List<Place> placeList) {
        String output = ""

        if (placeList.size() > 0) {
            output += "% for each place, you cannot consume more than the only available token\n"

            for (p in placeList) {

                if (p.outputs.size() > 1) {
                    output += ":- "
                    for (a in p.outputs) {
                        output += "fires(${a.target.id}, N), terminates(${a.target.id}, filled, ${p.id}, N), "
                    }
                    output += "holdsAt(filled, ${p.id}, N).\n"
                }
            }
            output += "\n"

        }

        output
    }

    static String buildTime(Integer maxSteps) {
        return "% simulation time range\ntime(0..$maxSteps).\n"+"\n"
    }

    static String buildSimulationModel(Net net) {

        String output = ""

        output += buildAxioms()
        output += buildBinaryPetriNetFluent()
        output += buildTypePlaces(net.placeList)
        output += buildTypeTransitions(net.transitionList)
        output += buildInitialMarkingPlaces(net.placeList)
        output += buildEnablingConditionTransitions(net.transitionList)
        output += buildTransitionProductionRules(net.transitionList)
        output += buildTransitionConsumptionRules(net.transitionList)
        output += buildPlaceConsumptionConstraints(net.placeList)
        output += buildExecutionSemantics()

        output

    }

}
