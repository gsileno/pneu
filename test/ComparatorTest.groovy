import org.leibnizcenter.pneu.comparison.Comparison
import org.leibnizcenter.pneu.comparison.Connection
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.parsers.PNML2PN

class ComparatorTest extends GroovyTestCase {

    void checkNodeOnConnections(List<Connection> connections, String id, Integer nInArcs, Integer nOutArcs) {
        Connection c
        c = connections.find() { it.sourceId == id }
        if (c) {
            assert (c.sourceId == id)
            assert (c.nSourceInArcs == nInArcs)
            assert (c.nSourceOutArcs == nOutArcs)
        }
        c = connections.find() { it.targetId == id }
        if (c) {
            assert (c.targetId == id)
            assert (c.nTargetInArcs == nInArcs)
            assert (c.nTargetOutArcs == nOutArcs)
        }
    }

    void testCountingLinks() {
        Net net = PNML2PN.parseFile("./stories/BurglaryStory.pnml")

        List<Connection> connections = Connection.readConnections(net)

        checkNodeOnConnections(connections, "tr1", 0, 3)  // emittor
        checkNodeOnConnections(connections, "tr2", 3, 0)  // collector
        checkNodeOnConnections(connections, "pl9", 1, 1)  // giving no consent place
        checkNodeOnConnections(connections, "tr3", 2, 1)  // no consent transition
        checkNodeOnConnections(connections, "tr10", 2, 3) // Stolen goods transition
        checkNodeOnConnections(connections, "pl11", 2, 2) // stealing place
    }

    void testStructuralComparison1() {
        Net sourceNet = PNML2PN.parseFile("./stories/tests/test11.pnml")
        Net targetNet = PNML2PN.parseFile("./stories/tests/test12.pnml")

        assert Comparison.structuralComparison(sourceNet, targetNet) == [0, 1]
    }

    void testStructuralComparison2() {
        Net sourceNet = PNML2PN.parseFile("./stories/tests/test11.pnml")
        Net targetNet = PNML2PN.parseFile("./stories/tests/test12.pnml")

        assert Comparison.structuralComparison(sourceNet, targetNet) == [0, 1]
    }


}