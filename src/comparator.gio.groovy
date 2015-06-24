import org.leibnizcenter.pneu.comparison.Comparison
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

List<Net> netList = []

netList = PNML2PN.parseFiles("../stories")

// netList << PNML2PN.parseFile("../stories/SaleStory.pnml")
// netList << PNML2PN.parseFile("../stories/FailedSale2Story.pnml")
// netList << PNML2PN.parseFile("../stories/BurglaryStory.pnml")
// netList << PNML2PN.parseFile("../stories/BurglaryTheftStory.pnml")

///////// LABEL SIMILARITY 1

println("######################## \t Label similarity 1")

Map<Integer, Integer, Float> labelComparisonValues = [:]

for (int i = 0; i < netList.size() ; i++) {
    for (int j = i; j < netList.size() ; j++) {
        labelComparisonValues[i, j] = Comparison.labelComparison(netList[i], netList[j])
    }
}

// printing functions
for (ArrayList<Integer> keys in labelComparisonValues.keySet()) {
    println(netList.get(keys[0]).sourceFile+
            "\t"+netList.get(keys[1]).sourceFile+": "+ labelComparisonValues.get(keys))
}

///////// LABEL SIMILARITY 2

println("######################## \t Label similarity 2")

Map<Integer, Integer, Float> labelComparison2Values = [:]

for (int i = 0; i < netList.size() ; i++) {
    for (int j = i; j < netList.size() ; j++) {
        labelComparison2Values[i, j] = Comparison.labelComparison2(netList[i], netList[j])
    }
}

// printing functions
for (ArrayList<Integer> keys in labelComparison2Values.keySet()) {
    println(netList.get(keys[0]).sourceFile+
            "\t"+netList.get(keys[1]).sourceFile+": "+ labelComparison2Values.get(keys))
}

///////// STRUCTURAL SIMILARITY

println("######################## \t Structural similarity 1")

Map<Integer, Integer, Integer> structuralComparisonValues = [:]

for (int i = 0; i < netList.size() ; i++) {
    for (int j = i; j < netList.size() ; j++) {
        int addDiff, delDiff
        (addDiff, delDiff) = Comparison.structuralComparison(netList[i], netList[j])
        structuralComparisonValues[i, j] = addDiff+delDiff
    }
}

// printing functions
for (ArrayList<Integer> keys in structuralComparisonValues.keySet()) {
    println(netList.get(keys[0]).sourceFile+
            "\t"+netList.get(keys[1]).sourceFile+": "+ structuralComparisonValues.get(keys))
}
