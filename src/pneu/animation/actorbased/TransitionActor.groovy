//package pneu.animation.actorbased
//
//import groovyx.gpars.actor.DefaultActor
//
///* http://www.gpars.org/1.0.0/guide/guide/actors.html
//
//  General form for actors:
//
//  class Player extends DefaultActor {
//    String name
//    Actor server
//    int myNum
//
//    void act() {
//      loop {
//        myNum = new Random().nextInt(10)
//        server.send myNum
//        react {
//          switch (it) {
//            case 'too large':
//              println "$name: $myNum was too large";
//              break
//            case 'too small':
//              println "$name: $myNum was too small";
//              break
//            case 'you win':
//              println "$name: I won $myNum";
//              terminate();
//              break
//          }
//        }
//      }
//    }
//  }
//
// */
//
///* Taubner's algorithm: On the implementation of Petri Nets (1998)
//
// BOOL success:
// WHILE TRUE
//    -- black box section: leaving this means, the transition wants to occur
//    -- management section: try to get the firing from the management of the place
//    success := result of the attempt
//
// PPO/PTO algorithm
//
// PROC transition (VAL INT own.t)
//   BOOL success:
//
//   // it seems it attachs the place
//
//   PROC PUT()
//     PAR p FOR post[own.t]
//       put [p][own.t] ! B[p][own.t]
//   :
//
//   PROC LINEAR.RESERVE()
//     SET(INT) reserved.places:
//     SEQ
//        success := TRUE
//        reserved.places := EMPTY
//        SEQ p LINEARFOR pre[own.t]
//            reserve[p][own.t] ! F[p][own.t]
//            result[p][own.t] ? success
//            IF
//                success
//                    reserved.places :=
//                        reserved.places + p
//                TRUE
//            EXIT
//        IF
//            success
//                PAR
//                   PAR p FOR pre[own.t]
//                     take[p][own.t] ! F[p][own.t]
//                   PUT()
//            TRUE
//                PAR p FOR reserved.places
//                    release[p][own.t] ! F[p][own.t]
//   :
//
//   WHILE TRUE
//     SEQ
//     -- black box section
//     LINEAR.RESERVE()
// :
//
// */
//
//class TransitionActor extends TransitionAsynchronousActor  {
//
//}
//
//class TransitionAsynchronousActor extends DefaultActor {
//
//    final Boolean log = true
//
//    String id
//
//    Map<PlaceActor, Integer> preMap = [:]
//    Map<PlaceActor, Integer> postMap = [:]
//    List<PlaceActor> reservedList = []
//
//    Integer nTokensCollected = 0 // number of tokens consumed here
//    Integer nTokensEmitted = 0   // number of tokens produced here
//
//    Boolean syncing = false // flag to avoid double syncs
//
//    Boolean firing() {
//
//        react { signal ->
//            switch (signal) {
//                case Signal.STATUS:
//                    reply(nTokensCollected)
//                    break
//                case Signal.SYNC:
//                    if (!syncing) {
//                      syncing = true
//                      Integer n = preMap.get(sender) // arc weight
//                      if (log) println(id + "> asking " + sender.id + " to reserve " + n + " tokens")
//                      reply(new Message(signal: Signal.RESERVE, n: n))
//                    }
//                    break
//                case Signal.SUCCESS:
//                    if (log) println(id + "> success from " + sender.id)
//                    reservedList << sender
//                    break
//                case Signal.FAILURE:
//                    if (log) println(id + "> failure from " + sender.id)
//                    if (log) println(id + "> renounces to fire..")
//                    return false
//                    break
//            }
//        }
//
//        if (reservedList.size() == preMap.size()) {
//            if (log) println(id + "> fires!")
//            return true
//        }
//    }
//
//    void act() {
//        if (preMap.size() > 0) {
//            loop {
//                if (firing()) {
//                    reservedList = []
//                    for (e in preMap) {
//                        if (log) println(id + "> asking " + e.key.id + " to consume " + e.value + " tokens")
//                        nTokensCollected = nTokensCollected + e.value
//                        e.key.send(new Message(signal: Signal.TAKE, n: e.value))
//                    }
//                    for (e in postMap) {
//                        if (log) println(id + "> asking " + e.key.id + " to produce " + e.value + " tokens")
//                        e.key.send(new Message(signal: Signal.PUT, n: e.value))
//                    }
//                    syncing = false
//                } else {
//                    for (p in reservedList) {
//                        Integer n = preMap.get(p)
//                        if (log) println(id + "> asking " + p.id + " to release " + n + " tokens")
//                        p.send(new Message(signal: Signal.RELEASE, n: n))
//                    }
//                    syncing = false
//                }
//            }
//        } else { // for emittor transitions (they don't have input places)
//            loop {
//                react { signal ->
//                    switch (signal) {
//                        case Signal.EMIT:
//                            for (e in postMap) {
//                                if (log) println(id + "> emitting " + e.value + " tokens into " + e.key.id)
//                                nTokensEmitted++
//                                e.key.send(new Message(signal: Signal.PUT, n: e.value))
//                            }
//                            break
//                        case Signal.STATUS:
//                            reply(nTokensEmitted)
//                            break
//                    }
//                }
//            }
//        }
//    }
//
//    public void onDeliveryError(msg) {
//        if (log) println(id + "> delivery error for "+msg)
//    }
//    public void afterStart() {
//        if (log) println(id + "> start")
//    }
//    public void afterStop(List undeliveredMessages) {
//        if (log) println(id + "> stop "+undeliveredMessages)
//    }
//    public void onTimeout() {
//        if (log) println(id + "> timeout")
//    }
//    /* public void onException(Throwable e) {
//        if (log) println(id + "> exception "+e.toString())
//    } */
//}
