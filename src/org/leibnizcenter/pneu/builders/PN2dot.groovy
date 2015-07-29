package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

/**
 * Created by giovanni on 4/3/15.
 */
class PN2dot {

    static String simpleConversion(Net net, boolean showId = false) {

        List<Place> placeList = net.getAllPlaces()
        List<Transition> transitionList = net.getAllTransitions()
        List<Arc> arcList = net.getAllArcs()

        String code = ""

        code += "digraph G {\n  rankdir=\"LR\";\n"

        if (placeList.size() > 0) code += headerPlaces

        Integer i

        i = 0
        placeList.each { pl ->
            if (!pl.id) pl.id = "_p"+i;
            code += "    "+pl.id+" [label=\""+pl.toMinString()+"\"];\n"
            i++
        }

        if (placeList.size() > 0) code += "  } \n"

        if (transitionList.size() > 0) {
            code += headerTransitions
        }

        i = 0
        transitionList.each { tr ->
            if (!tr.id) tr.id = "_t"+i;
            code += "    "+tr.id+" [label=\""+tr.toString()+"\"];\n"
            i++
        }

        if (transitionList.size() > 0) code += "  } \n"

        arcList.each { arc ->
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

