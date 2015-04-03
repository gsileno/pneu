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

enum TransitionType { NORMAL, EMITTER, COLLECTOR }

// from the perspective of the transition,
// we can read the input/ouptut places and their weight
// this is decorated while parsing
// it is redundant with arc information but should be more efficient
class PlaceWeight {
    Place place
    Integer weight
}

class Transition extends Node {
    TransitionType type = TransitionType.NORMAL

    // after decoration
    // input/output places / arc weight
    List<PlaceWeight> inputs = []
    List<PlaceWeight> outputs = []

    boolean isEnabled() {
        if (type == TransitionType.EMITTER) return true

        for (elem in inputs) {
            if (elem.place.marking < elem.weight) {
                return false
            }
        }
        return true
    }

    void consumeInputTokens() {
        if (type == TransitionType.COLLECTOR) println("Collector "+name+" consumes 1 unit")

        for (elem in inputs) {
            elem.place.marking -=  elem.weight
        }
    }

    void produceOutputTokens() {
        if (type == TransitionType.EMITTER) println("Emitter "+name+" produces 1 unit")

        for (elem in outputs) {
            elem.place.marking +=  elem.weight
        }
    }
}
