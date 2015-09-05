package org.leibnizcenter.pneu.components.petrinet

import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class NetInterface {

    List<Place> placeInputs = []
    List<Place> placeOutputs = []
    List<Transition> transitionInputs = []
    List<Transition> transitionOutputs = []

    String toString() {
        [placeInputs, placeOutputs, transitionInputs, transitionOutputs].toString()
    }

    String print() {
        String output = ""

        if (placeInputs.size() > 0) {
            output += "pIN: " + placeInputs + "\n"
        }
        if (placeOutputs.size() > 0) {
            output += "pOUT: " + placeOutputs + "\n"
        }
        if (transitionInputs.size() > 0) {
            output += "tIN: " + transitionInputs + "\n"
        }
        if (transitionOutputs.size() > 0) {
            output += "tOUT: " + transitionOutputs + "\n"
        }

        output
    }

}
