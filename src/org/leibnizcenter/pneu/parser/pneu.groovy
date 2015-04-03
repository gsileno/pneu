package org.leibnizcenter.pneu.parser

import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration
import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.PlaceWeight
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType
import org.leibnizcenter.pneu.animation.actorbased.NetChoreography
import org.leibnizcenter.pneu.components.graphics.Area
import org.leibnizcenter.pneu.components.graphics.Point
import org.leibnizcenter.pneu.graphics.export.PN2LaTeX

class pneu {

    static Net parseFile(String filename) {
        def records = new XmlParser().parse(filename)
        loadPNML(records)
    }

    static Net parseText(String text) {
        def records = new XmlParser().parseText(text)
        loadPNML(records)
    }

    static Net loadPNML(records) {
        def firstNet = records.net[0]

        Net net = new Net()

        def tRecs = firstNet.transition
        for (rec in tRecs) {

            // for graphics
            Integer x = rec.graphics.position.'@x'[0].toInteger()
            Integer y = rec.graphics.position.'@y'[0].toInteger()

            net.testMinMax(x, y)

            Transition t = new Transition(
                    id: rec.'@id',
                    name: rec.name.text(),
                    position: new Point(x: x, y: y),
                    dimension: new Area(x: rec.graphics.dimension.'@x'[0].toInteger(), y: rec.graphics.dimension.'@y'[0].toInteger())
            );

            if (rec.toolspecific.emitor)
                t.type = TransitionType.EMITTER
            else if (rec.toolspecific.collector)
                t.type = TransitionType.COLLECTOR

            net.transitionList.add(t)
        }

        def pRecs = firstNet.place
        for (rec in pRecs) {

            // artifice to put a set of tokens with no data in the marking of the place
            Integer n = 0
            String nmarking = rec.initialMarking.text.text()
            if (nmarking.length() > 0) n = nmarking.toInteger()

            List<Token> marking = []
            for (int j=0; j<n; j++) {
                marking << new Token()
            }

            // for graphics

            Integer x = rec.graphics.position.'@x'[0].toInteger()
            Integer y = rec.graphics.position.'@y'[0].toInteger()

            net.testMinMax(x, y)

            Place p = new Place(
                    id: rec.'@id',
                    name: rec.name.text(),
                    position: new Point(x: x, y: y),
                    dimension: new Area(x: rec.graphics.dimension.'@x'[0].toInteger(), y: rec.graphics.dimension.'@y'[0].toInteger()),
                    marking: marking
            );
            net.placeList.add(p)
        }

        def arcRecs = firstNet.arc
        for (rec in arcRecs) {

            // type for arc
            boolean fromTransitionToPlace = true

            String id = rec.'@source'.toString()
            def source = net.placeList.find{ it.id == id }
            if (source) fromTransitionToPlace = false
            else source = net.transitionList.find{ it.id == id }

            if (!source) println ("I haven't found the source node $id!")

            id = rec.'@target'.toString()
            def target = net.placeList.find{ it.id == id }
            if (!target) target = net.transitionList.find{ it.id == id }
            if (!target) println ("I haven't found the target node $id!")

            // for graphics

            List<Point> pointList = []

            def pointRecs = rec.graphics.position.findAll()

            for (pointRec in pointRecs) {
                Integer x = pointRec.'@x'.toInteger()
                Integer y = pointRec.'@y'.toInteger()

                net.testMinMax(x, y)

                Point point = new Point(
                        x: x,
                        y: y
                )
                pointList << point
            }

            // materialization

            Arc a = new Arc(
                    id: rec.'@id',
                    source: source,
                    target: target,
                    pointList: pointList,
                    weight: 1 //TODO to add the recognition of the weight
            );
            net.arcList.add(a)

            // alternative decoration, I expect more efficient
            if (fromTransitionToPlace) {
                source.outputs << new PlaceWeight(target, 1)
                target.inputs << source
            } else {
                target.inputs << new PlaceWeight(source, 1)
                source.outputs << target
            }

        }

        net
    }

    static void main(String[] args) {

        Net net

        def cli = new CliBuilder(header:'\nOptions:', usage:'pneu [options] <pnmlfile>')
        // cli.P(longOpt:'svg', 'Create a SVG file')
        cli.L(longOpt:'latex', 'export to LaTeX (tikz)')
        cli.r(longOpt:'run', 'execute the model')
        cli.o(longOpt:'output', args:1, argName:'file', 'Set the output file')
        def options = cli.parse(args)

        List<String> inputFileList = options.arguments()
        String outputFile = options.o

        println("pneu - PNML petri net loader")

        if (options.arguments().size() == 0) {
            cli.usage()
        } else {

            for (file in inputFileList) {
                boolean error = false
                print("reading from file " + file + "... ");
                try {
                    net = parseFile(file);
                } catch (FileNotFoundException) {
                    error = true
                    println("sorry, file " + file + " not found or not valid.");
                }

                if (!error) {
                    print("petri net loaded... ")

                    if (options.L) {
                        if (outputFile == 'false') outputFile = file.replaceFirst(~/\.[^\.]+$/, '') + ".tex"

                        new File(outputFile).withWriter { out ->
                            out.println(PN2LaTeX.convertabsolute(net))
                        }
                        println("petri net exported to " + outputFile)
                    }

                    if (options.r) {
                        println("running the petri net model...")

                        NetOrchestration orchestration = new NetOrchestration()
                        orchestration.embody(net)
                        orchestration.run()
                    }
                }
            }
        }
    }
}
