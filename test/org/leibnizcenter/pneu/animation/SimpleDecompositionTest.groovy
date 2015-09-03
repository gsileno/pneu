package org.leibnizcenter.pneu.animation

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.decomposition.SimpleSESEDecomposer
import org.leibnizcenter.pneu.examples.MarketModel

class SimpleDecompositionTest extends GroovyTestCase {

    static void batchDecompose(Net net) {
        NetRunner runner = new NetRunner()
        runner.load(net)
        runner.analyse()

        SimpleSESEDecomposer decomposer = new SimpleSESEDecomposer()
        decomposer.decompose(runner.analysis)
    }

    void testBasicSaleInstance1() {
        batchDecompose(MarketModel.basicSaleInstance1())
    }

    void testBasicSaleModel() {
        batchDecompose(MarketModel.basicSaleModel())
    }

    void testbasicSaleWith2Parties() {
        batchDecompose(MarketModel.basicSaleWith2Parties())
    }

    void testBasicSaleWithWorld() {
        batchDecompose(MarketModel.basicSaleWithWorld())
    }

    void testBasicSaleWithWorldAndTimeline() {
        batchDecompose(MarketModel.basicSaleWithWorldAndTimeline())
    }


}