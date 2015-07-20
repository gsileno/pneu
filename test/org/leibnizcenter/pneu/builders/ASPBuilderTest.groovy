package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

class ASPBuilderTest extends GroovyTestCase {

    void batchExport(String filename) {
        Net net = PNML2PN.parseFile("examples/basic/"+filename)

        def folder = new File( 'examples/out/asp/' )
        if( !folder.exists() ) folder.mkdirs()

        String outputFile = "examples/out/asp/"+filename.replaceFirst(~/\.[^\.]+$/, '') + ".lp"

        new File(outputFile).withWriter {
            out -> out.println(PN2ASP.buildSimulationModel(net))
        }
        println("petri net exported to " + outputFile)
    }

    void testBuilder0() {
        batchExport("0emptyplace.pnml")

    }

    void testBuilder0bis() {
        batchExport("0placefilledwith3tokens.pnml")
    }

    void testBuilder1() {
        batchExport("1transition.pnml")
    }

    void testBuilder2() {
        batchExport("2chaining.pnml")
    }

    void testBuilder3() {
        batchExport("3doublearc.pnml")
    }

    void testBuilder4() {
        batchExport("4conflict.pnml")
    }

    void testBuilder5() {
        batchExport("5inhibitor.pnml")
    }

    void testBuilder6() {
        batchExport("6biflow.pnml")
    }

    void testBuilder7() {
        batchExport("7reset.pnml")
    }

    void testBuilder8() {
        batchExport("8analysisconflict.pnml")
    }

}