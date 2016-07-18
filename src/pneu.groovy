import pneu.components.petrinet.Net
import pneu.builders.PN2LaTeX
import pneu.parsers.PNML2PN

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
cli.P(longOpt:'minplacesize', args:1, argName:'float number', 'min size for places (in mm.)')
cli.T(longOpt:'mintransitionsize', args:1, argName:'float number', 'min size for transitions (in mm.)')
cli.H(longOpt:'showhidden', 'show hidden ids when labels are not available')
cli.r(longOpt:'run', 'execute the model')
cli.o(longOpt:'output', args:1, argName:'file', 'Set the output file')
def options = cli.parse(args)

// resolving options

List<String> inputFileList = options.arguments()
String outputFile = options.o

Float zoomXRatio, zoomYRatio, minPlaceSize, minTransitionSize
if (options.X) zoomXRatio = options.X.toFloat()
else if (options.R) zoomXRatio = options.R.toFloat()
else zoomXRatio = defaultRatio
if (options.Y) zoomYRatio = options.Y.toFloat()
else if (options.R) zoomYRatio = options.R.toFloat()
else zoomYRatio = defaultRatio
if (options.P) minPlaceSize = options.P.toFloat()
else minPlaceSize = defaultSize
if (options.T) minTransitionSize = options.T.toFloat()
else minTransitionSize = defaultSize

Boolean showId = (options.H)
// main script

Net net

println("pneu - PNML lpetri net loader")

if (options.arguments().size() == 0) {
    cli.usage()
} else {
    for (file in inputFileList) {
        boolean error = false
        print("reading from file " + file + "... ");
        try {
            net = PNML2PN.parseFile(file)
        } catch (FileNotFoundException) {
            error = true
            println("sorry, file " + file + " not found or not valid.");
        }

        if (!error) {
            print("lpetri net loaded... ")

            if (options.L) {
                if (outputFile == 'false') outputFile = file.replaceFirst(~/\.[^\.]+$/, '') + ".tex"
                new File(outputFile).withWriter { out ->
                    out.println(PN2LaTeX.convertAbsolute(net))

                            // net, zoomXRatio, zoomYRatio, minPlaceSize, minTransitionSize, showId))
                }
                print("lpetri net exported to " + outputFile)
//                if (zoomXRatio.toString() != defaultRatio.toString() || zoomYRatio.toString() != defaultRatio.toString() || minSize.toString() != defaultSize.toString())
//                    print(" (zoom ratio: "+zoomXRatio+"/"+zoomYRatio+", place size: "+minPlaceSize+", transition size: "+minTransitionSize+")")
            }

            if (options.r) {
                println("running the lpetri net model... TODO")
            }
        }
        print("\n")
    }
}
