package org.leibnizcenter.pneu.animation.monolithic

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition



class NetOrchestration {

    Execution execution = new BruteForceExecution()

    void embody(Net net) {
        execution.embody(net)
    }

    void run() {

    }

}
