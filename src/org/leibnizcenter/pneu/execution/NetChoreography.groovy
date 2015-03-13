package org.leibnizcenter.pneu.execution

import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor
import org.leibnizcenter.pneu.components.Net
import org.leibnizcenter.pneu.components.TransitionType

import static groovyx.gpars.actor.Actors.actor

class NetChoreography {

    final boolean log = false

    Map<String, PlaceActor> placeId2ActorMap = [:]
    Map<String, TransitionActor> transitionId2ActorMap = [:]

    List<TransitionActor> emitters = []
    List<TransitionActor> collectors = []

    PlaceActor getPlaceActorById(String id) {
        return placeId2ActorMap.get(id)
    }

    TransitionActor getTransitionActorById(String id) {
        return transitionId2ActorMap.get(id)
    }

    void embody(Net net) {

        for (pl in net.placeList) {
            PlaceActor plActor = new PlaceActor(id: pl.id)
            plActor.nTokesAvailable = pl.marking.size() // TODO, to add DATA
            placeId2ActorMap[pl.id] = plActor
        }

        for (tr in net.transitionList) {

            TransitionActor trActor = new TransitionActor(id: tr.id)
            transitionId2ActorMap[tr.id] = trActor
            if (tr.type == TransitionType.EMITTER) {
                emitters << trActor
            } else if (tr.type == TransitionType.COLLECTOR) {
                collectors << trActor
            }

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

    void start(Integer delay = 100) {
        for (e in placeId2ActorMap + transitionId2ActorMap) {
            e.value.start()
        }
        def booting = actor {
            if (log) println("> booting")
            placeId2ActorMap.values()*.send(new Message(signal: Signal.BOOT))
            pause(delay)
        }
        booting.join()
    }

    void emit(Integer nTokens = 1, Integer delay = 100) {
        for (int i = 0; i < nTokens; i++) {
            def emitting = actor {
                if (log) println("> emission n. "+(i+1))
                emitters*.send(Signal.EMIT)
                pause(delay)
            }
            emitting.join()
        }

    }

    void pause(Integer delay = 100) {
        Thread.sleep(delay)
    }

    Map<String, Integer> outcome() {
        Map<String, Integer> emitted = [:]
        Map<String, Integer> collected = [:]

        def reader = actor {

            (emitters+collectors)*.send(Signal.STATUS)

            loop {
                react { n ->
                    if (emitters.contains(sender)) {
                        emitted[sender.id] = n
                    } else {
                        collected[sender.id] = n
                    }
                }

                if (emitted.size() == emitters.size() &&
                    collected.size() == collectors.size()) {
                    stop()
                }
            }
        }
        reader.join()

        return [emitted: emitted, collected: collected]
    }
}