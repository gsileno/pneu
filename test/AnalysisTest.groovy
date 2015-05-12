

import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration
import org.leibnizcenter.pneu.animation.monolithic.execution.ExecutionMode
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

class AnalysisTest extends GroovyTestCase {

//    void test0EmptyPlace() {
//        Net net = PNML2PN.parseFile("examples/basic/0emptyplace.pnml")
//
//        NetOrchestration orchestration = new NetOrchestration()
//        orchestration.load(net)
//        assert(orchestration.runAnalysis() == 0)
//        assert(orchestration.analysis.stateBase.base.size() == 1)
//        assert(orchestration.analysis.nFirings.size() == 0)
//    }
//
//    void test0PlaceFilledWith3Tokens() {
//        Net net = PNML2PN.parseFile("examples/basic/0placefilledwith3tokens.pnml")
//
//        NetOrchestration orchestration = new NetOrchestration()
//        orchestration.load(net)
//        assert(orchestration.runAnalysis() == 0)
//        assert(orchestration.analysis.stateBase.base.size() == 1)
//        assert(orchestration.analysis.nFirings.size() == 0)
//    }

//    void test8AnalysisConflictBase(NetOrchestration orchestration) {
//        Net net = PNML2PN.parseFile("examples/basic/8analysisconflict.pnml")
//        orchestration.load(net)
//        assert(orchestration.runAnalysis() == 6)
//
//        orchestration.status()
//        assert(orchestration.analysis.stateBase.base.size() == 5)
//        assert(orchestration.analysis.storySet.set.size() == 3)
//    }
//
//    void test8AnalysisConflictBase() {
//        NetOrchestration orchestration = new NetOrchestration()
//        test8AnalysisConflictBase(orchestration)
//    }

    void testComplicatedSaleStoryBase(NetOrchestration orchestration) {
        Net net = PNML2PN.parseFile("examples/story/complicatedsalestory.pnml")
        orchestration.load(net)
        orchestration.runAnalysis(200)

        orchestration.status()
        assert(orchestration.analysis.stateBase.base.size() == 65)
        assert(orchestration.analysis.storySet.set.size() == 47)
    }

    void testComplicatedSaleStoryBruteForce() {
        NetOrchestration orchestration = new NetOrchestration()
        testComplicatedSaleStoryBase(orchestration)
    }




}