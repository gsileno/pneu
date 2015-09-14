package org.leibnizcenter.pneu.builders

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

@Log4j
class PN2json extends PN2abstract {

    static String innerConversion(Net net, Integer level = 1, List<Net> alreadyConvertedNets = []) {

        List<Place> placeList = net.placeList
        List<Transition> transitionList = net.transitionList
        List<Arc> arcList = net.arcList
        List<Net> subNets = net.subNets

        String code = tab(level-1) + "{"

        // for checking purposes, the id should have been generated in the recursion in the subnets

        if (net.function) {
            if (net.function.id) {
                code += " \"id\": \"" + net.function.id + "\", "
            }
            if (net.function.isCluster()) {
                code += "\"cluster\": true, "
            }
        }
        code += "\n"

        if (placeList.size() > 0) {
            code += tab(level) + "\"places\": [\n"
            for (pl in placeList) {
                code += tab(level + 1) + "{\"id\": \""+pl.id+"\", "
                if (pl.isLink()) {
                    code += "\"link\": true"
                } else if (pl.label() != "") {
                    code += "\"label\": \""+pl.label()+"\""
                } else {
                    code = code[0..-3]
                }

                if (pl.marking.size() > 0) {
                    code += ", \"marking\": ["
                    for (token in pl.marking) {
                        code += "\""+token.label()+"\", "
                    }
                    code = code[0..-3]
                    code += "]"
                }

                code += "},\n"
            }

            code = code[0..-3] + "\n" + tab(level) + "],\n"
        }

        if (transitionList.size() > 0) {
            code += tab(level) + "\"transitions\": [\n"

            List<Transition> orderedTransitionList = transitionList.collect().sort() {it.id}

            for (tr in orderedTransitionList) {

                code += tab(level + 1) + "{\"id\": \""+tr.id+"\", "
                if (tr.isLink()) {
                    code += "\"link\": true"
                } else if (tr.label() != "") {
                    code += "\"label\": \""+tr.label()+"\""
                } else {
                    code = code[0..-3]
                }
                code += "},\n"
            }
            code = code[0..-3] + "\n" + tab(level) + "],\n"
        }

        if (subNets.size() > 0) {
            code += tab(level) + "\"subnets\": [\n"
            for (subNet in net.subNets) {
                if (!alreadyConvertedNets.contains(subNet)) {
                    code += innerConversion(subNet, level + 2, alreadyConvertedNets) + ""
                    code = code[0..-3] + ",\n"
                    alreadyConvertedNets << subNet
                } else {
                    code += tab(level+1)+"{ \"id\": \""+net.function.id+"\"},\n"
                }
            }
            code = code[0..-3] + "\n" + tab(level) + "],\n"
        }

        // the arcs have to be generated later, as we have to create all the places and transitions

        if (arcList.size() > 0) {
            code += tab(level) + "\"arcs\": [\n"

            Integer i = 0
            for (arc in arcList) {
                code += tab(level+1) + "{ \"source\": \""+arc.source.id+"\", " + "\"target\": \""+arc.target.id+"\", "
                if (arc.type == ArcType.INHIBITOR || arc.type == ArcType.RESET || arc.type == ArcType.LINK)
                    code += "\"type\": \""+arc.type.toString()+"\", "
                if (arc.weight > 1) {
                    code += "\"weight\": " + arc.weight + ","
                }

                code = code[0..-3] + " },\n"
            }

            code = code[0..-3] + "\n" + tab(level) + "],\n"
        }

        code = code[0..-3] + "\n" + tab(level-1) + "} \n"
        code
    }

    static String convert(Net net) {
        innerConversion(net)
    }

}

