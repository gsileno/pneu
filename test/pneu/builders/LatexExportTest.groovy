package pneu.builders

import pneu.components.petrinet.Net
import pneu.parsers.PNML2PN

class LatexExportTest extends GroovyTestCase {

    void batchExport(String filename, String path = "examples/basic/") {
        Net net = PNML2PN.parseFile(path+"/"+filename)

        def folder = new File( 'out/tex/' )
        if( !folder.exists() ) folder.mkdirs()

        String outputFile = "out/tex/"+filename.replaceFirst(~/\.[^\.]+$/, '') + ".tex"

        new File(outputFile).withWriter {
            out -> out.println(PN2LaTeX.convertAbsolute(net))
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

    // for jurix 2015

    void testBuyerScript() {
        batchExport("agentsalePredicate2.pnml", "examples/models/")
    }


    void testNormativeModel() {
        batchExport("normativesalePredicate2.pnml", "examples/models/")
    }

    void testSaleModel() {
        batchExport("salemodel.pnml", "examples/models/")
    }

    void testSaleInstance() {
        batchExport("saleinstanceProposition.pnml", "examples/models/")
    }

}