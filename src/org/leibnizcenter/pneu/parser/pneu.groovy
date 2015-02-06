package org.leibnizcenter.pneu.parser

import org.leibnizcenter.pneu.components.Arc
import org.leibnizcenter.pneu.components.Net
import org.leibnizcenter.pneu.components.Place
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
            Transition t = new Transition(
                    id: rec.'@id',
                    name: rec.name.text(),
                    position: new Point(x: rec.graphics.position.'@x'[0].toInteger(), y: rec.graphics.position.'@y'[0].toInteger()),
                    dimension: new Area(x: rec.graphics.dimension.'@x'[0].toInteger(), y: rec.graphics.dimension.'@y'[0].toInteger())
            );
            net.transitionList.add(t)
        }

        def pRecs = firstNet.place
        for (rec in pRecs) {
            Place p = new Place(
                    id: rec.'@id',
                    name: rec.name.text(),
                    position: new Point(x: rec.graphics.position.'@x'[0].toInteger(), y: rec.graphics.position.'@y'[0].toInteger()),
                    dimension: new Area(x: rec.graphics.dimension.'@x'[0].toInteger(), y: rec.graphics.dimension.'@y'[0].toInteger())
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

            Arc a = new Arc(
                    id: rec.'@id',
                    source: source,
                    target: target
            );
            net.arcList.add(a)
        }

        net
    }

    static void main(String[] args) {
        args += "./examples/events.pnml";

        Net net

        if (args.length == 0) {
            println("pneu - PNML petri net loader\nreading from standard input...");
            net = parseText(System.in);
        } else if (args.length == 1) {
            println("pneu - PNML petri net loader\nreading from file " + args[0] + "...");
            try {
                net = parseFile(args[0]);
            } catch (java.io.FileNotFoundException e) {
                println("sorry, file " + args[0] + " not found.");
            }
        } else {
            println("neu - PNML petri net loader\nUsage is one of:");
            println("\$ pneu < inputfile");
            println("OR");
            println("\$ pneu inputfile");
            return
        }

        println("petri net correctly loaded.")

        println(PN2LaTeX.convert(net))
    }
}
