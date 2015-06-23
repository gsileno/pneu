import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN
import org.leibnizcenter.pneu.animation.monolithic.NetOrchestration

/*
TO DO:
Read multiple files

Labels:
Put places and transition together - cleaner
Word matching
- ZEROES?
- Diff?


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
    Net net = PNML2PN.parseFile("../stories/test1.pnml")
    Net net1 = PNML2PN.parseFile("../stories/test2.pnml")

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
/*def lengthP = 0 as Integer
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
*/
/**********
 Structural

 - Use arcs to find target and source
 - Does not use labelling

 Error: When it needs to go to the next node, but there is no beginnode
 anymore. It uses [] which fails.
 Probably goes wrong in findnext and remembering nodes with multiple outgoing arcs.

 **********/
def totalNodes = places + transitions
def totalNodes1 = places1 + transitions1
def arcs = net.arcList
def arcs1 = net1.arcList
def connections = []
def connections1 = []

//Creates arc lists with the id of places and transitions
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

//Calculates the number of incoming and outgoing arcs
for(connection in connections){
    connection[2] = 0 //Number of incoming arcs for source
    connection[3] = 0 //Number of outgoing arcs from source
    connection[4] = 0 //Number of outgoing arcs for target
    for(connectionN in connections){
        if(connection[0]==connectionN[1]){
            connection[2] = connection[2]+1
        }
        if(connection[0]==connectionN[0]){
            connection[3] = connection[3]+1
        }
        if(connection[1]==connectionN[0]){
            connection[4]=connection[4]+1
        }
    }
}
//Calculates the number of incoming and outgoing arcs
for(connection in connections1){
    connection[2] = 0 //Number of incoming arcs for source
    connection[3] = 0 //Number of outgoing arcs from source
    connection[4] = 0 //Number of outgoing arcs for target
    for(connectionN in connections1){
        if(connection[0]==connectionN[1]){
            connection[2] = connection[2]+1
        }
        if(connection[0]==connectionN[0]){
            connection[3] = connection[3]+1
        }
        if(connection[1]==connectionN[0]){
            connection[4]=connection[4]+1
        }
    }
}

//Begin point
def(beginPoint, beginPoint1) = searchBoth(connections, connections1, 2,0)

//initial values
def totalDiff = 0
def delDiff = 0
def visitedConnection = []
def visitedConnection1 = []
def node = beginPoint[0]
def node1 = beginPoint1[0]
def newConnections = []
def rememberNodes = []
def rememberNodes1 = []
def remNr = 0
def remNr1 = 0

println connections.size()
println connections1.size()

/*
    Inserting and deleting nodes if necessary,
    remember nodes if they have multiple arcs

    -Replaces nodes when visited into visitedConnection
    -finds next nodes depending on current node (end, beginning, remembered)

    -Outer For-loop is temporarily
 */

while(connections!=[]){
    println "Node " + node + node1
    if(node[4]!=0&&node1[4]!=0&&node1[4]!=-1){
        if(node[3]==node1[3]){
            (nextConnection, remNr) = findNext(visitedConnection, connections, node, rememberNodes)
            (nextConnection1, remNr1) = findNext(visitedConnection1, connections1, node1, rememberNodes1)
            //println "Next " + nextConnection + nextConnection1
            if(remNr == 1){
                nextConnection1 = rememberNodes1[0]
                rememberNodes.remove(nextConnection)
                rememberNodes1.remove(nextConnection1)
            }
            rememberNode = remember(connections, node)
            rememberNode1 = remember(connections1, node1)
            if(rememberNode!=[]){
                for(rnode in rememberNode){
                    for(rnode1 in rememberNode1){
                        rememberNodes << rnode
                        rememberNodes1 << rnode1
                        connections.remove(rnode)
                        connections1.remove(rnode1)
                    }
                }
            }
        }else if(node[3] > node1[3]){ //Add
            def diff = node[3]-node1[3]
            searchAdd(connections1,0,node1[0],diff)
            for(int i =0; i < diff; i++) {
                newNode = [node1[0], 'n' + totalDiff, node1[2], node1[3]-1, -1]
                newConnections << newNode
                totalDiff++
            }
            (nextConnection, remNr) = findNext(visitedConnection, connections, node, rememberNodes)
            (nextConnection1, remNr1) = findNext(visitedConnection1, connections1, node1, rememberNodes1)
            if(remNr == 1){
                nextConnection1 = rememberNodes1[0]
                rememberNodes.remove(nextConnection)
                rememberNodes1.remove(nextConnection1)
            }
            rememberNode = remember(connections, node)
            rememberNode1 = remember(connections1, node1)

            //IF NODE1[3] == 2 THEN REMEMBERNODES = NEWNODE AND THE OTHER CONNECTION
            //search(connections1,0,node1[0])
            if(rememberNode!=[]){
                rememberNodes << rememberNode[0]
                rememberNodes1 << newNode
                if(node1[3]>=2){
                    println "bla"
                    rememberNodes << rememberNode[1]
                    rememberNodes1 << rememberNode1[0]
                    connections.remove(rememberNode[1])
                    connections1.remove(rememberNode1[0])
                }
                connections.remove(rememberNode[0])
            }
            println "ADD "
            //println "Add " + nextConnection + nextConnection1
        }else if(node[3] < node1[3]) { //del
            def diff = node1[3] - node[3]
            searchAdd(connections1, 0, node1[0], -diff)
            connectionList = search(connections1, 0, node1[0])
            for (int i = 0; i < diff; i++) {
                for (connectionL in connectionList) {
                    if (connectionL[3] == 1 && connectionL[4] < 2&&connectionL!=node1) {
                        visitedConnection1 << connectionL
                        connections1.remove(connectionL)
                        delDiff++
                        println "DelCon " + connectionL + connectionList
                   }
                }
            }
            (nextConnection, remNr) = findNext(visitedConnection, connections, node, rememberNodes)
            (nextConnection1, remNr1) = findNext(visitedConnection1, connections1, node1, rememberNodes1)
            println "Del " + node + node1
        }
    //If one is an end node and the other one is not
    }else if(node[4]!=0&&node1[4]==0){
        newNode = ["n"+totalDiff,node1[1],node1[2],1,0]
        newConnections << newNode
        totalDiff++
        (nextConnection, remNr) = findNext(visitedConnection, connections, node, rememberNodes)
        //println "Remnr " + remNr
        if(remNr == 1){
            nextConnection1 = rememberNodes1[0]
            rememberNodes.remove(nextConnection)
            rememberNodes1.remove(nextConnection1)
        }else {
            nextConnection1 = newNode
        }
        rememberNode = remember(connections, node)
        if(rememberNode!=[]){
            for(rnode in rememberNode){
                rememberNodes << rnode
                rememberNodes1 << node1
            }
        }
    }else if(node[4]==0&&node1[4]>0){
        //delete node
        delDiff++
        nextConnection = node
        (nextConnection1, remNr1) = findNext(visitedConnection1,connections1,node1, rememberNodes1)
        //println "NODE " +node1 + nextConnection1
        //DELETE ALL INSTANCES TILL 0 FROM CONNECTIONS1

        if(remNr1 == 1){
            nextConnection = rememberNodes[0]
            rememberNodes.remove(nextConnection)
            rememberNodes1.remove(nextConnection1)
        }else {
            nextConnection = node
        }
        rememberNode1 = remember(connections1, node1)
        if(rememberNode1!=[]){
            for(rnode in rememberNode1){
                rememberNodes1 << rnode
                rememberNodes << node
            }
        }
    //if both are end nodes, find the next remembered node or start from beginning
    }else if(node[4]==0&&node1[4]<=0){
        (nextConnection, remNr) = findNext(visitedConnection, connections, node, rememberNodes)
        (nextConnection1, remNr1) = findNext(visitedConnection1,connections1, node1, rememberNodes1)
        //println "Next line " + nextConnection + nextConnection1
    }else if(node1[4]==-1){
        println "HI"
        newNode = [node1[0],"n"+totalDiff,1,1,-1]
        newConnections << newNode
        totalDiff++
        (nextConnection, remNr) = findNext(visitedConnection, connections, node, rememberNodes)
        if(remNr == 1){
            nextConnection1 = rememberNodes1[0]
            rememberNodes.remove(nextConnection)
            rememberNodes1.remove(nextConnection1)
        }
        rememberNode = remember(connections, node)
        //rememberNode1 = remember(connections1, node1)
        if(rememberNode!=[]){
            for(rnode in rememberNode){
                rememberNodes << rnode
                rememberNodes1 << newNode
                connections.remove(rnode)
            }
        }
    }

    connections.remove(node)
    connections1.remove(node1)
    removeInd = rememberNodes.indexOf(node)
    rememberNodes.remove(node)
    if(removeInd >= 0 ){
        rememberNodes1.remove(removeInd)
    }
    node = nextConnection
    node1 = nextConnection1
    //println "Rem " + rememberNode + rememberNode1
    println "Con " + connections
    println "Con1 " + connections1
    println " Vis " + visitedConnection
    println " Vis1 " + visitedConnection1
    println "Rem " + rememberNodes
    println "Rem1 " + rememberNodes1
    println "Total del " + delDiff
    println "Total add " + totalDiff
}
conSize = connections1.size()
println conSize
delDiff = delDiff + conSize
println "End Total del " + delDiff


def remember(connections, node){
    if(node[3]>1&&node[2]!=0){
        rememberNodes = search(connections,0,node[0])
        searchAdd(connections,0,node[0],-1)
        rememberNodes.remove(node)
        return rememberNodes
    }else{
        return []
    }
}


//Finding next connection
def findNext(visitedConnection, connections, node, remember){
    def next = []
    for(connection in connections) {
        if (visitedConnection.contains(node)&&connection[2]==0){
            next = connection
        }else{
            if (connection[0] == node[1] && connection[1]!=node[0]&&node[4]!=0) {
                next = connection
            } else if(node[4] == 0 && connection[2] == 0) {
                next = connection
            }
        }
        remNr = 0
    }
    //If no new connection found, look in visitedConnection
    //if remembered node is used, other node needs to be remembered as well
    if(next.isEmpty()){
        nextN = search(visitedConnection,0,node[1])
        if(!nextN.isEmpty()){
            for(connection in connections){
                if(!remember.isEmpty()){
                    next = remember[0]
                    remNr = 1
                }else if(connection[2]==0){
                    next = connection
                    remNr = 1
                }
            }
        }
    }
    visitedConnection << node
    return [next, remNr]
}

//Searching for objects in both stories
def search(connections, def place, def searchfor){
    def connectionList = []
    for(connection in connections){
        if(connection[place]==searchfor) {
            connectionList << connection
        }
    }
    return connectionList
}

//Search for nodes and delete or add an arc
//Useful for visiting nodes twice
def searchAdd(connections,def place, def searchfor, def adding){
    for(connection in connections) {
        if (connection[place] == searchfor) {
            connection[3] = connection[3] + adding
        }
    }
}

//Look in both stories for same nodes
//Used to find beginpoint

//CAN DONE SEPARATELY
def searchBoth(connections, connections1, def place, def searchfor){
    def connectionList = []
    def connectionList1 = []
    for(connection in connections){
        for(connection1 in connections1){
            if(connection1[place]==searchfor&&connection[place]==searchfor){
                connectionList << connection
                connectionList1 << connection1
            }
        }
    }
    connectionList = connectionList.unique()
    connectionList1 = connectionList1.unique()
    return [connectionList, connectionList1]
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
