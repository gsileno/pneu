package org.leibnizcenter.pneu.components.basicpetrinet

import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType
import org.leibnizcenter.pneu.parsers.PNML2PN

class BasicNet extends Net {

    // an emitter transition is a natural input
    Transition createEmitterTransition() {
        BasicTransition tr = new BasicTransition(type: TransitionType.EMITTER)
        transitionList << tr
        inputs << tr
        tr
    }

    // a collector transition is a natural output
    Transition createCollectorTransition() {
        BasicTransition tr = new BasicTransition(type: TransitionType.COLLECTOR)
        transitionList << tr
        outputs << tr
        tr
    }

    Transition createTransition(String label = null) {
        BasicTransition tr = new BasicTransition(name: label)
        transitionList << tr
        tr
    }

    Place createPlace(String label = null) {
        BasicPlace pl = new BasicPlace(name: label)
        placeList << pl
        pl
    }

    Transition createBridge(Place p1, Place p2) {
        if (!getAllPlaces().contains(p1) || !getAllPlaces().contains(p2)) {
            throw new RuntimeException("Error: this net does not contain the place(s) to bridge")
        }
        Transition tBridge = createTransition()
        createArc(p1, tBridge)
        createArc(tBridge, p2)
        tBridge
    }

    Place createBridge(Transition t1, Transition t2) {
        if (!getAllTransitions().contains(t1) || !getAllTransitions().contains(t2)) {
            throw new RuntimeException("Error: this net does not contain the transition(s) to bridge")
        }
        Place pBridge = createPlace()
        createArc(t1, pBridge)
        createArc(pBridge, t2)
        pBridge
    }

    Place createDiodeBridge(Transition t1, Transition t2) {
        if (!getAllTransitions().contains(t1) || !getAllTransitions().contains(t2)) {
            throw new RuntimeException("Error: this net does not contain the transition(s) to bridge")
        }
        Place pBridge = createPlace()
        createDiodeArc(t1, pBridge)
        createArc(pBridge, t2)
        pBridge
    }

    Transition createNexus(List<Place> inputs, List<Place> outputs, List<Place> biflows, List<Place> diode, List<Place> inhibitors) {
        Transition tBridge = createTransition()

        for (p in inputs + biflows) {
            if (!getAllPlaces().contains(p)) {
                throw new RuntimeException("Error: this net does not contain the given input place (${p})")
            }
            createArc(p, tBridge)
        }

        for (p in outputs + biflows + diode) {
            if (!getAllPlaces().contains(p)) {
                throw new RuntimeException("Error: this net does not contain the given output place (${p})")
            }
            createArc(tBridge, p)
        }

        for (p in inhibitors + diode) {
            if (!getAllPlaces().contains(p)) {
                throw new RuntimeException("Error: this net does not contain the given inhibitor place (${p})")
            }
            createInhibitorArc(p, tBridge)
        }

        tBridge
    }

    // deep cloning done for nets
    // only the net structure is cloned, all the elements remains the same (e.g. places, transitions, etc.)
    Net minimalClone(Map<Net, Net> sourceCloneMap = [:]) {

        if (!sourceCloneMap[this]) {
            sourceCloneMap[this] = new BasicNet(transitionList: transitionList.collect(),
                    placeList: placeList.collect(),
                    arcList: arcList.collect(),
                    inputs: inputs.collect(),
                    outputs: outputs.collect(),
                    function: function)
        }

        Net clone = sourceCloneMap[this]

        for (subNet in subNets) {
            if (!sourceCloneMap[subNet]) {
                sourceCloneMap[subNet] = subNet.minimalClone(sourceCloneMap)
            }
            clone.subNets << sourceCloneMap[subNet]
        }

        for (parent in parents) {
            if (!sourceCloneMap[parent]) {
                sourceCloneMap[parent] = parent.minimalClone(sourceCloneMap)
            }
            clone.parents << sourceCloneMap[parent]
        }

        clone
    }

    //////////////////////////////////////////////////////
    // helpers for importing the net
    //////////////////////////////////////////////////////

    static Net importFromPNML(String filename) {

        Net net

        print("reading from file " + filename + "... ");
        try {
            net = PNML2PN.parseFile(filename)
        } catch (FileNotFoundException) {
            throw new RuntimeException("sorry, file " + filename + " not found or not valid.")
        }

        net
    }

}