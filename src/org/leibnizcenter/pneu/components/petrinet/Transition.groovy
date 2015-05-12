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

    // transition by default of normal type
    TransitionType type = TransitionType.NORMAL

    boolean isEmitter() {
        return (type == TransitionType.EMITTER)
    }

    boolean isCollector() {
        return (type == TransitionType.COLLECTOR)
    }

    // after decoration
    // input/output places / arc weight
    List<PlaceWeight> inputs = []
    List<PlaceWeight> outputs = []

    // for inhibitor arcs
    List<Place> inhibitors = []

    // for reset arcs
    List<Place> resets = []

    // Operational Semantics

    boolean isEnabled(boolean analysis = false) {

        if (inputs.size() == 0) {
            if (!analysis && isEmitter())
                return true
            else
                return false
        }

        for (p in inhibitors) {
            if (p.marking.size() > 0)
                return false
        }

        for (elem in inputs) {
            if (elem.place.marking.size() < elem.weight) {
                return false
            }
        }
        return true
    }

    void fire() {
        consumeInputTokens()
        produceOutputTokens()
    }

    void consumeInputTokens() {
        for (elem in inputs) {
            for (int i=0; i<elem.weight; i++) {
                elem.place.marking.pop()
            }
        }
    }

    void flushResetTokens() {
        for (p in resets) {
            p.flush()
        }
    }

    void produceOutputTokens() {
        for (elem in outputs) {
            for (int i=0; i<elem.weight; i++) {
                elem.place.marking.push(new Token())
            }
        }
    }

    String toString() {
        if (name != "") return name
        else return id
    }

}
