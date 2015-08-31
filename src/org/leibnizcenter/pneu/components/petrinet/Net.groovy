package org.leibnizcenter.pneu.components.petrinet

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.builders.PN2abstract
import org.leibnizcenter.pneu.builders.PN2dot
import org.leibnizcenter.pneu.builders.PN2json
import org.leibnizcenter.pneu.builders.PN2log
import org.leibnizcenter.pneu.components.graphics.Grid

@Log4j
abstract class Net {

    List<Transition> transitionList = []
    List<Place> placeList = []
    List<Arc> arcList = []
    List<Net> subNets = []
    List<Net> parents = []

    // this node synthetizes the function of the net (useful for subnets)
    Node function

    // for operations, these nodes are events/transitions,
    // for expressions, these are situations/places
    List<Node> inputs = []
    List<Node> outputs = []

    //////////////////////////////////////////////////////
    // helpers to get some properties from the net
    //////////////////////////////////////////////////////

    // a net may be assigned a function, as a place
    Boolean isPlaceLike() {
        if (!function) return false
        Place.isAssignableFrom(function.class)
    }

    // a net may be assigned a function, as a transition
    Boolean isTransitionLike() {
        if (!function) return false
        Transition.isAssignableFrom(function.class)
    }

    // returns all places (included of nested subnets)
    List<Place> getAllPlaces() {
        List<Place> allPlaces = []
        allPlaces += placeList
        for (subnet in subNets) {
            allPlaces += subnet.getAllPlaces()
        }
        allPlaces
    }

    // returns all transitions (included of nested subnets)
    List<Transition> getAllTransitions() {
        List<Transition> allTransitions = []
        allTransitions += transitionList
        for (subnet in subNets) {
            allTransitions += subnet.getAllTransitions()
        }
        allTransitions
    }

    // returns all arcs (included of nested subnets)
    List<Arc> getAllArcs() {
        List<Arc> allArcs = []
        allArcs += arcList
        for (subnet in subNets) {
            allArcs += subnet.getAllArcs()
        }
        allArcs
    }

    // returns all subnets (included of nested subnets)
    // TODO: I think it is wrong for subnets belonging to multiple nets
    List<Net> getAllNets() {
        List<Net> allNets = []
        allNets += this
        for (subnet in subNets) {
            allNets += subnet.getAllNets()
        }
        allNets
    }

    List<Arc> getBiflowArcs() {
        List<Arc> biflowArcs = []
        for (Arc a in arcList) {
            for (Arc b in arcList - a) {
                if (a.source == b.target && b.source == a.target && a.weight == b.weight) {
                    if (a.type == ArcType.NORMAL && b.type == ArcType.NORMAL) {
                        if (!biflowArcs.contains(a) && !biflowArcs.contains(b))
                            biflowArcs += [a, b]
                    }
                }
            }
        }
        biflowArcs
    }

    List<Arc> getDiodeArcs() {
        List<Arc> diodeArcs = []
        for (Arc a in arcList) {
            for (Arc b in arcList - a) {
                if (a.source == b.target && b.source == a.target && a.weight == b.weight) {
                    if (a.type == ArcType.NORMAL && b.type == ArcType.INHIBITOR) {
                        if (!biflowArcs.contains(a) && !biflowArcs.contains(b)) {
                            diodeArcs += [a, b]
                        }
                    }
                }
            }
        }
        diodeArcs
    }

    List<Arc> getResetArcs() {
        List<Arc> resetArcs = []
        for (Arc a in arcList) {
            if (a.type == ArcType.RESET) {
                resetArcs << a
            }
        }
        resetArcs
    }

    List<Arc> getStrictlyInhibitorArcs() {
        List<Arc> diodeArcs = getDiodeArcs()
        List<Arc> inhibitorArcs = []
        for (Arc a in arcList - diodeArcs) {
            if (a.type == ArcType.INHIBITOR ) {
                inhibitorArcs << a
            }
        }
        inhibitorArcs
    }

    // TODO: consider LINK arcs

    List<Arc> getStrictlyNormalArcs() {
        List<Arc> diodeArcs = getDiodeArcs()
        List<Arc> arcs = []
        for (Arc a in (arcList - diodeArcs) - biflowArcs) {
            if (a.type == ArcType.NORMAL ) {
                arcs << a
            }
        }
        arcs
    }

    //////////////////////////////////////////////////////
    // helpers for textual visualization
    //////////////////////////////////////////////////////

    void printMarking() {
        String output = ""
        for (place in placeList) {
            output += place.id + ": "
            for (token in place.marking) {
                output += token.toString() + ", "
            }
            output = output[0..-3] + "\n"
        }
        print output
    }

    String toString() {
        return "Net@" + hashCode()
    }

    void print() {
        println PN2log.convert(this)
    }

    //////////////////////////////////////////////////////
    // helpers for inclusion, cloning and comparison
    //////////////////////////////////////////////////////

    // include net at the given positions
    // maintains a neutral position about the inputs/outputs
    // of the parent net
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

    // to deep clone the net
    abstract Net minimalClone(Map<Net, Net> sourceCloneMap)

    // to check whether two nets share the same elements (places, transitions, arcs and subnets)
    // parent nets are NOT considered
    static Boolean compare(Net n1, Net n2) {

        // if they are the same object return true
        if (n1 == n2) return true

        // if they have a different number of places, transitions or arcs they are not the same
        if (n1.placeList.size() != n2.placeList.size()) {
            return false
        }
        if (n1.transitionList.size() != n2.transitionList.size()) {
            return false
        }
        if (n1.arcList.size() != n2.arcList.size()) {
            return false
        }
        if (n1.subNets.size() != n2.subNets.size()) {
            return false
        }

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

    abstract Transition createEmitterTransition()

    abstract Transition createCollectorTransition()

    abstract Transition createTransition()

    abstract Transition createTransition(String label)

    abstract Place createPlace()

    abstract Place createPlace(String label)

    Transition propagateTransition(Transition source) {
        Transition target = source.minimalClone()
        transitionList << target
        createBridge(source, target)
        target
    }

    void createArc(Place p, Transition t) {
        if (!getAllPlaces().contains(p) || !getAllTransitions().contains(t)) {
            throw new RuntimeException("Error: this net does not contain the place/transition to connect")
        }
        arcList << Arc.buildArc(p, t)
    }

    void createArc(Transition t, Place p) {
        if (!getAllPlaces().contains(p) || !getAllTransitions().contains(t)) {
            throw new RuntimeException("Error: this net does not contain the transition/place to connect")
        }
        arcList << Arc.buildArc(t, p)
    }

    void createInhibitorArc(Place p, Transition t) {
        if (!getAllPlaces().contains(p) || !getAllTransitions().contains(t)) {
            throw new RuntimeException("Error: this net does not contain the place/transition to connect")
        }
        arcList << Arc.buildInhibitorArc(p, t)
    }

    void createDiodeArc(Transition t, Place p) {
        if (!getAllPlaces().contains(p) || !getAllTransitions().contains(t)) {
            throw new RuntimeException("Error: this net does not contain the place/transition to connect")
        }
        arcList += Arc.buildDiodeArcs(t, p)
    }

    void createResetArc(Place p, Transition t) {
        if (!getAllPlaces().contains(p) || !getAllTransitions().contains(t)) {
            throw new RuntimeException("Error: this net does not contain the place/transition to connect")
        }
        arcList << Arc.buildResetArc(t, p)
    }

    void createBridge(Place p1, Transition tBridge, Place p2) {
        if (!getAllPlaces().contains(p1) || !getAllPlaces().contains(p2) || !getAllTransitions().contains(tBridge)) {
            throw new RuntimeException("Error: this net does not contain the place(s)/transition to bridge")
        }
        arcList += Arc.buildArcs(p1, tBridge, p2)
    }

    void createBridge(Transition t1, Place pBridge, Transition t2) {
        if (!getAllTransitions().contains(t1) || !getAllTransitions().contains(t2)) {
            throw new RuntimeException("Error: this net does not contain the transition(s)/place to bridge")
        }
        arcList += Arc.buildArcs(t1, pBridge, t2)
    }

    // depending on whether are logic or basic petri nets
    // you need to construct adequately the intermediate node
    abstract Transition createBridge(Place p1, Place p2)

    abstract Place createBridge(Transition t1, Transition t2)

    abstract Place createDiodeBridge(Transition t1, Transition t2)

    abstract Transition createTransitionNexus(List<Place> inputs, List<Place> outputs, List<Place> biflows, List<Place> diode, List<Place> inhibitors)

    void resetIds() {
        PN2abstract.resetIds(this)
    }

    //////////////////////////////////////////////////////
    // helpers for exporting the net
    //////////////////////////////////////////////////////

    // textual log output
    void exportToLog(String filename, String path = "out/log/") {
        exportToLog(this, filename, path)
    }

    static void exportToLog(Net net, String filename, String path = "out/log/") {

        File folder
        String outputFile

        folder = new File(path)
        if (!folder.exists()) folder.mkdirs()

        outputFile = path + filename + ".log"

        new File(outputFile).withWriter {
            out -> out.println(PN2log.convert(net))
        }
    }

    // dot output for graphviz
    void exportToDot(String filename, String path = "out/dot/") {
        exportToDot(this, filename, path)
    }

    static void exportToDot(Net net, String filename, String path = "out/dot/") {
        File folder
        String outputFile

        folder = new File(path)
        if (!folder.exists()) folder.mkdirs()

        outputFile = path + filename + ".dot"

        new File(outputFile).withWriter {
            out -> out.println(PN2dot.convert(net))
        }
    }

    // json output
    void exportToJson(String filename, String path = "out/json/") {
        exportToJson(this, filename, path)
    }

    static void exportToJson(Net net, String filename, String path = "out/json/") {
        File folder
        String outputFile

        folder = new File(path)
        if (!folder.exists()) folder.mkdirs()

        outputFile = path + filename + ".json"

        new File(outputFile).withWriter {
            out -> out.println(PN2json.convert(net))
        }
    }

    //////////////////////////////////////////////////////
    // helpers for graphical visualization information
    //////////////////////////////////////////////////////

    // this serves for graphical purposes
    Grid grid

    // to remember the original name of the file, when importing from PNML
    String sourceName
    String sourceFile

    // setup the grid for visualization purposes
    // for each place and transition check the min and max position
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


}
