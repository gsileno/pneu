package org.leibnizcenter.pneu.comparison

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition


class Comparison {

    static Float labelComparison(Net sourceNet, Net targetNet) {
        List<Place> sourcePlaces = sourceNet.placeList
        List<Place> targetPlaces = targetNet.placeList

        List<Transition> sourceTransitions = sourceNet.transitionList
        List<Transition> targetTransitions = targetNet.transitionList

        //  With empty nodes:
        // Integer lengthPlace = sourcePlaces.size() + targetPlaces.size()

        // Define length without empty nodes
        Integer lengthSourcePlaces = sourcePlaces.findAll() {it.name != ""}.size()
        Integer lengthTargetPlaces = targetPlaces.findAll() {it.name != ""}.size()

        Integer lengthPlaces = lengthSourcePlaces + lengthTargetPlaces

        Integer diff1 = 0
        Float simPlace = 0
        Float simTrans = 0

        for ( i in sourcePlaces ) {
            Float sim = 0
            Integer diff2 = 0
            if (i.name != "") {
                for ( j in targetPlaces ) {
                    Integer diff = Math.abs(diff2 - diff1)
                    println(i.name +"=?="+ j.name)
                    if (i.name == j.name) {
                        sim = (2 * diff) / lengthPlaces
                        println("YES!!! $sim $diff $lengthPlaces")
                        simPlace = simPlace + sim
                    }
                    diff2++
                }
            }
            diff1++
        }

        Integer lengthSourceTransitions = sourceTransitions.findAll() {it.name != ""}.size()
        Integer lengthTargetTransitions = targetTransitions.findAll() {it.name != ""}.size()

        Integer lengthTransitions = lengthSourceTransitions + lengthTargetTransitions

        diff1 = 0

        for ( i in sourceTransitions ) {
            Float sim = 0
            Integer diff2 = 0
            if (i.name != "") {
                for (j in targetTransitions) {
                    Integer diff = Math.abs(diff2 - diff1)
                    if (i.name == j.name) {
                        sim = (2 * diff) / lengthTransitions
                        simTrans = simTrans + sim
                    }
                    diff2++
                }
            }
            diff1++
        }

        // Calculate label similarity
        return (2 * (simPlace + simTrans)) / (lengthPlaces + lengthTransitions)

    }

    static Float structuralComparison(Net sourceNet, Net targetNet) {



    }


    static Float behaviouralComparison(Net sourceNet, Net targetNet) {



    }

}
