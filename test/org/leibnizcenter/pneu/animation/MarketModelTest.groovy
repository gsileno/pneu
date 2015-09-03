package org.leibnizcenter.pneu.animation

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.examples.MarketModel

class MarketModelTest extends GroovyTestCase {

    NetRunner runner = new NetRunner()

    void testBasicSaleInstance1() {
        runner.load(MarketModel.basicSaleInstance1())
        assert (runner.analyse() == 6)
        assert (runner.analysis.storyBase.getSize() == 1)
        assert (runner.analysis.storyBase.base[0].steps.size() == 7)
        assert (runner.analysis.stateBase.base.size() == 6)
        runner.analysis.exportToLog("BasicSaleInstance1")
    }

    void testBasicSaleModel() {
        runner.load(MarketModel.basicSaleModel())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 2)
        assert (runner.analysis.storyBase.base[0].steps.size() == 7)
        assert (runner.analysis.stateBase.base.size() == 7)
        runner.analysis.exportToLog("BasicSaleModel")
    }

    void testbasicSaleWith2Parties() {
        runner.load(MarketModel.basicSaleWith2Parties())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 3)
        assert (runner.analysis.storyBase.base[0].steps.size() == 9)
        assert (runner.analysis.stateBase.base.size() == 10)
        runner.analysis.exportToLog("BasicSaleWith2Parties")
    }

    void testBasicSaleWithWorld() {
        runner.load(MarketModel.basicSaleWithWorld())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 9)
        assert (runner.analysis.storyBase.base[0].steps.size() == 13)
        assert (runner.analysis.stateBase.base.size() == 20)
        runner.analysis.exportToLog("BasicSaleWithWorld")
    }

    void testBasicSaleWithWorldAndTimeline() {
        runner.load(MarketModel.basicSaleWithWorldAndTimeline())
        runner.analyse()
        assert (runner.analysis.storyBase.getSize() == 8)
        assert (runner.analysis.storyBase.base[0].steps.size() == 13)
        assert (runner.analysis.stateBase.base.size() == 19)
        runner.analysis.exportToLog("BasicSaleWithWorldAndTimeline")
    }


}