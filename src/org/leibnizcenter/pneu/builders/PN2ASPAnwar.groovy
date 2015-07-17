package org.leibnizcenter.pneu.builders

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

// from Anwar2013

@Log4j
class PN2ASPAnwar implements PN2ASP {

    static String buildPlace(Place p) {
        String output = ""

        output += "place(${p.id})." // f1
        if (p.marking.size() > 0) {
            output += "holds(${p.id}, ${p.marking.size()}, 0)" // i1
        }
        output
    }

    static String buildTransition(Transition t) {
        return "trans(${t.id})." // f2
    }

    static String buildArc(Arc a) {
        String output = ""
        if (a.source.class == Place)
            output += "ptarc" // f3
        else if (a.source.class == Transition)
            output += "tparc" // f4
        else
            { log.warn("You should not be here."); return null }
        output += "($a.source.id,$a.target.id,$a.weight)."
        output
    }

    static String buildTime(Integer maxSteps) {
        return "time(0..$maxSteps)." // f5
    }


    static String buildTokens(Integer nTokens) {
        return "num($nTokens)." // x1
    }

    static String buildEnablingRules() {
        String output = ""
        output += "notenabled(T, TS) :- ptarc(P, T, N), holds(P, Q, TS), Q < N, place(P), trans(T), time(TS), num(N), num(Q)." // e1
        output += "enabled(T,TS) :- trans(T), time(TS), not notenabled(T, TS)." // e2
        output
    }

    static String buildFiringRules() {
        String output = ""
        output += "{fires(T, TS)} :- enabled(T, TS), trans(T), time(TS)." // a1: multiple transitions may occur once
        output += "consumesmore(P, TS) :- holds(P, Q, TS), tot_decr(P, Q1, TS), Q1 > Q." // a2: these serve to avoid over consumption
        output += "consumesmore :- consumesmore(P,TS)." // a3
        output += ":- consumesmore." // a4
        output
    }

    static String buildProductionRules() {
        String output = ""
        output += "add(P, Q, T, TS) :- fires(T, TS), tparc(T, P, Q), time(TS)." // r1: production of tokesn
        output += "del(P, Q, T, TS) :- fires(T, TS), ptarc(P, T, Q), time(TS)." // r2: removal of tokens
        output += "tot_incr(P, QQ, TS) :- QQ=#sum[add(P,Q,T,TS)=Q:num(Q):trans(T)], time(TS), num(QQ), place(P)."
        output += "tot_decr(P, QQ, TS) :- QQ=#sum[del(P,Q,T,TS)=Q:num(Q):trans(T)], time(TS), num(QQ), place(P)."
        output += "holds(P,Q,TS+1) :- holds(P,Q1,TS), tot_incr(P,Q2,TS), time(TS+1), tot_decr(P,Q3,TS), Q=Q1+Q2-Q3, place(P), num(Q;Q1;Q2;Q3), time(TS)."
        output
    }
}
