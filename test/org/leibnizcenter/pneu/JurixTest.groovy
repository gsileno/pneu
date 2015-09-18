package org.leibnizcenter.pneu

import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.decomposition.Alignment
import org.leibnizcenter.pneu.examples.MarketModel
import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.animation.monolithic.analysis.Analysis
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.decomposition.Subsumption

class JurixTest extends GroovyTestCase {

    static Analysis batchAnalysis(String filename, Net net) {

        Analysis analysis = null

        if (!analysis) {
            NetRunner runner = new NetRunner()
            runner.load(net)
            runner.analyse()
            runner.status()
            analysis = runner.analysis
        }

        analysis
    }

    void testGroundNetsExports() {
        MarketModel.basicSaleInstance1().exportToDot("groundSaleInstance")
        MarketModel.basicSaleModel().exportToDot("groundSaleModel")
        MarketModel.groundSaleNormativeModel().exportToDot("groundSaleNormativeModel")
        MarketModel.groundSaleScriptModel().exportToDot("groundSaleScriptModel")
    }

    void testGroundNetsAnalysis1() {
        String filename = "groundSaleInstance"
        Analysis analysis = batchAnalysis(filename, MarketModel.basicSaleInstance1())
        analysis.exportToLog(filename)
    }

    void testGroundNetsAnalysis2() {
        String filename = "groundSaleModel"
        Analysis analysis = batchAnalysis(filename, MarketModel.basicSaleModel())
        analysis.exportToLog(filename)
    }

    void testGroundNetsAnalysis3() {
        String filename = "groundSaleNormativeModel"
        Analysis analysis = batchAnalysis(filename, MarketModel.groundSaleNormativeModel())
        analysis.exportToLog(filename)
    }

    void testGroundNetsAnalysis4() {
        String filename = "groundSaleScriptModel"
        Analysis analysis = batchAnalysis(filename, MarketModel.groundSaleScriptModel())
        analysis.exportToLog(filename)
    }

    void testSubsumptionIdentity() {
        assert Subsumption.subsumes(MarketModel.basicSaleInstance1(), MarketModel.basicSaleInstance1())
        assert Subsumption.subsumes(MarketModel.basicSaleModel(), MarketModel.basicSaleModel())
        assert Subsumption.subsumes(MarketModel.groundSaleNormativeModel(), MarketModel.groundSaleNormativeModel())
        assert Subsumption.subsumes(MarketModel.groundSaleScriptModel(), MarketModel.groundSaleScriptModel())
    }

    void testPatternSubsumesInstance() {
        assert Subsumption.subsumes(MarketModel.basicSaleModel(), MarketModel.basicSaleInstance1())
    }

    void testInstanceDoesNotSubsumePattern() {
        assert !Subsumption.subsumes(MarketModel.basicSaleInstance1(), MarketModel.basicSaleModel())
    }

    void testPatternSubsumesBehaviouralMechanism() {
        assert Subsumption.subsumes(MarketModel.basicSaleModel(), MarketModel.groundSaleScriptModel())
    }

    void testBehaviouralMechanismDoesNotSubsumePattern() {
        assert !Subsumption.subsumes(MarketModel.groundSaleScriptModel(), MarketModel.basicSaleModel())
    }

    void testPatternSubsumesNormativeMechanism() {
        assert Subsumption.subsumes(MarketModel.basicSaleModel(), MarketModel.groundSaleNormativeModel())
    }

    void testNormativeMechanismDoesNotSubsumePattern() {
        assert !Subsumption.subsumes(MarketModel.groundSaleNormativeModel(), MarketModel.basicSaleModel())
    }

    void testNormativeMechanismSubsumeBehaviouralMechanism() {
        assert Subsumption.subsumes(MarketModel.groundSaleNormativeModel(), MarketModel.groundSaleScriptModel())
    }

    void testBehaviouralMechanismDoesNotSubsumeNormativeMechanism() {
        assert !Subsumption.subsumes(MarketModel.groundSaleScriptModel(), MarketModel.groundSaleNormativeModel())
    }

}