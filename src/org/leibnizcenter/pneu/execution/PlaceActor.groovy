package org.leibnizcenter.pneu.execution

import groovy.transform.Immutable
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

/* List<Token> tokenList = []
List<TransitionActor> queue = []
Map<TransitionActor, List<Token>> queue = [:] */

class PlaceActor extends PlaceActorPPO {}

@Immutable
class Request {
    TransitionActor t  // source of the request
    Integer n          // number of tokens requested
}

class PlaceActorPPO extends DefaultActor {

    String id

    List<Request> requests = [] // request of reservation received by transition actors
    Integer nAvailable = 0      // number of tokens available in this place
    Boolean reserved = false    // flag on the place

    void act() {
        loop {
            println(id+"> cycle")

            Signal signal

            react { Message msg ->
                println(id+"> received "+msg+" from "+sender.id)
                signal = msg.signal
                switch (signal) {
                    case Signal.RESERVE:
                        requests << [sender, msg.n]
                        break
                    case Signal.RELEASE:
                        reserved = false
                        break
                    case Signal.TAKE:
                        nAvailable -= msg.n
                        reserved = true
                        break
                    case Signal.PUT:
                        nAvailable += msg.n
                        break
                }
            }

            if (signal != Signal.PUT && signal != Signal.RELEASE) {
                // respond to all requests which cannot be satisfied
                for (req in requests) {
                    if (req.n > nAvailable) {
                        println(id+"> saying to "+t.id+" that cannot satisfy its request")
                        req.t.send(Signal.FAILURE)
                        requests.remove(req)
                    }
                }
            }

            if (signal != Signal.RESERVE) {
                if (requests.size() > 0 && !reserved) {
                    println(id+"> saying to "+t.id+" that can satisfy its request")
                    Request req = requests.first()
                    req.t.send(Signal.SUCCESS)
                    reserved = true
                    requests.remove(req)
                }
            }
        }
    }

}

class PlaceActorPTO extends DefaultActor {

    String id

    List<Request> requests = [] // request of reservation received by transition actors
    Integer nAvailable = 0      // number of tokens available in this place
    Integer nReserved = 0       // number of tokens already reserved

    void act() {
        loop {
            Signal signal

            react { Message msg ->

                signal = msg.signal
                switch (msg.signal) {
                    case Signal.RESERVE:
                        requests << new Request(t: sender, n: msg.n)
                        break
                    case Signal.RELEASE:
                        nReserved -= msg.n
                        break
                    case Signal.TAKE:
                        nAvailable -= msg.n
                        nReserved -= msg.n
                        break
                    case Signal.PUT:
                        nAvailable += msg.n
                        break
                }
            }

            // respond to all requests
            for (req in requests) {
                if (signal != Signal.PUT && signal != Signal.RELEASE && req.n > nAvailable) {
                    // if required tokens are not available
                    req.t.send(Signal.FAILURE)
                    requests.remove(req)
                }
                if (signal != Signal.RESERVE && req.n <= nAvailable) { // if required tokens are available
                    req.t.send(Signal.SUCCESS)
                    nReserved = nReserved + req.n
                    requests.remove(req)
                }
            }
        }
    }

}
