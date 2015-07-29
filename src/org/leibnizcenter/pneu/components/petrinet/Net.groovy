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

import org.leibnizcenter.pneu.components.graphics.Grid

class Net {
    List<Transition> transitionList = []
    List<Place> placeList = []
    List<Arc> arcList = []
    List<Net> subNets = []

    Grid grid

    // for hierarchical distribution of nets
    Integer zIndex

    // for operations, these nodes are events/transitions,
    // for expressions, these are situations/places
    List<Node> inputs = []
    List<Node> outputs = []

    void include(Net net, Integer xPos = 0, Integer yPos = 0) {
        if (xPos != 0 || yPos != 0) {
            for (p in net.placeList) p.position.traslate(xPos, yPos)
            for (t in net.transitionList) t.position.traslate(xPos, yPos)
        }

        subNets << net
    }

    List<Place> getAllPlaces() {
        List<Place> allPlaces = []
        allPlaces += placeList
        for (subnet in subNets) {
            allPlaces += subnet.getAllPlaces()
        }
        allPlaces
    }

    List<Transition> getAllTransitions() {
        List<Transition> allTransitions = []
        allTransitions += transitionList
        for (subnet in subNets) {
            allTransitions += subnet.getAllTransitions()
        }
        allTransitions
    }

    List<Arc> getAllArcs() {
        List<Arc> allArcs = []
        allArcs += arcList
        for (subnet in subNets) {
            allArcs += subnet.getAllArcs()
        }
        allArcs
    }

    void setupGrid(Integer inputDotGranularity = 1, Integer outputDotGranularity = 33) {
        if (!grid) grid = new Grid()
        grid.setInputDotGranularity(inputDotGranularity)
        grid.setOutputDotGranularity(outputDotGranularity)

        for (p in placeList) {
            if (p.position)
              grid.testMinMax(p.position.x, p.position.y)
        }
        for (t in transitionList) {
            if (t.position)
              grid.testMinMax(t.position.x, t.position.y)
        }
    }

    void printTab(Integer level = 0, String c = " ") {
        for (int i = 0; i < level * 4; i++)
            print(c)
    }

    void print(Integer level = 0) {
        printTab(level); println("places: "+placeList)
        printTab(level); println("transitions: "+transitionList)
        printTab(level); println("arcs: "+arcList)
        printTab(level); println("subnets: ("+subNets.size()+")")
        for (subNet in subNets) {
            printTab(level+1, "-")
            print("--\n")
            subNet.print(level+1)
        }
        printTab(level); print("======\n")
        printTab(level); println("inputs: "+inputs)
        printTab(level); println("outputs: "+outputs)

    }

    // to remember the original name of the file
    String sourceName
    String sourceFile
}
