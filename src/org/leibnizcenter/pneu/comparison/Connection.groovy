package org.leibnizcenter.pneu.comparison

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.Net

@Log4j
class Connection {
    String sourceId
    String targetId
    Integer nSourceInArcs = 0
    Integer nSourceOutArcs = 0
    Integer nTargetInArcs = 0
    Integer nTargetOutArcs = 0

    static List<Connection> readConnections(Net net) {
        List<Arc> arcs = net.arcList
        List<Connection> connections = []
        for (arc in arcs) {
            // a list with all connections but not in execution order.
            connections << new Connection(sourceId: arc.source.id, targetId: arc.target.id)
        }

        for (i in connections) {
            for (j in connections) {
                // println i.toString() + " with " + j.toString()

                if (i.sourceId == j.targetId) {
                    i.nSourceInArcs++
                }
                if (i.sourceId == j.sourceId) {
                    i.nSourceOutArcs++
                }
                if (i.targetId == j.sourceId) {
                    i.nTargetOutArcs++
                }
                if (i.targetId == j.targetId) {
                    i.nTargetInArcs++
                }
            }
        }
        connections
    }

    static List<Connection> searchBySourceId(List<Connection> connections, String id) {
        log.trace("searchBySourceId ### connections: $connections ### id $id")
        List<Connection> result = []
        for (connection in connections) {
            if (connection.sourceId == id) {
                result << connection
            }
        }
        return result
    }

    // search for nodes and delete or add an output arc on the source
    // useful for visiting nodes twice
    static List<Connection> searchBySourceIdAdd(List<Connection> connections, String id, Integer adding) {
        List<Connection> result = []
        for (connection in connections) {
            if (connection.sourceId == id) {
                connection.nSourceOutArcs = connection.nSourceOutArcs + adding
                result << connection
            }
        }
        return result
    }

    static List<Connection> searchByTargetId(List<Connection> connections, String id) {
        List<Connection> result = []
        for (connection in connections) {
            if (connection.targetId == id) {
                result << connection
            }
        }
        return result
    }

    // TOCHECK: is this correct? there may be places with no inputs/outputs
    static Connection searchCollector(List<Connection> connections) {
        for (connection in connections) {
            if (connection.nSourceInArcs == 0) {
                return connection
            }
        }
    }

    // remembering another path
    static List<Connection> remember(connections, node) {
        if (node.nSourceOutArcs > 1 && node.nSourceInArcs != 0) {
            List<Connection> rememberSourceNodes = searchBySourceIdAdd(connections, node.sourceId, -1)
            rememberSourceNodes.remove(node)
            return rememberSourceNodes
        } else {
            return []
        }
    }

    // finding next connection
    static def findNext(List<Connection> visitedConnections, List<Connection> connections, Connection node, List<Connection> remember) {
        log.trace("findNext. visited: $visitedConnections, connections: $connections, node: $node, remember: $remember")

        Connection next
        log.trace("Next: $next")
        Boolean backtrack = false

        for (connection in connections) {
            if (visitedConnections.contains(node) && connection.nSourceInArcs == 0) {
                next = connection
            } else {
                if (connection.sourceId == node.targetId && connection.targetId != node.sourceId && node.nTargetOutArcs != 0) {
                    next = connection
                } else if (node.nTargetOutArcs == 0 && connection.nSourceInArcs == 0) {
                    next = connection
                }
            }
        }

        if (!next) {

            // if no new connection found, look in visitedConnection
            // if remembered node is used, other node needs to be remembered as well
            List<Connection> nextN = searchBySourceId(visitedConnections, node.targetId)

            if (nextN.size() > 0) {
                if (!remember.isEmpty()) {
                    next = remember[0]
                    backtrack = true
                    log.trace("HI")
                } else {
                    for (connection in connections) {
                        if (connection.nSourceInArcs == 0) {
                            next = connection
                            backtrack = true
                        }
                    }
                }
            }

        }

        // TO CHECK: there is some NULL around
        if (node)
          visitedConnections << node

        return [next, backtrack]
    }

    String toString() {
        return sourceId + "{$nSourceInArcs,$nSourceOutArcs}->" + targetId + "{$nTargetInArcs,$nTargetOutArcs}"

    }
}
