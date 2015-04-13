package org.leibnizcenter.pneu.parsers

import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.PlaceWeight
import org.leibnizcenter.pneu.components.petrinet.Token
import org.leibnizcenter.pneu.components.petrinet.Transition
import org.leibnizcenter.pneu.components.petrinet.TransitionType
import org.leibnizcenter.pneu.components.graphics.Area
import org.leibnizcenter.pneu.components.graphics.Point

class PNML2PN {

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
                    name: rec.name.text(), // (rec.name.text().length() > 0)?rec.name.text():rec.'@id',
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
                    name: rec.name.text(), // (rec.name.text().length() > 0)?rec.name.text():rec.'@id',
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

            Arc existing = net.arcList.find() { it.source == source && it.target == target }

            if (existing) {
              existing.weight++

              // alternative decoration, I expect more efficient
              if (fromTransitionToPlace) {
                  source.outputs.find { it.place == target }.weight++
              } else {
                  target.inputs.find { it.place == source }.weight++
              }

            } else {

                // check types
                ArcType arcType = ArcType.NORMAL
                if (rec.type) {
                    String type = rec.type.text.text()
                    if (type == "inhibitor")
                        arcType = ArcType.INHIBITOR
                    else if (type == "reset")
                        arcType = ArcType.RESET
                }

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
                        type: arcType,
                        weight: 1
                );
                net.arcList.add(a)

                if (arcType == ArcType.NORMAL) {
                    // alternative decoration, I expect more efficient
                    if (fromTransitionToPlace) {
                        source.outputs << new PlaceWeight(place: target, weight: 1)
                        target.inputs << source
                    } else {
                        target.inputs << new PlaceWeight(place: source, weight: 1)
                        source.outputs << target
                    }
                } else if (arcType == ArcType.INHIBITOR) {
                    if (fromTransitionToPlace) {
                        source.inhibitors << target
                    } else {
                        println("Something strange here. The inhibitor arc should have the opposite direction.")
                        target.inhibitors << source
                    }
                } else if (arcType == ArcType.RESET) {
                    if (fromTransitionToPlace) {
                        source.resets << target
                    } else {
                        println("Something strange here. The reset arc should have the opposite direction.")
                        target.resets << source
                    }
                }
            }

        }

        net
    }
}
