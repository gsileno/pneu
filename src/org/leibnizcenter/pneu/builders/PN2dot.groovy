package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net

/**
 * Created by giovanni on 4/3/15.
 */
class PN2dot {

    static String printName(String name) {
        return "\"$name\""
    }

    static String simpleConversion(Net net, boolean showId = false) {

        String code = ""

        code += "digraph G {\n  rankdir=\"LR\";\n"

        if (net.placeList.size() > 0) code += headerPlaces

        net.placeList.each { pl ->
            code += "    "+pl.id+" [label=\""+pl.label(showId)+"\"];\n"
        }

        if (net.placeList.size() > 0) code += "  } \n"

        if (net.transitionList.size() > 0) {
            code += headerTransitions
        }

        net.transitionList.each { tr ->
            code += "    "+tr.id+" [label=\""+tr.label(showId)+"\"];\n"
        }

        if (net.transitionList.size() > 0) code += "  } \n"

        net.arcList.each { arc ->
            code += "  "+arc.source.id // printName(arc.source.id)

            code +=" -> "
            code += arc.target.id // printName(arc.target.id)
            code += " ["
            if (arc.type == ArcType.INHIBITOR)
                code += "arrowhead=dot"
            else if (arc.type == ArcType.RESET)
                code += "style=dashed arrowhead=none"
            if (arc.weight > 1) {
                if (arc.type != ArcType.NORMAL) code += " "
                code += "label=\""+arc.weight+"\""
            }
            code += "]"
            code += " ;\n"
        }

        code += "}\n"

        code
    }

    // preambles

    static String headerPlaces = '''  subgraph place {
    graph [shape=circle,color=gray];
    node [shape=circle,fixedsize=true,width=.5];
'''

    static String headerTransitions = '''  subgraph transitions {
    node [shape=rect,height=.5,width=.5];
'''
}

