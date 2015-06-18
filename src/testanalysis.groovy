import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN
import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration

Net net = PNML2PN.parseFile("../stories/SaleStory.pnml")
NetOrchestration orchestration = new NetOrchestration()
orchestration.load(net)
orchestration.run(2)

println orchestration.execution.places