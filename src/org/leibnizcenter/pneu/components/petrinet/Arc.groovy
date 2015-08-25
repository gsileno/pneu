package org.leibnizcenter.pneu.components.petrinet

import org.leibnizcenter.pneu.components.graphics.Point

class Arc {

    String id

    Node source
    Node target

    Integer weight = 1
    ArcType type = ArcType.NORMAL

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

    static Arc buildResetArc(Transition t1, Place p1) {
        return buildArc(t1, p1, ArcType.RESET)
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

    // a link arc is an implicit biflow arc
    // while a link place/transition is one whose content is defined by the others
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

    static Boolean compare(Arc a1, Arc a2) {
        if (a1 == a2) return true
        if (a1.source.class != a2.source.class) return false
        if (a1.type != a2.type) return false
        if (a1.weight != a2.weight) return false
        if (a1.target.class != a2.target.class) return false  // not useful
        if (!a1.source.class.compare(a1.source, a2.source)) return false
        if (!a2.source.class.compare(a1.target, a2.target)) return false
        return true
    }

    // for graphics
    List<Point> pointList = []

    String toString() {
        String sourceLabel = source.toString()
        String targetLabel = target.toString()

        if (source.isTransitionLike()) sourceLabel = "|${sourceLabel}|"
        if (target.isTransitionLike()) targetLabel = "|${targetLabel}|"

        if (type == ArcType.NORMAL) sourceLabel + " -> " + targetLabel
        else if (type == ArcType.RESET) sourceLabel + " -- " + targetLabel
        else if (type == ArcType.INHIBITOR) sourceLabel + " -o " + targetLabel
        else if (type == ArcType.LINK) sourceLabel + " -- " + targetLabel // TO CHANGE
        else throw new RuntimeException("Arc type not recognized.")
    }

}
