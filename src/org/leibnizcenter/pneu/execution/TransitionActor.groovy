package org.leibnizcenter.pneu.execution

import groovy.transform.Immutable
import groovyx.gpars.actor.DefaultActor
import org.leibnizcenter.pneu.components.Place
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

class Connection {
    PlaceActor p   // place to which it is connected
    Integer n      // weight of the arc (consumption/production)
}

class TransitionActorPPOPTO extends DefaultActor {

    String id

    List<Connection> preList = []
    List<Connection> postList = []
    List<Connection> reservedList = []

    public void onDeliveryError(msg) {
        println "Could not deliver message $msg"
    }

    void act() {
        loop {
            println(id+"> starting cycle")

            Boolean success = false
            for (c in preList) {
                println(id+"> asking "+c.p.id+" to reserve "+c.n+" tokens")
                // c.p.send(new Message(signal: Signal.RESERVE, n: c.n))
                c.p.send "ciao"

                react { signal ->
                    println(id+"> received "+signal+" from "+sender.id)
                    switch(signal) {
                        case Signal.SUCCESS:
                            reservedList << c
                            success = true
                            break
                        case Signal.FAILURE:
                            success = false
                            break
                    }
                }
            }

            if (success) {
                for (c in preList) {
                    println(id+"> asking "+c.p.id+" to consume "+c.n+" tokens")
                    c.p.send(new Message(signal: Signal.TAKE, n: c.n))
                }
                for (c in postList) {
                    println(id+"> asking "+c.p.id+" to produce "+c.n+" tokens")
                    c.p.send(new Message(signal: Signal.PUT, n: c.n))
                }
            } else {
                for (c in reservedList) {
                    println(id+"> asking "+c.p.id+" to release "+c.n+" tokens")
                    c.p.send(new Message(signal: Signal.RELEASE, n: c.n))
                }
            }
        }
    }
}
