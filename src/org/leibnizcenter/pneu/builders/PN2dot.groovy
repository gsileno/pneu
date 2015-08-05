package org.leibnizcenter.pneu.builders

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

@Log4j
class PN2dot {

    static private String tab(Integer level = 1, String c = " ") {
        String output = ""
        for (int i = 0; i < level * 2; i++)
            output += c
        output
    }

    static String simpleInnerConversion(Net net, boolean showId = false, Integer level = 1, String prefix = "", List<Net> alreadyConvertedNets = []) {

        List<Place> placeList = net.placeList
        List<Transition> transitionList = net.transitionList
        List<Arc> arcList = net.arcList

        String code = ""

        if (placeList.size() > 0) code += "\n" + headerPlaces(level)

        Integer i

        i = 0
        placeList.each { pl ->
            if (!pl.id) {
                pl.id = "_p" + i + ((prefix == "") ? "" : "_" + prefix)
                log.trace("assigning id ${pl.id} to ${pl}")
            } else {
                log.trace("existing id of ${pl}: ${pl.id}")
            }
            if (!pl.isLink()) {
                code += tab(level + 1) + pl.id + " [label=\"" + pl.toMinString() + "\"] ;\n"
            } else {
                code += tab(level + 1) + pl.id + " [label=\"\",height=.1,width=.1,style=filled,width=.1,color=black] ;\n"
            }
            i++
        }

        if (placeList.size() > 0) code += tab(level) + "} \n"

        if (transitionList.size() > 0) code += "\n" + headerTransitions(level)

        i = 0
        transitionList.each { tr ->
            if (!tr.id) {
                tr.id = "_t" + i + ((prefix == "") ? "" : "_" + prefix)
                log.trace("assigning id ${tr.id} to ${tr}")
            } else {
                log.trace("existing id of ${tr}: ${tr.id}")
            }

            if (!tr.isLink()) {
                code += tab(level + 1) + tr.id + " [label=\"" + tr.toString() + "\"] ;\n"
            } else {
                code += tab(level + 1) + tr.id + " [label=\"\",height=.1,width=.1,style=filled,width=.1,color=black] ;\n"
            }
            i++
        }

        if (transitionList.size() > 0) code += tab(level) + "} \n"

        i = 0
        for (subNet in net.subNets) {

            if (!alreadyConvertedNets.contains(subNet)) {
                if (subNet.function.isCluster()) {
                    code += "\n" + tab(level) + "subgraph cluster${prefix}_${i} {\n"
                    code += tab(level + 1) + "label=\"" + subNet.function.toString() + "\" ;\n"
                    if (subNet.hasPlaceLikeFunction())
                        code += tab(level + 1) + "color=lightblue ;\n"
                    else if (subNet.hasTransitionLikeFunction())
                        code += tab(level + 1) + "color=darkred ;\n"
                    else
                        code += tab(level + 1) + "color=lightgray ;\n"
                } else {
                    code += "\n" + tab(level) + "subgraph {\n"  // for simple subgraph you don't have to put the prefix!
                }
                code += simpleInnerConversion(subNet, showId, level + 1, prefix + i.toString(), alreadyConvertedNets)
                code += tab(level) + "}\n"
                i++

                alreadyConvertedNets << subNet
            }
        }

        // the arcs have to be generated later, as we have to create all the places and transitions

        if (arcList.size() > 0) code += "\n"

        i = 0
        arcList.each { arc ->
            if (!arc.source.id) {
                arc.source.id = "_ext" + i + ((prefix == "") ? "" : "_" + prefix)
                log.trace("assigning id ${arc.source.id} to ${arc.source}")
            } else {
                log.trace("existing id of ${arc.source}: ${arc.source.id}")
            }

            if (!arc.target.id) {
                arc.target.id = "_ext" + i + ((prefix == "") ? "" : "_" + prefix)
                log.trace("assigning id ${arc.target.id} to ${arc.target}")
            } else {
                log.trace("existing id of ${arc.target}: ${arc.target.id}")
            }

            code += tab(level) + arc.source.id // printName(arc.source.id)

            code += " -> "
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
                code += "label=\"" + arc.weight + "\""
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
        return tab(level) + "subgraph place {\n" + tab(level + 1) + "node [shape=circle,fixedsize=true,width=.5];\n"
    }

    static String headerTransitions(Integer level = 0) {
        return tab(level) + "subgraph transitions {\n" + tab(level + 1) + "node [shape=rect,height=.5,width=.5];\n"
    }

}

