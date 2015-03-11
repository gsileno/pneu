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

class TransitionActor extends DefaultActor {

    Transition t
    List<PlaceActor> preList
    List<PlaceActor> postList
    List<PlaceActor> reservedList = []

    void act() {
        loop {
            Boolean success
            for (p in preList) {
                p.send() // reserve F[p]
                react { msg ->
                    switch(msg) {
                        case Response.SUCCESS:
                            reservedList << p
                            success = true
                            break
                        case Response.FAILURE:
                            success = false // ??
                            break
                    }
                }
            }

            if (success) {
                // PAR
                for (p in preList)          // PAR
                    p.send(Request.TAKE)    // take F[p]
                for (p in postList)         // PAR
                    p.send(Request.PUT)     // put B[p]
            } else {
                for (p in reservedList)     // PAR
                    p.send(Request.RELEASE) // release F[p]
            }
        }
    }
}
