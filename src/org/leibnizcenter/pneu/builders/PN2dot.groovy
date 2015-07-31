package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class PN2dot {

    static private String tab(Integer level = 1, String c = " ") {
        String output = ""
        for (int i = 0; i < level * 2; i++)
            output += c
        output
    }

    static String simpleInnerConversion(Net net, boolean showId = false, Integer level = 1, String prefix = "") {

        List<Place> placeList = net.placeList
        List<Transition> transitionList = net.transitionList
        List<Arc> arcList = net.arcList

        String code = ""

        if (placeList.size() > 0) code += "\n"+headerPlaces(level)

        Integer i

        i = 0
        placeList.each { pl ->
            if (!pl.id) pl.id = "_p"+i+((prefix == "")?"":"_"+prefix)
            if (!pl.isLink()) {
                code += tab(level + 1) + pl.id + " [label=\"" + pl.toMinString() + "\"] ;\n"
            } else {
                code += tab(level+1) + pl.id + " [label=\"\",height=.1,width=.1,style=filled,width=.1,color=black] ;\n"
            }
            i++
        }

        if (placeList.size() > 0) code += tab(level)+"} \n"

        if (transitionList.size() > 0) code += "\n"+headerTransitions(level)

        i = 0
        transitionList.each { tr ->
            if (!tr.id) tr.id = "_t"+i+((prefix == "")?"":"_"+prefix)
            if (!tr.isLink()) {
              code += tab(level+1)+tr.id+" [label=\""+tr.toString()+"\"] ;\n"
            } else {
              code += tab(level+1)+tr.id+" [label=\"\",height=.1,width=.1,style=filled,width=.1,color=black] ;\n"
            }
            i++
        }

        if (transitionList.size() > 0) code += tab(level)+"} \n"

        i = 0
        for (subNet in net.subNets) {
            code += "\n"+tab(level)+"subgraph cluster${prefix}_${i} {\n"
            code += tab(level+1)+"color=lightgray ;\n"
            code += simpleInnerConversion(subNet, showId, level+1, prefix+i.toString())
            code += tab(level)+"}\n"
            i++
        }

        // the arcs have to be generated later, as we have to create all the places and transitions

        if (arcList.size() > 0) code += "\n"

        arcList.each { arc ->
            code += tab(level)+arc.source.id // printName(arc.source.id)

            code +=" -> "
            code += arc.target.id // printName(arc.target.id)
            code += " ["
            if (arc.type == ArcType.INHIBITOR)
                code += "arrowhead=dot"
            else if (arc.type == ArcType.RESET)
                code += "style=dashed arrowhead=none"
            else if (arc.type == ArcType.LINK)
                code += "arrowhead=none"

            if (arc.weight > 1) {
                if (arc.type != ArcType.NORMAL) code += " "
                code += "label=\""+arc.weight+"\""
            }
            code += "]"
            code += " ;\n"
        }

        code

    }

    static String simpleConversion(Net net, boolean showId = false) {

        String code = ""

        code += "digraph G {\n  rankdir=\"LR\";\n"

        code += simpleInnerConversion(net, showId)

        code += "}\n"

        code
    }

    // preambles

    static String headerPlaces(Integer level = 0) {
        return tab(level)+"subgraph place {\n" +tab(level+1)+"node [shape=circle,fixedsize=true,width=.5];\n"
    }

    static String headerTransitions(Integer level = 0) {
        return tab(level)+"subgraph transitions {\n" + tab(level+1)+"node [shape=rect,height=.5,width=.5];\n"
    }

}

