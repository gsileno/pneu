package org.leibnizcenter.pneu.execution

import groovyx.gpars.actor.DefaultActor

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

class PlaceActor extends AsynchronousPlaceActor {}

class AsynchronousPlaceActor extends DefaultActor {

    final boolean log = true

    String id

    List<TransitionActor> postList = [] // request of reservation received by transition actors

    Integer nTokesAvailable = 0     // number of tokens available in this place
    Integer nTokesReserved = 0      // number of tokens already reserved

    void boot() {
        for (c in postList) {
            if (log) println(id + "> sending SYNC to "+c.id)
            c.send(Signal.SYNC)
        }
    }

    boolean reserve(int n) {
        if (n <= nTokesAvailable - nTokesReserved) {
            nTokesReserved = nTokesReserved + n
            return true
        } else {
            return false
        }
    }

    void release(int n) {
        nTokesReserved -= n
    }

    void take(int n) {
        nTokesAvailable -= n
        nTokesReserved -= n
    }

    void put(int n) {
        nTokesAvailable += n
    }

    void act() {
        loop {
            Signal signal

            react { Message msg ->

                signal = msg.signal
                switch (msg.signal) {
                    case Signal.BOOT:
                        if (log) println(id + "> booting")
                        boot()
                        break
                    case Signal.RESERVE:
                        if (log) println(id + " ["+nTokesReserved+"|"+nTokesAvailable+"]> receiving request RESERVE "+msg.n+" from "+sender.id)
                        if (reserve(msg.n)) {
                            reply(Signal.SUCCESS)
                        } else {
                            reply(Signal.FAILURE)
                        }
                        break
                    case Signal.RELEASE:
                        if (log) println(id + " ["+nTokesReserved+"|"+nTokesAvailable+"]> receiving request RELEASE "+msg.n+" from "+sender.id)
                        release(msg.n)
                        break
                    case Signal.TAKE:
                        if (log) println(id + " ["+nTokesReserved+"|"+nTokesAvailable+"]> receiving command TAKE "+msg.n+" from "+sender.id)
                        take(msg.n)
                        break
                    case Signal.PUT:
                        if (log) println(id + " ["+nTokesReserved+"|"+nTokesAvailable+"]> receiving command PUT "+msg.n+" from "+sender.id)
                        put(msg.n)
                        boot()
                        break
                }
            }
        }
    }

    public void onDeliveryError(msg) {
        if (log) println(id + "> delivery error for "+msg)
    }
    public void afterStart() {
        if (log) println(id + "> start")
    }
    public void afterStop(List undeliveredMessages) {
        if (log) println(id + "> stop "+undeliveredMessages)
    }
    public void onTimeout() {
        if (log) println(id + "> timeout")
    }
    /* public void onException(Throwable e) {
        if (log) println(id + "> exception "+e.toString())
    } */
}