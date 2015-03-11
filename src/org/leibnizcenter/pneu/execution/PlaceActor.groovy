package org.leibnizcenter.pneu.execution

import groovyx.gpars.actor.DefaultActor
import org.leibnizcenter.pneu.components.Place
import org.leibnizcenter.pneu.components.Token

/* Taubner's algorithm: On the implementation of Petri Nets (1998)

 PTO algorithm

 PROC place (VAL INT own.p)
   INT tokens, i
   BOOL place.is.reserved:
   --(PTO) INT reserved.t:
   QUEUE([2]INT) q:

   PROC clean.q()
     SEQ e FOR q
        IF e[1] > tokens
          SEQ
            result[own.p][e[0]] ! FALSE
            DEQ(e,q)
   :
   PROC check.q()
     IF --(PTO) WHILE
       q <> EMPTY AND NOT place.is.reserved
       --(PTO) ...AND FIRST(q)[1] <= tokens
         SEQ
            result[own.p][FIRST(q)[0]] ! TRUE
            place.is.reserved := TRUE
            --(PTO) reserevd.t := reserved.t + FIRST(q)[1]
            DEQ(FIRST(q),q)
   :
   SEQ
     tokens := MO[own.p]
     place.is.reserved := FALSE
     --(PTO) reserved.t := 0
     q := EMPTY
     WHILE TRUE
       SEQ
         ALT
           ALT t = 0 for t.max
              reserve[own.p][t] ?i
                ENQ([t,i], q)
           ALT t = 0 for t.max
              release[own.p][t] ?i
                place.is.reserved := FALSE
                --(PTO) reserved.t := reserved.t -i
           ALT t = 0 for t.max
              take[own.p][t] ?i
                SEQ
                  tokens := tokens - i
                  place.is.reserved := FALSE
                  --(PTO) reserved.t := reserved.t -i
           ALT t = 0 for t.max
              put[own.p][t] ?i
              tokens := tokens + i
         clean,q() -- redundant for put, relase
         check,q() -- redundant for put
 :

 */

enum Response { FAILURE, SUCCESS }
enum Request { RESERVE, RELEASE, TAKE, PUT }

class PlaceActor extends DefaultActor {

    Place p
    List<Token> tokenList = []
    List<TransitionActor> queue = []
    // Map<TransitionActor, Token> queue = [:]

    Integer nTokens

    Boolean reserved // PPO
    Integer reservedTransitions // PPT

    void act() {
        loop {
            react { msg ->             // sender is another anonymous entity
                switch (msg) {
                    case Request.RESERVE:
                        queue << [sender]
                        break
                    case Request.RELEASE:
                        reserved = false
                        reservedTransitions = reservedTransitions - 1
                        break
                    case Request.TAKE:
                        nTokens = nTokens - 1
                        reserved = true
                        reservedTransitions = reservedTransitions + 1
                        break
                    case Request.PUT:
                        nTokens = nTokens + 1
                        break
                }
            }
            for (e in queue) {
                if 
            }
        }
    }

}
