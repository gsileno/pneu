package org.leibnizcenter.pneu.subsumption

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.components.petrinet.Net

@Log4j
class NetSubsumption {

    Net generalNet
    Net specificNet

    Type type

    enum Type {
        NONE,
        SUBSUMES,
        PARTIALLY_SUBSUMES
    }

    NetSubsumption(Net inputGeneralNet, Net inputSpecificNet) {

        generalNet = inputGeneralNet
        specificNet = inputSpecificNet

        generalNet.exportToLog("generalNet")
        specificNet.exportToLog("specificNet")

        Analysis generalNetAnalysis = Analysis.analyse(generalNet)
        Analysis specificNetAnalysis = Analysis.analyse(specificNet)

        AnalysisSubsumption eval = new AnalysisSubsumption(generalNetAnalysis, specificNetAnalysis)

        if (eval.type == AnalysisSubsumption.Type.SUBSUMES) type = Type.SUBSUMES
        else if (eval.type == AnalysisSubsumption.Type.NONE) type = Type.NONE
        else throw new RuntimeException('Yet to be implemented')

        // ~ you could reconstruct the partial network which allow subsumtion
        // by removing the arcs of the last triggered transition... to be reflected later
    }

}

