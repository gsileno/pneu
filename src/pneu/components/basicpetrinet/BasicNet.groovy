package pneu.components.basicpetrinet

import pneu.components.petrinet.Net
import pneu.components.petrinet.Place
import pneu.components.petrinet.Transition
import pneu.components.petrinet.TransitionType
import pneu.parsers.PNML2PN

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

    Transition createLinkTransition() {
        BasicTransition tr = new BasicTransition()
        transitionList << tr
        tr
    }

    Place createLinkPlace() {
        BasicPlace pl = new BasicPlace()
        placeList << pl
        pl
    }

    Transition createTransition(String label) {
        BasicTransition tr = new BasicTransition(name: label)
        transitionList << tr
        tr
    }

    Place createPlace(String label) {
        BasicPlace pl = new BasicPlace(name: label)
        placeList << pl
        pl
    }

    Net minimalCloneNoRecursive() {
        new BasicNet(transitionList: transitionList.collect(),
                placeList: placeList.collect(),
                arcList: arcList.collect(),
                inputs: inputs.collect(),
                outputs: outputs.collect(),
                function: function)
    }

    //////////////////////////////////////////////////////
    // helpers for importing the net
    //////////////////////////////////////////////////////

    static Net importFromPNML(String filename) {

        Net net

//        try {
            net = PNML2PN.parseFile(filename)
//        } catch (FileNotFoundException) {
//            throw new RuntimeException("sorry, file " + filename + " not found or not valid.")
//        }

        net
    }

}