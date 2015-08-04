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

import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import org.leibnizcenter.pneu.components.graphics.Area
import org.leibnizcenter.pneu.components.graphics.Point

class Node {
    String id
    String name

    // after decoration
    // input/output places / arc weight
    List<Arc> inputs = []
    List<Arc> outputs = []

    // this will be used in logic programming petri nets
    // for terminological connections
    Boolean isLink() {
        return false
    }

    // this is used for nets:
    // nodes are used to represent the function of the net
    // by default the net is a cluster for the visualization
    Boolean isCluster() {
        return true
    }

    Boolean hasPlaceLikeFunction() {
        Place.isAssignableFrom(this.class)
    }

    Boolean hasTransitionLikeFunction() {
        Transition.isAssignableFrom(this.class)
    }

    // graphics
    Point position
    Area dimension

    String label(Boolean showId = false) {
        if (name || !showId) return name
        else return id
    }
}
