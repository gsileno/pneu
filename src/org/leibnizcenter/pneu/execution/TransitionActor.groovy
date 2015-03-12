package org.leibnizcenter.pneu.execution

import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.actor.DynamicDispatchActor
import org.leibnizcenter.pneu.components.Place

/* http://www.gpars.org/1.0.0/guide/guide/actors.html

  General form for actors:

  class Player extends DefaultActor {
    String name
    Actor server
    int myNum

    void act() {
      loop {
        myNum = new Random().nextInt(10)
        server.send myNum
        react {
          switch (it) {
            case 'too large':
              println "$name: $myNum was too large";
              break
            case 'too small':
              println "$name: $myNum was too small";
              break
            case 'you win':
              println "$name: I won $myNum";
              terminate();
              break
          }
        }
      }
    }
  }

 */

/* Taubner's algorithm: On the implementation of Petri Nets (1998)

 BOOL success:
 WHILE TRUE
    -- black box section: leaving this means, the transition wants to occur
    -- management section: try to get the firing from the management of the place
    success := result of the attempt

 PPO/PTO algorithm

 PROC transition (VAL INT own.t)
   BOOL success:

   // it seems it attachs the place

   PROC PUT()
     PAR p FOR post[own.t]
       put [p][own.t] ! B[p][own.t]
   :

   PROC LINEAR.RESERVE()
     SET(INT) reserved.places:
     SEQ
        success := TRUE
        reserved.places := EMPTY
        SEQ p LINEARFOR pre[own.t]
            reserve[p][own.t] ! F[p][own.t]
            result[p][own.t] ? success
            IF
                success
                    reserved.places :=
                        reserved.places + p
                TRUE
            EXIT
        IF
            success
                PAR
                   PAR p FOR pre[own.t]
                     take[p][own.t] ! F[p][own.t]
                   PUT()
            TRUE
                PAR p FOR reserved.places
                    release[p][own.t] ! F[p][own.t]
   :

   WHILE TRUE
     SEQ
     -- black box section
     LINEAR.RESERVE()
 :

 */

class TransitionActor extends TransitionAsynchronousActor  {

}

class TransitionAsynchronousActor extends DefaultActor {

    String id

    Map<PlaceActor, Integer> preMap = [:]
    Map<PlaceActor, Integer> postMap = [:]
    List<PlaceActor> reservedList = []

    Boolean negotiation() {
        Boolean success = true

        loop {
            react { signal ->
                switch (signal) {
                    case Signal.SYNC:
                        Integer n = preMap.get(sender) // arc weight
                        println(id + "> asking " + sender.id + " to reserve " + n + " tokens")
                        reply(new Message(signal: Signal.RESERVE, n: n))
                        break;
                    case Signal.SUCCESS:
                        reservedList << sender
                        break
                    case Signal.FAILURE:
                        success = false
                        break
                }
            }

            if (success == false)
                return false
            if (reservedList.size() == preMap.size())
                return true
        }
    }

    void act() {
        loop {
            if (negotiation()) {
                for (e in preMap) {
                    println(id + "> asking " + e.key.id + " to consume " + e.value + " tokens")
                    e.key.send(new Message(signal: Signal.TAKE, n: e.value))
                }
                for (e in postMap) {
                    println(id + "> asking " + e.key.id + " to produce " + e.value + " tokens")
                    e.key.send(new Message(signal: Signal.PUT, n: e.value))
                }
            } else {
                for (p in reservedList) {
                    Integer n = preMap.get(p)
                    println(id + "> asking " + p.id + " to release " + n + " tokens")
                    p.send(new Message(signal: Signal.RELEASE, n: n))
                }
            }
        }
    }

    public void onDeliveryError(msg) {
        println(id + "> delivery error for "+msg)
    }
    public void afterStart() {
        println(id + "> start - setting up listeners")
    }
    public void afterStop(List undeliveredMessages) {
        println(id + "> stop "+undeliveredMessages)
    }
    public void onTimeout() {
        println(id + "> timeout")
    }
    /* public void onException(Throwable e) {
        println(id + "> exception "+e.toString())
    } */
}
