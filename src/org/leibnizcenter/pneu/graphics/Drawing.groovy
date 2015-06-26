package org.leibnizcenter.pneu.graphics

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class Drawing {

    Net draw(Net petriNet) {

        Net net = petriNet.clone()

        Map<Integer, List<Place>> inputPlaceRanking = [:]
        Map<Integer, List<Transition>> inputTransitionRanking = [:]

        for (p in net.placeList) {
            Integer size = p.inputs.size()
            if (!inputPlaceRanking[size]) {
                inputPlaceRanking[size] = [p]
            } else {
                inputPlaceRanking[size] << p
            }
        }

        for (t in net.placeList) {
            Integer size = t.inputs.size()
            if (!inputTransitionRanking[size]) {
                inputTransitionRanking[size] = [t]
            } else {
                inputTransitionRanking[size] << t
            }
        }



    }

}

