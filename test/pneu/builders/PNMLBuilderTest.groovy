package pneu.builders

import pneu.components.petrinet.Net
import pneu.parsers.PNML2PN

class PNMLBuilderTest extends GroovyTestCase {

    void testBuilder() {
        Net sourceNet = PNML2PN.parseFile("./examples/story/actionscheme.pnml")

        PN2PNML.buildPNML(sourceNet)
    }

}