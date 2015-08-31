package org.leibnizcenter.pneu.examples

import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.basicpetrinet.BasicTransition
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Transition

class MarketModel {

    static Net basicSaleInstance1() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offers")
        Transition t2 = sale.createTransition("accepts")
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSaleInstance1")
        sale
    }

    static Net basicSaleInstance2() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offers")
        Transition t2 = sale.createTransition("accepts")
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, t3)
        sale.createBridge(t3, tOut)

        sale.exportToLog("basicSaleInstance2")
        sale
    }

    static Net basicSaleModel() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offers")
        Transition t2 = sale.createTransition("accepts")
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSale")
        sale
    }

    static Net basicSaleWith2Parties() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offers")
        Transition t1b = sale.propagateTransition(t1s)
        Transition t2b = sale.createTransition("accepts")
        Transition t2s = sale.propagateTransition(t2b)
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2s, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSale2Parties")
        sale
    }

    static Net basicSaleWithWorld() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offers")
        Transition t1 = sale.propagateTransition(t1s)
        Transition t1b = sale.propagateTransition(t1)
        Transition t2b = sale.createTransition("accepts")
        Transition t2 = sale.propagateTransition(t2b)
        Transition t2s = sale.propagateTransition(t2)
        Transition t3b = sale.createTransition("pays")
        Transition t3 = sale.propagateTransition(t3b)
        Transition t4s = sale.createTransition("delivers")
        Transition t4 = sale.propagateTransition(t4s)
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3b)
        sale.createBridge(t2s, t4s)
        sale.createBridge(t3, tOut)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSaleWithWorld")
        sale
    }

    static Net basicSaleWithWorldAndTimeline() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offers")
        Transition t1 = sale.propagateTransition(t1s)
        Transition t1b = sale.propagateTransition(t1)
        Transition t2b = sale.createTransition("accepts")
        Transition t2 = sale.propagateTransition(t2b)
        Transition t2s = sale.propagateTransition(t2)
        Transition t3b = sale.createTransition("pays")
        Transition t3 = sale.propagateTransition(t3b)
        Transition t4s = sale.createTransition("delivers")
        Transition t4 = sale.propagateTransition(t4s)
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3b)
        sale.createBridge(t2s, t4s)

        // this is the message layer: it gives the timeline
        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSaleWithWorldAndTimeline")
        sale
    }

}
