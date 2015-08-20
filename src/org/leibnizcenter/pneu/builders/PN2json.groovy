package org.leibnizcenter.pneu.builders

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

@Log4j
class PN2json {

    static private String tab(Integer level = 1, String c = " ") {
        String output = ""
        for (int i = 0; i < level * 2; i++)
            output += c
        output
    }

    static String simpleInnerConversion(Net net, Integer level = 1, String prefix = "", List<Net> alreadyConvertedNets = []) {

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

            Integer i = 0
            for (pl in placeList) {

                if (!pl.id) {
                    pl.id = "_p" + i + ((prefix == "") ? "" : "_" + prefix)
                    log.trace("assigning id ${pl.id} to ${pl}")
                } else {
                    log.trace("existing id of ${pl}: ${pl.id}")
                }

                code += tab(level + 1) + "{\"id\": \""+pl.id+"\", "
                if (pl.isLink()) {
                    code += "\"link\": true"
                } else {
                    code += "\"label\": \""+pl.toMinString()+"\""
                }
                i++

                code += "},\n"

            }

            code = code[0..-3] + "\n" + tab(level) + "],\n"
        }

        if (transitionList.size() > 0) {
            code += tab(level) + "\"transitions\": [\n"

            Integer i = 0
            for (tr in transitionList) {
                if (!tr.id) {
                    tr.id = "_t" + i + ((prefix == "") ? "" : "_" + prefix)
                    log.trace("assigning id ${tr.id} to ${tr}")
                } else {
                    log.trace("existing id of ${tr}: ${tr.id}")
                }

                code += tab(level + 1) + "{\"id\": \""+tr.id+"\", "
                if (tr.isLink()) {
                    code += "\"link\": true"
                } else {
                    code += "\"label\": \""+tr.toString()+"\""
                }
                i++

                code += "},\n"
            }
            code = code[0..-3] + "\n" + tab(level) + "],\n"
        }

        if (subNets.size() > 0) {
            code += tab(level) + "\"subnets\": [\n"
            Integer i = 0
            for (subNet in net.subNets) {
                if (!alreadyConvertedNets.contains(subNet)) {
                    log.trace("i $i, prefix $prefix")
                    if (!subNet.function.id) {
                        subNet.function.id = "_net" + ((prefix == "") ? i : "_" + prefix + i)
                        log.trace("assigning id ${subNet.function.id} to ${subNet}")
                    } else {
                        log.trace("existing id of ${subNet}: ${subNet.function.id}")
                    }

                    code += simpleInnerConversion(subNet, level + 2, prefix + i.toString(), alreadyConvertedNets) + ""
                    code = code[0..-3] + ",\n"
                    i++
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

    static String simpleConversion(Net net) {

        String code = ""
        code += simpleInnerConversion(net)
        code
    }

    // preambles

}

