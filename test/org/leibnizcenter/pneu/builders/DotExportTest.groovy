package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.builders.PN2dot
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

class DotExportTest extends GroovyTestCase {
    void batchExport(String filename) {
        Net net = PNML2PN.parseFile("examples/basic/"+filename)

        def folder = new File( 'examples/out/dot/' )
        if( !folder.exists() ) folder.mkdirs()

        String outputFile = "examples/out/dot/"+filename.replaceFirst(~/\.[^\.]+$/, '') + ".dot"

        new File(outputFile).withWriter {
            out -> out.println(PN2dot.simpleConversion(net))
        }
        println("lpetri net exported to " + outputFile)
    }

    void test0EmptyPlace() {
        batchExport("0emptyplace.pnml")
    }

    void test0PlaceFilledWith3Tokens() {
        batchExport("0placefilledwith3tokens.pnml")
    }

    void test1Transition() {
        batchExport("1transition.pnml")
    }

    void test2Chaining() {
        batchExport("2chaining.pnml")
    }

    void test3Doublearc() {
        batchExport("3doublearc.pnml")
    }

    void test4Conflict() {
        batchExport("4conflict.pnml")
    }

    void test5Inhibitor() {
        batchExport("5inhibitor.pnml")
    }

    void test6Biflow() {
        batchExport("6biflow.pnml")
    }

    void test7Reset() {
        batchExport("7reset.pnml")
    }

    void test8analysisconflict() {
        batchExport("8analysisconflict.pnml")
    }
}
