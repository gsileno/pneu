import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN
import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration

/*
TO DO:
Read multiple files
Create nets

Labels:
Put places and transition together - cleaner
Word matching


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

// With empty nodes:
//def lengthPlace = places.size() + places1.size()

//Define length without empty nodes
def lengthP = 0 as Integer
def lengthP1 = 0 as Integer

for(place in places){
    if(place.name != ""){
        lengthP++
    }
}
for(place1 in places1){
    if(place1.name != ""){
        lengthP1++
    }
}

def lengthPlace = lengthP + lengthP1



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

//def lengthTrans = transitions.size() + transitions1.size()

def lengthT = 0 as Integer
def lengthT1 = 0 as Integer

for(transition in transitions){
    if(transition.name != ""){
        lengthT++
    }
}
for(transition1 in transitions1){
    if(transition1.name != ""){
        lengthT1++
    }
}

def lengthTrans = lengthT + lengthT1

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

/**********
 Structural

 - Use arcs to find target and source
 - Does not use labelling

 **********/
diff1 = 0
diff2 = 0

def totalNodes = places + transitions
def totalNodes1 = places1 + transitions1
def arcs = net.arcList
def arcs1 = net1.arcList
def connections = []
def connections1 = []
sim = 0

for(arc in arcs){
    def connection = [arc.source.id, arc.target.id]

    //A list with all connections but not in execution order.
    connections << connection
}
for(arc1 in arcs1){
    def connection1 = [arc1.source.id, arc1.target.id]

    //A list with all connections but not in execution order.
    connections1 << connection1
}

for(connection in connections){
    for(connection1 in connections1){
        if(connection[1]==connection1[1]) {
            println connection
            println connection1
            // look for source and target
            // insert node if necessary
        }
        diff++
    }
}


/**********
Execution

Compare movement
 **********/

NetOrchestration orchestration = new NetOrchestration()
NetOrchestration orchestration1 = new NetOrchestration()

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
