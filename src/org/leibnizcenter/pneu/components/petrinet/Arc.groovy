// ----------------------------------------------------------------------------
// Copyright (C) 2015 G. Sileno
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
// To contact the authors:
// http://www.leibnizcenter.org/~sileno
//----------------------------------------------------------------------------

package org.leibnizcenter.pneu.components.petrinet
import org.leibnizcenter.pneu.components.graphics.Point


class Arc {
    String id
    Node source
    Node target
    Integer weight = 1
    ArcType type = ArcType.NORMAL

    // for graphics
    List<Point> pointList = []

    static Arc buildArc(Place p1, Transition t1) {
        Arc a = new Arc(source: p1, target: t1)

        p1.outputs << a
        t1.inputs << a

        return a
    }

    static Arc buildArc(Transition t1, Place p1) {
        Arc a = new Arc(source: t1, target: p1)

        t1.outputs << a
        p1.inputs << a

        return a
    }

    static Arc buildInhibitorArc(Place p1, Transition t1) {
        Arc a = new Arc(source: p1, target: t1, type: ArcType.INHIBITOR)

        t1.inputs << a
        p1.outputs << a

        return a
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

    static List<Arc> buildArcs(Place p1, Transition t1, Place p2) {
        Arc a1 = buildArc(p1, t1)
        Arc a2 = buildArc(t1, p2)
        return [a1, a2]
    }

    static List<Arc> buildArcs(Transition t1, Place p1, Transition t2) {
        Arc a1 = buildArc(t1, p1)
        Arc a2 = buildArc(p1, t2)
        return [a1, a2]
    }

}
