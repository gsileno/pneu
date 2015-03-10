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

package org.leibnizcenter.pneu.components

class Net {
    List<Transition> transitionList = []
    List<Place> placeList = []
    List<Arc> arcList = []

    // Grid dimensions
    Integer minX, maxX
    Integer minY, maxY

    void testMinMax(Integer x, Integer y) {
        if (minX == null) minX = x
        else if (x < minX) minX = x
        if (maxX == null) maxX = x
        else if (x > maxX) maxX = x
        if (minY == null) minY = y
        else if (y < minY) minY = y
        if (maxY == null) maxY = y
        else if (y > maxY) maxY = y
    }

}
