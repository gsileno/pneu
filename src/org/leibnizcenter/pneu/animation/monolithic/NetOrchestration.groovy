package org.leibnizcenter.pneu.animation.monolithic

import org.leibnizcenter.pneu.animation.monolithic.execution.BruteForceExecution
import org.leibnizcenter.pneu.animation.monolithic.execution.EnabledTransitionExecution
import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.animation.monolithic.execution.ExecutionMode
import org.leibnizcenter.pneu.animation.monolithic.execution.RepresentingPlacesExecution
import org.leibnizcenter.pneu.components.petrinet.Net

class NetOrchestration {

    Execution execution

    NetOrchestration(ExecutionMode executionMode = ExecutionMode.BruteForce) {
        switch (executionMode) {
            case ExecutionMode.BruteForce:
                execution = new BruteForceExecution()
                break
            case ExecutionMode.EnabledTransition:
                execution = new EnabledTransitionExecution()
                break
            case ExecutionMode.StaticRepresentingPlaces:
                execution = new RepresentingPlacesExecution()
                break
            case ExecutionMode.DynamicRepresentingPlaces:
                execution = new RepresentingPlacesExecution(dynamic: true)
                break
        }
    }

    void load(Net net) {
        if (!execution) return
        execution.load(net)
    }

    // run at most max steps (less if there no enabled transitions)
    Integer run(Integer max = 100) {

        Integer n

        for (n=0; n<max; n++) {
           if (!execution.step()) break
        }

        return n
    }

    void status() {
        println "Marking: "+execution.places
        println "Firings: "+execution.nFirings
    }

}
