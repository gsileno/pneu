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

import org.leibnizcenter.pneu.components.graphics.Area
import org.leibnizcenter.pneu.components.graphics.Point

abstract class Node {

    String id

    // input/output arcs
    List<Arc> inputs = []
    List<Arc> outputs = []

    Boolean isPlaceLike() {
        Place.isAssignableFrom(this.class)
    }

    Boolean isTransitionLike() {
        Transition.isAssignableFrom(this.class)
    }

    abstract label()

    // graphics
    Point position
    Area dimension

    static Boolean compare(Node n1, Node n2) {
        if (n1 == n2) return true
        return true
    }

    // for visualization purposes: for terminological connections
    Boolean isLink() { return false }

    // for visualization purposes: the function of a net is represented by a node.
    // if it is cluster, this means the net counts as a cluster
    Boolean isCluster() { return true }



}
