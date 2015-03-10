package org.leibnizcenter.pneu.parser

import org.leibnizcenter.pneu.components.Arc
import org.leibnizcenter.pneu.components.Net
import org.leibnizcenter.pneu.components.Place
import org.leibnizcenter.pneu.components.Token
import org.leibnizcenter.pneu.components.Transition
import org.leibnizcenter.pneu.graphics.components.Area
import org.leibnizcenter.pneu.graphics.components.Point
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

            Integer x = rec.graphics.position.'@x'[0].toInteger()
            Integer y = rec.graphics.position.'@y'[0].toInteger()

            net.testMinMax(x, y)

            Transition t = new Transition(
                    id: rec.'@id',
                    name: rec.name.text(),
                    position: new Point(x: x, y: y),
                    dimension: new Area(x: rec.graphics.dimension.'@x'[0].toInteger(), y: rec.graphics.dimension.'@y'[0].toInteger())
            );
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

            String id = rec.'@source'.toString()
            def source = net.placeList.find{ it.id == id }
            if (!source) source = net.transitionList.find{ it.id == id }
            if (!source) println ("I haven't found the source node $id!")

            id = rec.'@target'.toString()
            def target = net.placeList.find{ it.id == id }
            if (!target) target = net.transitionList.find{ it.id == id }
            if (!target) println ("I haven't found the target node $id!")

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

            Arc a = new Arc(
                    id: rec.'@id',
                    source: source,
                    target: target,
                    pointList: pointList
            );
            net.arcList.add(a)
        }

        net
    }

    static void main(String[] args) {
        // args += "./mock/events.pnml";
        // args += "./mock/placewithtoken.pnml";

        Net net

        def cli = new CliBuilder(header:'\nOptions:', usage:'pneu [options] <pnmlfile>')
        // cli.P(longOpt:'svg', 'Create a SVG file')
        cli.L(longOpt:'latex', 'export to LaTeX (tikz)')
        cli.o(longOpt:'output', args:1, argName:'file', 'Set the output file')
        def options = cli.parse(args)

        List<String> inputFileList = options.arguments()
        String outputFile = options.o

        println("pneu - PNML petri net loader")

        if (options.arguments().size() == 0) {
            cli.usage()
        } else {

            for (file in inputFileList) {
                print("reading from file " + file + "... ");
                try {
                    net = parseFile(file);
                } catch (java.io.FileNotFoundException e) {
                    println("sorry, file " + file + " not found or not valid.");
                }

                print("petri net loaded... ")

                if (outputFile == 'false') outputFile = file.replaceFirst(~/\.[^\.]+$/, '') + ".tex"

                new File(outputFile).withWriter { out ->
                    out.println(PN2LaTeX.convertabsolute(net))
                }
                println("petri net exported to " + outputFile)

            }
        }
    }
}
