package org.leibnizcenter.pneu.execution

import groovyx.gpars.actor.DefaultActor
import org.leibnizcenter.pneu.components.Transition

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

class TransitionActor extends TransitionActorPPOPTO {

}

class TransitionActorPPOPTO extends DefaultActor {

    String id

    Integer nConsumedTokens
    Integer nProducedTokens
    List<PlaceActor> preList = []
    List<PlaceActor> postList = []
    List<PlaceActor> reservedList = []

    void act() {
        loop {
            println(id+"> cycle "+preList.size()+"/"+postList.size())

            Boolean success
            for (p in preList) {
                println(id+"> asking "+p.id+" to reserve "+n+" tokens")
                p.send(new Message(signal: Signal.RESERVE, n: nConsumedTokens))

                react { signal ->
                    println(id+"> received "+signal+" from "+sender.id)
                    switch(signal) {
                        case Signal.SUCCESS:
                            reservedList << p
                            success = true
                            break
                        case Signal.FAILURE:
                            success = false
                            break
                    }
                }
            }

            if (success) {
                for (p in preList) {
                    println(id+"> asking "+p.id+" to consume "+n+" tokens")
                    p.send(new Message(signal: Signal.TAKE, n: nConsumedTokens))
                }
                for (p in postList) {
                    println(id+"> asking "+p.id+" to produce "+n+" tokens")
                    p.send(new Message(signal: Signal.PUT, n: nProducedTokens))
                }
            } else {
                for (p in reservedList) {
                    println(id+"> asking "+p.id+" to release "+n+" tokens")
                    p.send(new Message(signal: Signal.RELEASE, n: nConsumedTokens))
                }
            }
        }
    }
}
