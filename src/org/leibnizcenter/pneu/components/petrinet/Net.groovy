package org.leibnizcenter.pneu.components.petrinet

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.graphics.Grid

@Log4j
abstract class Net {

    // the function node synthetize the function of the net (for subnets)
    Node function

    List<Transition> transitionList = []
    List<Place> placeList = []
    List<Arc> arcList = []
    List<Net> subNets = []
    List<Net> parents = []

    // this serves for graphical purposes
    Grid grid

    // for operations, these nodes are events/transitions,
    // for expressions, these are situations/places
    List<Node> inputs = []
    List<Node> outputs = []

    void include(Net net, Integer xPos = 0, Integer yPos = 0) {
        if (xPos != 0 || yPos != 0) {
            for (p in net.placeList) p.position.traslate(xPos, yPos)
            for (t in net.transitionList) t.position.traslate(xPos, yPos)
        }

        if (!net.parents.contains(this))
            net.parents << this
        else {
            log.warn("Parent net already included here")
        }

        if (!subNets.contains(net)) {
            subNets << net
        } else {
            log.warn("Child net already included here")
        }

    }

    List<Place> getAllPlaces() {
        List<Place> allPlaces = []
        allPlaces += placeList
        for (subnet in subNets) {
            allPlaces += subnet.getAllPlaces()
        }
        allPlaces
    }

    List<Transition> getAllTransitions() {
        List<Transition> allTransitions = []
        allTransitions += transitionList
        for (subnet in subNets) {
            allTransitions += subnet.getAllTransitions()
        }
        allTransitions
    }

    List<Arc> getAllArcs() {
        List<Arc> allArcs = []
        allArcs += arcList
        for (subnet in subNets) {
            allArcs += subnet.getAllArcs()
        }
        allArcs
    }

    List<Net> getAllNets() {
        List<Net> allNets = []
        allNets += this
        for (subnet in subNets) {
            allNets += subnet.getAllNets()
        }
        allNets
    }

    void setupGrid(Integer inputDotGranularity = 1, Integer outputDotGranularity = 33) {
        if (!grid) grid = new Grid()
        grid.setInputDotGranularity(inputDotGranularity)
        grid.setOutputDotGranularity(outputDotGranularity)

        for (p in placeList) {
            if (p.position)
                grid.testMinMax(p.position.x, p.position.y)
        }
        for (t in transitionList) {
            if (t.position)
                grid.testMinMax(t.position.x, t.position.y)
        }
    }

    private static String tab(Integer level = 0, String c = " ") {
        String output = ""
        for (int i = 0; i < level * 4; i++)
            output += c
        output
    }

    String toString() {
        return "Net@"+hashCode()
    }

    String toLog(Integer level = 0) {
        String output = ""

        output += tab(level) + "Net@"+hashCode()+ "\n"
        if (function) {
            output += tab(level) + "function: " + function.toString()
            if (isPlaceLike())
                output += ", like a place"
            else if (isTransitionLike()) {
                output += ", like a transition"
            }
            output += "\n"
        }
        output += tab(level) + "inputs: " + inputs + "\n"
        output += tab(level) + "outputs: " + outputs + "\n"
        output += tab(level) + "=====\n"
        output += tab(level) + "places: " + placeList + "\n"
        output += tab(level) + "transitions: " + transitionList + "\n"

        output += tab(level) + "arcs: " + arcList + "\n"
        output += tab(level) + "=====\n"
        output += tab(level) + "parents: (" + parents.size() + "): " + parents + "\n"
        output += tab(level) + "subnets: (" + subNets.size() + ")" + "\n"
        for (subNet in subNets) {
            output += tab(level) + "--------\n"
            output += subNet.toLog(level + 1)
        }
        if (subNets.size() > 0) output += tab(level) + "--------\n"
        output
    }

    Boolean isPlaceLike() {
        if (!function) return false
        Place.isAssignableFrom(function.class)
    }

    Boolean isTransitionLike() {
        if (!function) return false
        Transition.isAssignableFrom(function.class)
    }

    void print() {
        println toLog()
    }

    // to deep clone the net
    abstract Net minimalClone(Map<Net, Net> sourceCloneMap)

    // to remember the original name of the file
    String sourceName
    String sourceFile
}
