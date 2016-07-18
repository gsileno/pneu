package pneu.builders

import groovy.util.logging.Log4j
import pneu.components.petrinet.*

@Log4j
class PN2log extends PN2abstract {

    static String innerConversion(Net net, Integer level = 1) {
        String output = ""

        output += tab(level) + "Net@"+hashCode()+ "\n"
        if (net.function) {
            output += tab(level) + "function: " + net.function.toString()
            if (net.isPlaceLike())
                output += ", like a place"
            else if (net.isTransitionLike()) {
                output += ", like a transition"
            }
            output += "\n"
        }
        output += tab(level) + "inputs: " + net.inputs + "\n"
        output += tab(level) + "outputs: " + net.outputs + "\n"
        output += tab(level) + "=====\n"
        output += tab(level) + "places: " + net.placeList + "\n"

        List<Transition> orderedTransitionList = net.transitionList.collect().sort() {it.id}

        output += tab(level) + "transitions: " + orderedTransitionList + "\n"

        output += tab(level) + "arcs: " + net.arcList + "\n"
        output += tab(level) + "=====\n"
        output += tab(level) + "parents: (" + net.parents.size() + ") " + net.parents + "\n"
        output += tab(level) + "subnets: (" + net.subNets.size() + ")" + "\n"
        for (subNet in net.subNets) {
            output += tab(level) + "--------\n"
            output += innerConversion(subNet, level + 1)
        }
        if (net.subNets.size() > 0) output += tab(level) + "--------\n"
        output
    }

    static String convert(Net net) {
        innerConversion(net)
    }

}

