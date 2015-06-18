import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN
import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration

/*
TO DO:
Read multiple files
Create nets

Labels:
Put places and transition together?
Word matching
Delete from length the "" ones

Execution:
Find and compare the tokens

 */

/*
Reading multiple files:

    // take all pnml files from the given directory
    def dir = new File("../stories"); def files = []
    dir.eachFileMatch(FileType.FILES, ~/.*\.pnml$/) {
        files << it.name
    }
    // order by name
    files.sort()
    for (filename in files) {
        println filename
    }
*/

//Read files
    Net net = PNML2PN.parseFile("../stories/SaleStory.pnml")
    Net net1 = PNML2PN.parseFile("../stories/FailedSale2Story.pnml")

    def places = net.placeList
    def transitions = net.transitionList

    def places1 = net1.placeList
    def transitions1 = net1.transitionList

    def diff1 = 0 as Integer
    def simPlace = 0 as Integer
    def simTrans = 0 as Integer

/*
Compare labels of places and transitions

Only whole phrase sentences are compared
 */
    def lengthPlace = places.size() + places1.size()

    for ( place in places ) {
        def sim = 0 as Integer
        def diff2 = 0
        for (place1 in places1) {
            diff = Math.abs(diff2 - diff1)
            if (place.name == place1.name && place.name != "") {
                sim = (2 * diff) / lengthPlace
                simPlace = simPlace + sim
            }
            diff2++
        }
        diff1++
    }

    def lengthTrans = transitions.size() + transitions1.size()
    diff1 = 0
    for ( transition in transitions ) {
        def diff2 = 0
        def sim = 0
        for (transition1 in transitions1) {
            if (transition.name == transition1.name && transition.name != "") {
                sim = (2 * diff) / lengthTrans
                simTrans = simTrans + sim
            }
            diff2++
        }
        diff1++
    }

//Calculate label similarity
    def simall = (2 * (simPlace + simTrans)) / (lengthPlace + lengthTrans)
    println "Label similarity: " + simall


/*
Execution

Compare movement
 */

def arcs = net.arcList
def connection = []
NetOrchestration orchestration = new NetOrchestration()
NetOrchestration orchestration1 = new NetOrchestration()

/*
for(arc in arcs){
    def theconnection = [arc.source.id, arc.target.id]

    //A list with all connections but not in execution order.
    connection << theconnection
}

println connection
*/

//Orchestration
orchestration.load(net)
orchestration1.load(net1)


for (int i = 1; i < 2 ; i++) {
    orchestration.run(i)
    orchestration1.run(i)

    def execution = orchestration.execution.places.marking
    def execution1 = orchestration1.execution.places.marking

    //Find and compare tokens
    //Give it a measure
    //Maybe look at labelling

    println "Sale "
    println orchestration.execution.places
    println "Failed Sale"
    println orchestration1.execution.places
}
