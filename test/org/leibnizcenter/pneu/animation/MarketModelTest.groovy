package org.leibnizcenter.pneu.animation

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.examples.MarketModel

class MarketModelTest extends GroovyTestCase {

    NetRunner runner = new NetRunner()

    void testBasicSaleInstance1() {
        runner.load(MarketModel.basicSaleInstance1())
        assert (runner.analyse() == 6)
        assert (runner.analysis.storyBase.getSize() == 1)
        runner.analysis.exportToLog("BasicSaleInstance1")
    }

    void testBasicSaleModel() {
        runner.load(MarketModel.basicSaleModel())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 2)
        runner.analysis.exportToLog("BasicSaleModel")
    }

    void testbasicSaleWith2Parties() {
        runner.load(MarketModel.basicSaleWith2Parties())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 3)
        runner.analysis.exportToLog("BasicSaleWith2Parties")
    }

    void testBasicSaleWithWorld() {
        runner.load(MarketModel.basicSaleWithWorld())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 9)
        runner.analysis.exportToLog("BasicSaleWithWorld")
    }

    void testBasicSaleWithWorldAndTimeline() {
        runner.load(MarketModel.basicSaleWithWorldAndTimeline())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 8)
        runner.analysis.exportToLog("BasicSaleWithWorldAndTimeline")
    }

}