import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.graphics.export.PN2LaTeX
import org.leibnizcenter.pneu.parsers.PNML2PN

class LatexExportTest extends GroovyTestCase {

    void batchExport(String filename) {
        Net net = PNML2PN.parseFile("examples/basic/"+filename)

        String outputFile = "examples/out/tex/"+filename.replaceFirst(~/ \.[^\.]+$ /, '') + ".tex"

        new File(outputFile).withWriter {
            out -> out.println(PN2LaTeX.convertabsolute(net))
        }
        println("petri net exported to " + outputFile)
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

}