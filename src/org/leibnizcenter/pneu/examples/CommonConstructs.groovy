package org.leibnizcenter.pneu.examples

import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.basicpetrinet.BasicTransition
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class CommonConstructs {

    static Net inhibitorChoice1() {
        Net net = new BasicNet()

        Transition tIn = net.createEmitterTransition()

        Transition e1 = net.createTransition("e1")
        Transition e2 = net.createTransition("e2")
        Transition tc = net.createTransition("c")

        Place c = net.createPlace("c")
        Place o1 = net.createPlace("o1")
        Place o2 = net.createPlace("o2")
        net.createPlaceNexus([tIn], [e1, e2], [tc], [], [])
        net.createPlaceNexus(c, [], [e1], [], [tc], [e2])
        net.createArc(e1, o1)
        net.createArc(e2, o2)
        net.createInhibitorArc(o2, e2)

        net.resetIds()
        net
    }

    // we consider a very primitive propositional logic for labels: "-" for negation.
    static Net inhibitorChoice2() {
        Net net = new BasicNet()

        Transition tIn = net.createEmitterTransition()
        Transition e1 = net.createTransition("e1")
        Transition e2 = net.createTransition("e2")
        Place pc = net.createPlace("c")
        Place pnc = net.createPlace("-c")
        Transition tc = net.createTransition("c")
        Transition tnc = net.createTransition("-c")
        Place o1 = net.createPlace("o1")
        Place o2 = net.createPlace("o2")
        net.createPlaceNexus([tIn], [tc, tnc], [], [], [])
        net.createBridge(tc, pc, e1)
        net.createBridge(tnc, pnc, e2)
        net.createArc(e1, o1)
        net.createArc(e2, o2)

        net.resetIds()
        net
    }



}
