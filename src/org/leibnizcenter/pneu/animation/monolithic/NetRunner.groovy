package org.leibnizcenter.pneu.animation.monolithic

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.animation.monolithic.execution.BruteForceExecution
import org.leibnizcenter.pneu.animation.monolithic.execution.Execution
import org.leibnizcenter.pneu.animation.monolithic.execution.ExecutionMode
import org.leibnizcenter.pneu.components.petrinet.Net

@Log4j

// the runner class serves to organize execution or analysis
class NetRunner {

    Analysis analysis
    Execution execution

    // TODO: implement additional execution modes
    NetRunner(ExecutionMode executionMode = ExecutionMode.BruteForce) {
        switch (executionMode) {
            case ExecutionMode.BruteForce:
                execution = new BruteForceExecution()
                break
        }
    }

    void load(Net net) {
        execution.load(net)
    }

    // run at most max steps (less if there no enabled transitions)
    Integer run(Integer maxSteps = 100) {
        Integer n
        for (n = 0; n < maxSteps; n++) {
            if (!execution.step()) break
        }
        return n
    }

    // run at most max steps (less if there no enabled transitions)
    Integer analyse(Integer max = 100) {

        // in a previous analysis does not exist create it.
        if (!analysis) {
            analysis = new Analysis()
            analysis.execution = execution
        }

        Integer n

        for (n = 0; n < max; n++) {
            if (!analysis.step()) break
            status()
        }

        return n
    }

    void status() {
        if (analysis) {
            println("Summary: \n" + analysis.storyBase.toLog())
            println("Stories: \n" + analysis.storyBase)
            println("States: \n" + analysis.stateBase)
        } else {
            println "Marking"
            execution.net.printMarking()
        }
    }

}
