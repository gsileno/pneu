import org.leibnizcenter.pneu.comparison.Comparison
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.parsers.PNML2PN

List<Net> netList = []

netList = PNML2PN.parseFiles("../stories")

// netList << PNML2PN.parseFile("../stories/SaleStory.pnml")
// netList << PNML2PN.parseFile("../stories/FailedSale2Story.pnml")
// netList << PNML2PN.parseFile("../stories/BurglaryStory.pnml")
// netList << PNML2PN.parseFile("../stories/BurglaryTheftStory.pnml")

File report

///////// LABEL SIMILARITY 1

println("######################## \t Label similarity 1")
report = new File('../out/report.labelsimilarity1.txt')

Map<Integer, Integer, Float> labelComparisonValues = [:]

for (int i = 0; i < netList.size() ; i++) {
    for (int j = i; j < netList.size() ; j++) {
        labelComparisonValues[i, j] = Comparison.labelComparison(netList[i], netList[j])
    }
}

// printing functions
for (ArrayList<Integer> keys in labelComparisonValues.keySet()) {
    String output = netList.get(keys[0]).sourceName+
            "\t"+netList.get(keys[1]).sourceName+"\t"+ labelComparisonValues.get(keys)+"\n"
    report << output
    print(output)
}

///////// LABEL SIMILARITY 2

println("######################## \t Label similarity 2")
report = new File('../out/report.labelsimilarity2.txt')

Map<Integer, Integer, Float> labelComparison2Values = [:]

for (int i = 0; i < netList.size() ; i++) {
    for (int j = i; j < netList.size() ; j++) {
        labelComparison2Values[i, j] = Comparison.labelComparison2(netList[i], netList[j])
    }
}

// printing functions
for (ArrayList<Integer> keys in labelComparison2Values.keySet()) {
    String output = netList.get(keys[0]).sourceName+
            "\t"+netList.get(keys[1]).sourceName+"\t"+labelComparison2Values.get(keys)+"\n"
    report << output
    print(output)
}

///////// STRUCTURAL SIMILARITY

println("######################## \t Structural similarity 1")
report = new File('../out/report.structuralsimilarity.txt')

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
    String output = netList.get(keys[0]).sourceName+
            "\t"+netList.get(keys[1]).sourceName+"\t"+structuralComparisonValues.get(keys)+"\n"
    report << output
    print(output)
}
