package org.leibnizcenter.pneu.components

import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.basicpetrinet.BasicPlace
import org.leibnizcenter.pneu.components.basicpetrinet.BasicTransition
import org.leibnizcenter.pneu.components.petrinet.Net

class ComparatorTest extends GroovyTestCase {

    void testCompare1() {
        Net net = new BasicNet()

        BasicPlace pIn = net.createPlace("in")
        BasicTransition tFunction = net.createTransition("function")
        BasicPlace pOut = net.createPlace("out")

        net.createBridge(pIn, tFunction, pOut)

        Net clone = net.minimalClone()

        assert Net.compare(net, clone)
    }

    void testCompare2() {
        Net n1 = new BasicNet()

        BasicPlace pIn = n1.createPlace("in")
        BasicTransition tFunction = n1.createTransition("function")
        BasicPlace pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        BasicPlace pIn2 = n2.createPlace("in")
        BasicTransition tFunction2 = n2.createTransition("function")
        BasicPlace pOut2 = n2.createPlace("out")
        n2.createBridge(pIn2, tFunction2, pOut2)

        assert Net.compare(n1, n2)
    }

    void testCompare3() {
        Net n1 = new BasicNet()

        BasicPlace pIn = n1.createPlace("in")
        BasicTransition tFunction = n1.createTransition("function")
        BasicPlace pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        BasicTransition tFunction2 = n2.createTransition("function")
        BasicPlace pOut2 = n2.createPlace("out")
        n2.createArc(tFunction2, pOut2)

        assert !Net.compare(n1, n2)
    }

    void testCompare4() {
        Net n1 = new BasicNet()

        BasicPlace pIn = n1.createPlace("in")
        BasicTransition tFunction = n1.createTransition("function")
        n1.createArc(pIn, tFunction)

        Net n2 = new BasicNet()

        BasicPlace pIn2 = n2.createPlace("in")
        BasicTransition tFunction2 = n2.createTransition("function")
        BasicPlace pOut2 = n2.createPlace("out")
        n2.createBridge(pIn2, tFunction2, pOut2)

        assert !Net.compare(n1, n2)
    }

    void testCompare5() {
        Net n1 = new BasicNet()

        BasicPlace pIn = n1.createPlace("in")
        BasicTransition tFunction = n1.createTransition("function")
        BasicPlace pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        BasicPlace pIn2 = n2.createPlace("in")
        BasicPlace pOut2 = n2.createPlace("out")
        n2.createBridge(pIn2, pOut2)

        assert !Net.compare(n1, n2)
    }

    void testCompare6() {
        Net n1 = new BasicNet()

        BasicPlace pIn = n1.createPlace("in")
        BasicTransition tFunction = n1.createTransition("function")
        BasicPlace pOut = n1.createPlace("out")
        n1.createBridge(pIn, tFunction, pOut)

        Net n2 = new BasicNet()

        BasicPlace pIn2 = n2.createPlace("in")
        BasicTransition tFunction2 = n2.createTransition("function")
        BasicPlace pOut2 = n2.createPlace("out")

        assert !Net.compare(n1, n2)
    }

}