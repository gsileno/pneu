package org.leibnizcenter.pneu.decomponsition

import org.leibnizcenter.pneu.animation.monolithic.NetRunner
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.decomposition.SimpleSESEDecomposer
import org.leibnizcenter.pneu.decomposition.StoryTree
import org.leibnizcenter.pneu.examples.MarketModel

class MarketDecompositionTest extends GroovyTestCase {

    static void batchDecompose(Net net, String filename) {
        NetRunner runner = new NetRunner()
        runner.load(net)
        runner.analyse()

        SimpleSESEDecomposer decomposer = new SimpleSESEDecomposer()
        StoryTree tree = decomposer.decompose(runner.analysis)
        tree.exportToDot(filename)
        tree.exportToLog(filename)

    }

    void testBasicSaleInstance1() {
        batchDecompose(MarketModel.basicSaleInstance1(), "MDT.saleinstance")
    }

    void testBasicSaleModel() {
        batchDecompose(MarketModel.basicSaleModel(), "MDT.salemodel")
    }

    void testbasicSaleWith2Parties() {
        batchDecompose(MarketModel.basicSaleWith2Parties(), "MDT.salewith2parties")
    }

    void testBasicSaleWithWorld() {
        batchDecompose(MarketModel.basicSaleWithWorld(), "MDT.salewithworld")
    }

    void testBasicSaleWithWorldAndTimeline() {
        batchDecompose(MarketModel.basicSaleWithWorldAndTimeline(), "MDT.salewithworldandtimeline")
    }

    void testGroundSaleNormativeModel() {
        batchDecompose(MarketModel.groundSaleNormativeModel(), "MDT.salenormativemodel")
    }
}