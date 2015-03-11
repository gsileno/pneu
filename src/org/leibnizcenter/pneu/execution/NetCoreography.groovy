package org.leibnizcenter.pneu.execution

import org.leibnizcenter.pneu.components.Net

class NetCoreography {

    Map<Integer, PlaceActor> placeId2ActorMap = [:]
    Map<Integer, PlaceActor> transitionId2ActorMap = [:]

    void embody(Net net) {

        for (pl in net.placeList) {
            PlaceActor plActor = new PlaceActor(id: pl.id)
            placeId2ActorMap[pl.id] = plActor
        }

        for (tr in net.transitionList) {
            TransitionActor trActor = new TransitionActor(id: tr.id)
            transitionId2ActorMap[tr.id] = trActor

            def edgesIn = net.arcList.findAll { arc -> arc.target.id == tr.id }
            edgesIn.each() { arc ->
                trActor.preList << new Connection(p: placeId2ActorMap.get(arc.source.id), n: 1)
            }

            def edgesOut = net.arcList.findAll { arc -> arc.source.id == tr.id }
            edgesOut.each() { arc ->
                trActor.postList << new Connection(p: placeId2ActorMap.get(arc.target.id), n: 1)
            }
        }
    }

    void run() {
        for (e in placeId2ActorMap) {
            e.value.start()
        }
        for (e in transitionId2ActorMap) {
            e.value.start()
        }
    }
}
