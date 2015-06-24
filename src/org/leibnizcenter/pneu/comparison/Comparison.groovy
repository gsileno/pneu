package org.leibnizcenter.pneu.comparison

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Node
import org.leibnizcenter.pneu.components.petrinet.Transition

import org.simmetrics.StringMetric;
import org.simmetrics.StringMetrics;

@Log4j
class Comparison {

    ///////////////// LABEL COMPARISON

    static Float labelComparison(Net sourceNet, Net targetNet) {
        List<Place> sourcePlaces = sourceNet.placeList
        List<Place> targetPlaces = targetNet.placeList

        List<Transition> sourceTransitions = sourceNet.transitionList
        List<Transition> targetTransitions = targetNet.transitionList

        //  With empty nodes:
        // Integer lengthPlace = sourcePlaces.size() + targetPlaces.size()

        // Define length without empty nodes
        Integer lengthSourcePlaces = sourcePlaces.findAll() { it.name != "" }.size()
        Integer lengthTargetPlaces = targetPlaces.findAll() { it.name != "" }.size()

        Integer lengthPlaces = lengthSourcePlaces + lengthTargetPlaces

        Integer diff1 = 0
        Float simPlace = 0
        Float simTrans = 0

        for (i in sourcePlaces) {
            Float sim = 0
            Integer diff2 = 0
            if (i.name != "") {
                for (j in targetPlaces) {
                    Integer diff = Math.abs(diff2 - diff1)
                    log.trace("comparing "+i.name + "=?=" + j.name)
                    if (i.name == j.name) {
                        sim = (2 * diff) / lengthPlaces
                        log.trace("found equal: $sim $diff $lengthPlaces")
                        simPlace = simPlace + sim
                    }
                    diff2++
                }
            }
            diff1++
        }

        Integer lengthSourceTransitions = sourceTransitions.findAll() { it.name != "" }.size()
        Integer lengthTargetTransitions = targetTransitions.findAll() { it.name != "" }.size()

        Integer lengthTransitions = lengthSourceTransitions + lengthTargetTransitions

        diff1 = 0

        for (i in sourceTransitions) {
            Float sim = 0
            Integer diff2 = 0
            if (i.name != "") {
                for (j in targetTransitions) {
                    Integer diff = Math.abs(diff2 - diff1)
                    if (i.name == j.name) {
                        sim = (2 * diff) / lengthTransitions
                        simTrans = simTrans + sim
                    }
                    diff2++
                }
            }
            diff1++
        }

        // Calculate label similarity
        return (2 * (simPlace + simTrans)) / (lengthPlaces + lengthTransitions)

    }

    static class NodeComparisonValue {
        Float value
        Boolean active

        String toString() { return "{$value, $active}" }
    }

    static class NodeComparisonKey {
        Node source
        Node target

        String toString() { return "{${source.id}, ${target.id}}" }
    }

    static Float labelComparison2(Net sourceNet, Net targetNet) {
        List<Place> sourcePlaces = sourceNet.placeList
        List<Place> targetPlaces = targetNet.placeList

        List<Transition> sourceTransitions = sourceNet.transitionList
        List<Transition> targetTransitions = targetNet.transitionList

        // source node, target node, similarity value, active
        Map<NodeComparisonKey, NodeComparisonValue> labelSimilarity = [:]

        StringMetric metric = StringMetrics.cosineSimilarity()

        // compute the similarity over all the nodes (no distinction between places and transitions)
        List<Node> sourceNodes = sourcePlaces + sourceTransitions
        List<Node> targetNodes = targetPlaces + targetTransitions

        // find how many nodes are labeled
        Integer nLabeledNodes = sourceNodes.findAll() { it.name != "" }.size() +
                               targetNodes.findAll() { it.name != "" }.size()

        for (Node i in sourceNodes) {
            for (Node j in targetNodes) {
                if (i.name && j.name)
//                    labelSimilarity[i, j] = metric.compare(i.name, j.name)
                  labelSimilarity[new NodeComparisonKey(source: i, target: j)] = new NodeComparisonValue(
                          value: metric.compare(i.name, j.name),
                          active: true
                  )
            }
        }

        // greedy algorithm
        Float max = 1
        Float similarity = 0

        while (max != 0) {
            Float newMax = 0

            // find max
            for (NodeComparisonKey key in labelSimilarity.keySet()) {
                NodeComparisonValue nodeComparisonValue = labelSimilarity[key]
                if (nodeComparisonValue.active) {
                    Float value = nodeComparisonValue.value
                    if (value == max) {
                        log.trace(key.toString() + " is a max ($max)")
                        similarity += value

                        // purge
                        for (NodeComparisonKey innerKey in labelSimilarity.keySet()) {
                            NodeComparisonValue nodeComparisonInnerValue = labelSimilarity[innerKey]
                            if (nodeComparisonInnerValue.active) {
                                if (key.source == innerKey.source || key.target == innerKey.target) {
                                    nodeComparisonInnerValue.active = false
                                    log.trace("remove " + key.toString())
                                }
                            }
                        }
                    } else {
                        if (newMax < value) {
                            log.trace("new potential max ($value)")
                            newMax = value
                        }
                    }
                }
            }

            max = newMax

        }

        // weighted by the number of labeled nodes
        return 2 * similarity / nLabeledNodes

    }

    ///////////////// STRUCTURAL COMPARISON


    /**********
     Structural

     - Use arcs to find target and source
     - Does not refer to labelling
     **********/

    def static structuralComparison(Net sourceNet, Net targetNet) {

        List<Connection> sourceConnections
        List<Connection> targetConnections

        // count the number of incoming and outgoing arcs for source net
        sourceConnections = Connection.readConnections(sourceNet)
        // count the number of incoming and outgoing arcs for target net
        targetConnections = Connection.readConnections(targetNet)

        // find begin point
        Connection sourceBeginPoint = Connection.searchCollector(sourceConnections)
        Connection targetBeginPoint = Connection.searchCollector(targetConnections)

        // initial values
        List<Connection> visitedSourceConnections = []
        List<Connection> visitedTargetConnections = []
        Connection sourceNode = sourceBeginPoint
        Connection targetNode = targetBeginPoint
        List<Connection> newConnections = []
        List<Connection> rememberSourceNodes = []
        List<Connection> rememberTargetNodes = []
        Integer addDiff = 0
        Integer delDiff = 0

        /*
          Inserting and deleting nodes if necessary,
          remember nodes if they have multiple arcs

          - Replaces nodes when visited into visitedConnection
          - finds next nodes depending on current node (end, beginning, remembered)
        */

        while (sourceConnections.size() > 0 && sourceNode && targetNode) { // ADDED CHECK FOR VOID NODES?

            Connection nextSourceConnection, nextTargetConnection
            Boolean sourceBacktrack, targetBacktrack

            log.trace("######################## New cycle: sourceNode $sourceNode, targetNode $targetNode.")

            if (sourceNode.nTargetOutArcs != 0 && targetNode.nTargetOutArcs != 0 && targetNode.nTargetOutArcs!= -1) {
                log.trace("the sources of the arcs have both a number of output > 0..")

                if (sourceNode.nSourceOutArcs == targetNode.nSourceOutArcs) {
                    log.trace("they have the same number of output arcs at the source")

                    (nextSourceConnection, sourceBacktrack) = Connection.findNext(visitedSourceConnections, sourceConnections, sourceNode, rememberSourceNodes)
                    (nextTargetConnection, targetBacktrack) = Connection.findNext(visitedTargetConnections, targetConnections, targetNode, rememberTargetNodes)

                    // println "Next " + nextConnection + nextTargetConnection
                    if (sourceBacktrack) {
                        log.trace("backtrack!")
                        nextTargetConnection = rememberTargetNodes[0]
                        rememberSourceNodes.remove(nextSourceConnection)
                        rememberTargetNodes.remove(nextTargetConnection)
                    } else if(targetBacktrack){
                        log.trace("backtrack!")
                        nextSourceConnection = rememberSourceNodes[0]
                        rememberSourceNodes.remove(nextSourceConnection)
                        rememberTargetNodes.remove(nextTargetConnection)
                    }

                    List<Connection> rSourceNodes = Connection.remember(sourceConnections, sourceNode)
                    List<Connection> rTargetNodes = Connection.remember(targetConnections, targetNode)
                    if (rSourceNodes.size() > 0) {
                        for (rSourceNode in rSourceNodes) {
                            for (rTargetNode in rTargetNodes) {
                                rememberSourceNodes << rSourceNode
                                rememberTargetNodes << rTargetNode
                                sourceConnections.remove(rSourceNode)
                                targetConnections.remove(rTargetNode)
                            }
                        }
                    }
                    log.trace("Next $nextSourceConnection $nextTargetConnection")
                } else if (sourceNode.nSourceOutArcs > targetNode.nSourceOutArcs) { // Add
                    log.trace("the first has more output than the second on the source, I add one the second")

                    Integer diff = sourceNode.nSourceOutArcs - targetNode.nSourceOutArcs
                    Connection.searchBySourceIdAdd(targetConnections, targetNode.sourceId, diff)

                    for (int i = 0; i < diff; i++) {
                        Connection newNode = new Connection(
                                sourceId: targetNode.sourceId,
                                targetId: 'n' + addDiff,
                                nSourceInArcs: targetNode.nSourceInArcs,
                                nSourceOutArcs: targetNode.nSourceOutArcs -1,
                                nTargetOutArcs: -1
                        )
                        newConnections.add(0,newNode)
                        addDiff++
                    }

                    (nextSourceConnection, sourceBacktrack) = Connection.findNext(visitedSourceConnections, sourceConnections, sourceNode, rememberSourceNodes)
                    (nextTargetConnection, targetBacktrack) = Connection.findNext(visitedTargetConnections, targetConnections, targetNode, rememberTargetNodes)

                    if (sourceBacktrack) {
                        log.trace("backtrack!")
                        nextTargetConnection = rememberTargetNodes[0]
                        rememberSourceNodes.remove(nextSourceConnection)
                        rememberTargetNodes.remove(nextTargetConnection)
                    }

                    List<Connection> rSourceNodes = Connection.remember(sourceConnections, sourceNode)
                    List<Connection> rTargetNodes = Connection.remember(targetConnections, targetNode)
                    if (rSourceNodes.size() > 0) {
                            rememberSourceNodes << rSourceNodes[0]
                            rememberTargetNodes << newConnections[0]

                            if (targetNode.nSourceOutArcs >= 2) {
                                rememberSourceNodes << rSourceNodes[1]
                                rememberTargetNodes << rTargetNodes[0]
                                sourceConnections.remove(rSourceNodes[1])
                                targetConnections.remove(rTargetNodes[0])
                            }
                            sourceConnections.remove(rSourceNodes[0])
                   }

                    println "Add " + nextSourceConnection + nextTargetConnection

                } else if (sourceNode.nSourceOutArcs < targetNode.nSourceOutArcs) { // del
                    log.trace("the first has less outputs than the second on the source, I remove from the second")

                    Integer diff = targetNode.nSourceOutArcs - sourceNode.nSourceOutArcs
                    List<Connection> connectionList = Connection.searchBySourceIdAdd(targetConnections, targetNode.sourceId, -diff)

                    for (int i = 0; i < diff; i++) {
                        connectionList.remove(targetNode)
                        if (connectionList[0])  // TOCHECK: there is some NULL around!
                          visitedTargetConnections << connectionList[0]
                        targetConnections.remove(connectionList[0])
                        connectionList.remove(connectionList[0])
                        delDiff++
                    }

                    (nextSourceConnection, sourceBacktrack) = Connection.findNext(visitedSourceConnections, sourceConnections, sourceNode, rememberSourceNodes)
                    (nextTargetConnection, targetBacktrack) = Connection.findNext(visitedTargetConnections, targetConnections, targetNode, rememberTargetNodes)

                    if (sourceBacktrack) {
                        nextTargetConnection = rememberTargetNodes[0]
                        rememberSourceNodes.remove(nextSourceConnection)
                        rememberTargetNodes.remove(nextTargetConnection)
                    }
                    List<Connection> rSourceNodes = Connection.remember(sourceConnections, sourceNode)
                    List<Connection> rTargetNodes = Connection.remember(targetConnections, targetNode)

                    if (rSourceNodes.size() > 0) {
                        for(rSourceNode in rSourceNodes){
                            for(rTargetNode in rTargetNodes){
                                rememberSourceNodes << rSourceNode
                                rememberTargetNodes << rTargetNode
                                sourceConnections.remove(rSourceNode)
                                targetConnections.remove(rTargetNode)
                            }
                        }
                    }

                    println "Del " + sourceNode + targetNode
                }

            // If one is an end node and the other one is not
            } else if (sourceNode.nTargetOutArcs > 0 && targetNode.nTargetOutArcs == 0) {
                log.trace("the first is not an end node, the second yes")

                Connection newNode = new Connection(
                        sourceId: "n"+addDiff,
                        targetId: targetNode.targetId,
                        nSourceInArcs: targetNode.nSourceInArcs,
                        nSourceOutArcs: 1
                )
                newConnections << newNode
                addDiff++

                (nextSourceConnection, sourceBacktrack) = Connection.findNext(visitedSourceConnections, sourceConnections, sourceNode, rememberSourceNodes)

                if (sourceBacktrack) {
                    nextTargetConnection = rememberTargetNodes[0]
                    rememberSourceNodes.remove(nextSourceConnection)
                    rememberTargetNodes.remove(nextTargetConnection)
                } else {
                    nextTargetConnection = newNode
                }

                List<Connection> rSourceNodes = Connection.remember(sourceConnections, sourceNode)
                if (rSourceNodes.size() > 0) {
                    for (rSourceNode in rSourceNodes) {
                        rememberSourceNodes << rSourceNode
                        rememberTargetNodes << targetNode
                    }
                }
            } else if (sourceNode.nTargetOutArcs == 0 && targetNode.nTargetOutArcs > 0){
                log.trace("the first is an end node, the second no")

                //delete node
                delDiff++

                nextSourceConnection = sourceNode
                (nextTargetConnection, targetBacktrack) = Connection.findNext(visitedTargetConnections, targetConnections, targetNode, rememberTargetNodes)

                //println "NODE " +targetNode + nextTargetConnection
                //DELETE ALL INSTANCES TILL 0 FROM targetConnections

                if (targetBacktrack) {
                    log.trace("backtrack!")
                    nextSourceConnection = rememberSourceNodes[0]
                    rememberSourceNodes.remove(nextSourceConnection)
                    rememberTargetNodes.remove(nextTargetConnection)
                } else {
                    nextSourceConnection = sourceNode
                }

                List<Connection> rTargetNodes = Connection.remember(targetConnections, targetNode)
                if (rTargetNodes.size() > 0) {
                    for (rSourceNode in rTargetNodes) {
                        rememberTargetNodes << rSourceNode
                        rememberSourceNodes << sourceNode
                    }
                }

            //if both are end nodes, find the next remembered node or start from beginning
            } else if (sourceNode.nTargetOutArcs == 0 && targetNode.nTargetOutArcs <= 0) {
                log.trace("they are both end nodes")

                (nextSourceConnection, sourceBacktrack) = Connection.findNext(visitedSourceConnections, sourceConnections, sourceNode, rememberSourceNodes)
                (nextTargetConnection, targetBacktrack) = Connection.findNext(visitedTargetConnections, targetConnections, targetNode, rememberTargetNodes)

                //println "Next line " + nextConnection + nextTargetConnection
                if(nextSourceConnection == null && !rememberSourceNodes.isEmpty()){
                    nextSourceConnection = rememberSourceNodes[0]
                    nextTargetConnection = rememberTargetNodes[0]
                }

                //REMEMBER IS [] THEN GO FROM THE END BACK TO BEGINNING - NEEDS TO BE IMPLEMENTED
            } else if (targetNode.nTargetOutArcs == -1) {
                log.trace("The new node needs more additions")
                Connection newNode = new Connection(
                        sourceId: sourceNode.sourceId,
                        targetId: "n"+addDiff,
                        nSourceInArcs: 1,
                        nSourceOutArcs: 1,
                        nTargetOutArcs: -1
                )

                newConnections.add(0,newNode)
                nextTargetConnection = newConnections[0]
                addDiff++
                (nextSourceConnection, sourceBacktrack) = Connection.findNext(visitedSourceConnections, sourceConnections, sourceNode, rememberSourceNodes)

                if (sourceBacktrack) {
                    log.trace("backtrack!")
                    nextTargetConnection = rememberTargetNodes[0]
                    rememberSourceNodes.remove(nextSourceConnection)
                    rememberTargetNodes.remove(nextTargetConnection)
                }
                List<Connection> rSourceNodes = Connection.remember(sourceConnections, sourceNode)

                if (rSourceNodes.size() > 0) {
                    for (rSourceNode in rSourceNodes){
                        rememberSourceNodes << rSourceNode
                        rememberTargetNodes << newNode
                        sourceConnections.remove(rSourceNode)
                    }
                }
            }

            sourceConnections.remove(sourceNode)
            targetConnections.remove(targetNode)

            Integer removeIndex = rememberSourceNodes.indexOf(sourceNode)
            rememberSourceNodes.remove(sourceNode)
            if (removeIndex >= 0) {
                rememberTargetNodes.remove(removeIndex)
            }
            sourceNode = nextSourceConnection
            targetNode = nextTargetConnection

            //println "Rem " + rememberNode + rememberNode1
            log.trace "sourceConnections: " + sourceConnections
            log.trace "targetConnections: " + targetConnections
            log.trace "visitedSourceConnections: " + visitedSourceConnections
            log.trace "visitedTargetConnections: " + visitedTargetConnections
            log.trace "rememberSourceNodes: " + rememberSourceNodes
            log.trace "rememberTargetNodes: " + rememberTargetNodes
            log.trace "delDiff: " + delDiff
            log.trace "addDiff: " + addDiff

        }

        Integer conSize = targetConnections.size()
        //println conSize
        delDiff = delDiff + conSize
        //println "End Total del " + delDiff

        return [addDiff, delDiff]
    }

}
