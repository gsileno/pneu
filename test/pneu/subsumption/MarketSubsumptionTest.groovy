package pneu.subsumption

import pneu.animation.monolithic.analysis.Analysis
import pneu.animation.monolithic.analysis.Story
import pneu.examples.MarketModel

class MarketSubsumptionTest extends GroovyTestCase {


    void testTransitionStorySubsumption4() {

        Analysis generalAnalysis = Analysis.analyse(MarketModel.basicSaleModel())
        Analysis specificAnalysis = Analysis.analyse(MarketModel.basicSaleInstance1())

        assert generalAnalysis.storyBase.size == 2
        assert specificAnalysis.storyBase.size == 1

        // a - b - d - e   vs    b - c - d

        println generalAnalysis.toLog()
        println specificAnalysis.toLog()

        Story generalStory = generalAnalysis.storyBase.base[0]
        Story specificStory = specificAnalysis.storyBase.base[0]

        StorySubsumption outcome

        outcome = new StorySubsumption(generalStory, specificStory)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES

        outcome = new StorySubsumption(specificStory, generalStory)

        assert outcome.type() == StorySubsumption.Type.PARTIALLY_SUBSUMES

    }

}