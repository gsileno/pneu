import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

class LogicProgrammingTest extends GroovyTestCase {

    void testBasic1(NetOrchestration orchestration) {

        orchestration.load(net)
        assert(orchestration.runAnalysis() == 92)

        orchestration.status()
        assert(orchestration.analysis.stateBase.base.size() == 61)
        assert(orchestration.analysis.storySet.set.size() == 36)
    }

    void testComplicatedSaleStoryBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        testComplicatedSaleStoryBase(orchestration)
    }




}