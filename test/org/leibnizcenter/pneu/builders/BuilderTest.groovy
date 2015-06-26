package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

class BuilderTest extends GroovyTestCase {

    void testBuilder() {
        Net sourceNet = PNML2PN.parseFile("./examples/story/actionscheme.pnml")

        PN2PNML.buildPNML(sourceNet)
    }

}