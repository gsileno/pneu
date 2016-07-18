package pneu.parsers

import pneu.builders.PN2LaTeX
import pneu.components.petrinet.Net

class PNMLImporterTest extends GroovyTestCase {

    void batchExport(String filename, String path = "examples/models") {
        Net net = PNML2PN.parseFile(path+"/"+filename)

        def folder = new File( 'out/tex/' )
        if( !folder.exists() ) folder.mkdirs()

        String outputFile = "out/tex/"+filename.replaceFirst(~/\.[^\.]+$/, '') + ".tex"

        new File(outputFile).withWriter {
            out -> out.println(PN2LaTeX.convertAbsolute(net))
        }
        println("lpetri net exported to " + outputFile)
    }

    void testConvertToLaTeX() {
        batchExport("normativesalePredicate.pnml")
    }
}