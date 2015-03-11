package org.leibnizcenter.pneu.execution

import org.leibnizcenter.pneu.components.Net

class NetCoreography {

    Map<Integer, PlaceActor> placeId2ActorMap = [:]
    Map<Integer, PlaceActor> transitionId2ActorMap = [:]

    void embody(Net net) {

        for (pl in net.placeList) {
            PlaceActor plActor = new PlaceActor()
            placeId2ActorMap[pl.id] = plActor
        }

        for (tr in net.transitionList) {
            TransitionActor trActor = new TransitionActor()
            transitionId2ActorMap[tr.id] = trActor

            def edgesIn = net.arcList.findAll { arc -> arc.target.id == tr.id }
            edgesIn.each() { arc ->
                trActor.preList << placeId2ActorMap.get(arc.source.id)
            }

            def edgesOut = net.arcList.findAll { arc -> arc.source.id == tr.id }
            edgesOut.each() { arc ->
                trActor.postList << placeId2ActorMap.get(arc.target.id)
            }
        }

        println placeId2ActorMap
        println transitionId2ActorMap
    }
}
