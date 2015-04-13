import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.graphics.export.PN2LaTeX
import org.leibnizcenter.pneu.parsers.PNML2PN

// grid scaling ratio in latex export
double defaultRatio = 0.65
// terminal size in latex export (in mm)
double defaultSize = 5

// command line configuration

def cli = new CliBuilder(header:'\nOptions:', usage:'pneu [options] <pnmlfile>')
// cli.P(longOpt:'svg', 'export to SVG')
cli.L(longOpt:'latex', 'export to LaTeX (tikz)')
cli.R(longOpt:'ratio', args:1, argName:'float number', 'ratio for grid X/Y scaling')
cli.X(longOpt:'ratiox', args:1, argName:'float number', 'ratio for grid X scaling')
cli.Y(longOpt:'ratioy', args:1, argName:'float number', 'ratio for Y scaling')
cli.S(longOpt:'minsize', args:1, argName:'float number', 'min size for nodes (in mm.)')
cli.H(longOpt:'showhidden', 'show hidden ids when labels are not available')
cli.r(longOpt:'run', 'execute the model')
cli.o(longOpt:'output', args:1, argName:'file', 'Set the output file')
def options = cli.parse(args)

// resolving options

List<String> inputFileList = options.arguments()
String outputFile = options.o

Float zoomXRatio, zoomYRatio, minSize
if (options.X) zoomXRatio = options.X.toFloat()
else if (options.R) zoomXRatio = options.R.toFloat()
else zoomXRatio = defaultRatio
if (options.Y) zoomYRatio = options.Y.toFloat()
else if (options.R) zoomYRatio = options.R.toFloat()
else zoomYRatio = defaultRatio
if (options.S) minSize = options.S.toFloat()
else minSize = defaultSize

Boolean showId = (options.H)
// main script

Net net

println("pneu - PNML petri net loader")

if (options.arguments().size() == 0) {
    cli.usage()
} else {
    for (file in inputFileList) {
        boolean error = false
        print("reading from file " + file + "... ");
        try {
            net = PNML2PN.parseFile(file);
        } catch (FileNotFoundException) {
            error = true
            println("sorry, file " + file + " not found or not valid.");
        }

        if (!error) {
            print("petri net loaded... ")

            if (options.L) {
                if (outputFile == 'false') outputFile = file.replaceFirst(~/\.[^\.]+$/, '') + ".tex"
                new File(outputFile).withWriter { out ->
                    out.println(PN2LaTeX.convertabsolute(net, zoomXRatio, zoomYRatio, minSize, showId))
                }
                print("petri net exported to " + outputFile)
                if (zoomXRatio.toString() != defaultRatio.toString() || zoomYRatio.toString() != defaultRatio.toString() || minSize.toString() != defaultSize.toString())
                    print(" (zoom ratio: "+zoomXRatio+"/"+zoomYRatio+", size: "+minSize+")")
            }

            if (options.r) {
                println("running the petri net model... TODO")
            }
        }
    }
}
