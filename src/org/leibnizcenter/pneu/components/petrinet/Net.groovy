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
        output += tab(level) + "parents: (" + parents.size() + ") " + parents + "\n"
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

    // to check whether two nets share the same elements (places, transitions, arcs and subnets)
    // parent nets are NOT considered
    static Boolean compare(Net n1, Net n2) {

        // if they are the same object return true
        if (n1 == n2) return true

        // if they have a different number of places, transitions or arcs they are not the same
        if (n1.placeList.size() != n2.placeList.size()) { return false }
        if (n1.transitionList.size() != n2.transitionList.size()) { return false }
        if (n1.arcList.size() != n2.arcList.size()) { return false }
        if (n1.subNets.size() != n2.subNets.size()) { return false }

        // check all places
        for (p1 in n1.placeList) {
            Boolean found = false
            for (p2 in n2.placeList) {
                if (p1.class.compare(p1, p2)) {
                    found = true
                    break
                }
            }
            if (!found) return false
        }

        // check all transitions
        for (t1 in n1.transitionList) {
            Boolean found = false
            for (t2 in n2.transitionList) {
                if (t1.class.compare(t1, t2)) {
                    found = true
                    break
                }
            }
            if (!found) return false
        }

        // check all arcs
        for (a1 in n1.arcList) {
            Boolean found = false
            for (a2 in n2.arcList) {
                if (Arc.compare(a1, a2)) {
                    found = true
                    break
                }
            }
            if (!found) return false
        }

        // check all subnets
        for (sub1 in n1.subNets) {
            Boolean found = false
            for (sub2 in n2.subNets) {
                if (compare(sub1, sub2)) {
                    found = true
                    break
                }
            }
            if (!found) return false
        }

        return true
    }

    //////////////////////////////////////////////////////
    // helpers for creating elements inside the net
    //////////////////////////////////////////////////////

    abstract Transition createTransition(String label)
    abstract Place createPlace(String label)

    Transition propagateTransition(Transition source) {
        Transition target = source.clone()
        transitionList << target
        createBridge(source, target)
        target
    }

    void createArc(Place p, Transition t) {
        if (!placeList.contains(p) || !transitionList.contains(t)) {
            throw new RuntimeException("Error: this net does not contain the place/transition to bridge")
        }
        arcList << Arc.buildArc(p, t)
    }

    void createArc(Transition t, Place p) {
        if (!placeList.contains(p) || !transitionList.contains(t)) {
            throw new RuntimeException("Error: this net does not contain the transition/place to bridge")
        }
        arcList << Arc.buildArc(t, p)
    }

    void createBridge(Place p1, Transition tBridge, Place p2) {
        if (!placeList.contains(p1) || !placeList.contains(p2) || !transitionList.contains(tBridge)) {
            throw new RuntimeException("Error: this net does not contain the place(s)/transition to bridge")
        }
        arcList += Arc.buildArcs(p1, tBridge, p2)
    }

    void createBridge(Place p1, Place p2) {
        if (!placeList.contains(p1) || !placeList.contains(p2)) {
            throw new RuntimeException("Error: this net does not contain the place(s) to bridge")
        }
        Transition tBridge = createTransition()
        arcList += Arc.buildArcs(p1, tBridge, p2)
    }

    void createBridge(Transition t1, Place pBridge, Transition t2) {
        if (!transitionList.contains(t1) || !transitionList.contains(t2)) {
            throw new RuntimeException("Error: this net does not contain the transition(s)/place to bridge")
        }
        arcList += Arc.buildArcs(t1, pBridge, t2)
    }

    void createBridge(Transition t1, Transition t2) {
        if (!transitionList.contains(t1) || !transitionList.contains(t2)) {
            throw new RuntimeException("Error: this net does not contain the transition(s) to bridge")
        }
        Place pBridge = createPlace()
        arcList += Arc.buildArcs(t1, pBridge, t2)
    }

    // to remember the original name of the file
    String sourceName
    String sourceFile
}
