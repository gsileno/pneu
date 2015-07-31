package org.leibnizcenter.pneu.components.petrinet

import groovy.transform.AutoClone
import groovy.transform.EqualsAndHashCode
import org.leibnizcenter.pneu.components.graphics.Grid

class Net {
    List<Transition> transitionList = []
    List<Place> placeList = []
    List<Arc> arcList = []
    List<Net> subNets = []

    Net parent

    Grid grid

    // for operations, these nodes are events/transitions,
    // for expressions, these are situations/places
    List<Node> inputs = []
    List<Node> outputs = []

    Net clone() {
        Net clone = new Net(transitionList: transitionList.collect(),
                placeList: placeList.collect(),
                arcList: arcList.collect(),
                inputs: inputs.collect(),
                outputs: inputs.collect())

        for (net in subNets) {
            clone.subNets << net.clone()
        }

        clone
    }

    void include(Net net, Integer xPos = 0, Integer yPos = 0) {
        if (xPos != 0 || yPos != 0) {
            for (p in net.placeList) p.position.traslate(xPos, yPos)
            for (t in net.transitionList) t.position.traslate(xPos, yPos)
        }

        net.parent = this
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

    List<Net> getAllNets() {
        List<Net> allNets = []
        allNets += this
        for (subnet in subNets) {
            allNets += subnet.getAllNets()
        }
        allNets
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
        printTab(level); println("places: " + placeList)
        printTab(level); println("transitions: " + transitionList)
        printTab(level); println("arcs: " + arcList)
        printTab(level); println("subnets: (" + subNets.size() + ")")
        for (subNet in subNets) {
            printTab(level + 1, "-")
            print("--\n")
            subNet.print(level + 1)
        }
        printTab(level); print("======\n")
        printTab(level); println("inputs: " + inputs)
        printTab(level); println("outputs: " + outputs)

    }

    // to remember the original name of the file
    String sourceName
    String sourceFile
}
