import org.leibnizcenter.pneu.comparison.Comparison
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

List<Net> netList = []

// netList = PNML2PN.parseFiles("../stories")

netList << PNML2PN.parseFile("../stories/SaleStory.pnml")
netList << PNML2PN.parseFile("../stories/FailedSale2Story.pnml")

Map<Integer, Integer, Float> labelComparisonValues = [:]

for (int i = 0; i < netList.size() ; i++) {
    for (int j = i; j < netList.size() ; j++) {
        labelComparisonValues[i, j] = Comparison.labelComparison(netList[i], netList[j])
    }
}

// printing functions
for (ArrayList<Integer> keys in labelComparisonValues.keySet()) {
    println("Comparing "+ netList.get(keys[0]).sourceFile+
            " with "+netList.get(keys[1]).sourceFile+": "+ labelComparisonValues.get(keys))
}
