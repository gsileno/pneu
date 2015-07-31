package org.leibnizcenter.pneu.components.petrinet

import groovy.transform.AutoClone
import org.leibnizcenter.pneu.components.graphics.Point

class Arc {
    String id
    Node source
    Node target
    Integer weight = 1
    ArcType type = ArcType.NORMAL

    // for graphics
    List<Point> pointList = []

    static Arc buildArc(Place p1, Transition t1, type = ArcType.NORMAL) {
        Arc a = new Arc(source: p1, target: t1, type: type)

        p1.outputs << a
        t1.inputs << a

        return a
    }

    static Arc buildArc(Transition t1, Place p1, type = ArcType.NORMAL) {
        Arc a = new Arc(source: t1, target: p1, type: type)

        t1.outputs << a
        p1.inputs << a

        return a
    }

    static Arc buildInhibitorArc(Place p1, Transition t1) {
        return buildArc(p1, t1, ArcType.INHIBITOR)
    }

    static List<Arc> buildDiodeArcs(Transition t1, Place p1) {
        Arc a1 = buildArc(t1, p1)
        Arc a2 = buildInhibitorArc(p1, t1)
        return [a1, a2]
    }

    static List<Arc> buildBiflowArcs(Transition t1, Place p1) {
        Arc a1 = buildArc(t1, p1)
        Arc a2 = buildArc(p1, t1)
        return [a1, a2]
    }

    static List<Arc> buildBiflowArcs(Place p1, Transition t1) {
        return buildBiflowArcs(t1, p1)
    }

    static List<Arc> buildArcs(Place p1, Transition t1, Place p2, type = ArcType.NORMAL) {
        Arc a1 = buildArc(p1, t1, type)
        Arc a2 = buildArc(t1, p2, type)
        return [a1, a2]
    }

    static List<Arc> buildLinkArcs(Place p1, Transition t1, Place p2) {
        return buildArcs(p1, t1, p2, ArcType.LINK)
    }

    static List<Arc> buildArcs(Transition t1, Place p1, Transition t2, type = ArcType.NORMAL) {
        Arc a1 = buildArc(t1, p1, type)
        Arc a2 = buildArc(p1, t2, type)
        return [a1, a2]
    }

    static List<Arc> buildLinkArcs(Transition t1, Place p1, Transition t2) {
        return buildArcs(t1, p1, t2, ArcType.LINK)
    }

    String toString() {
        if (type == ArcType.NORMAL) source.toString() + " -> " + target.toString()
        else if (type == ArcType.RESET) source.toString() + " -- " + target.toString()
        else if (type == ArcType.INHIBITOR) source.toString() + " -o " + target.toString()
        else if (type == ArcType.LINK) source.toString() + " -- " + target.toString() // TO CHANGE
    }

}
