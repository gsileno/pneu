//import static groovyx.gpars.actor.Actors.*
//
//final def decryptor = actorbased {
//    loop {
//        react { String message ->
//            reply message.reverse()
//        }
//    }
//}
//
//def console = actorbased {
//    decryptor.send 'lellarap si yvoorG'
//    react { println 'Decrypted message: ' + it
//    }
//}
//
//console.join()

import groovyx.gpars.actor.DefaultActor
class MyLoopActor extends DefaultActor {

    protected void act() {
        outerLoop()
    }

    private void outerLoop() {
        react { a ->
            println 'Outer: ' + a
            if (a != 0)
                innerLoop()
            else
                println 'Done'
        }
    }

    private void innerLoop() {
        react {b ->
            println 'Inner: ' + b
            if (b == 0)
                outerLoop()
            else
                innerLoop()
        }
    }
}

final def actor = new MyLoopActor().start()
actor 10
actor 20
actor 0
actor 0
actor.join()
