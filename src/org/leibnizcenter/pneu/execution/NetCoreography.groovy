package org.leibnizcenter.pneu.execution

import org.leibnizcenter.pneu.components.Net

import static groovyx.gpars.actor.Actors.actor

class NetCoreography {

    Map<String, PlaceActor> placeId2ActorMap = [:]
    Map<String, PlaceActor> transitionId2ActorMap = [:]

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
                PlaceActor plActor = placeId2ActorMap.get(arc.source.id)
                trActor.preMap[plActor] = 1
                plActor.postList << trActor
            }

            def edgesOut = net.arcList.findAll { arc -> arc.source.id == tr.id }
            edgesOut.each() { arc ->
                PlaceActor plActor = placeId2ActorMap.get(arc.target.id)
                trActor.postMap[plActor] = 1
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
        def orchestrator = actor {
            placeId2ActorMap.values()*.nAvailable = 0
            placeId2ActorMap.values()*.send(new Message(signal: Signal.BOOT))

        }
        // to finish the running
        orchestrator.join()
        transitionId2ActorMap.values()*.join()
    }
}
