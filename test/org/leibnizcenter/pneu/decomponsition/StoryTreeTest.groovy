package org.leibnizcenter.pneu.decomponsition

import org.leibnizcenter.pneu.animation.monolithic.analysis.Story
import org.leibnizcenter.pneu.decomposition.StoryTree
import org.leibnizcenter.pneu.decomposition.StoryTreeType

class StoryTreeTest extends GroovyTestCase {


    void test1() {
        Story st1 = new Story(id: "st1")
        Story st2 = new Story(id: "st2")

        StoryTree t1 = new StoryTree(story: st1)
        StoryTree t2 = new StoryTree(type: StoryTreeType.SEQ)

        t2.addLeave(t1)

        assert t1.story == st1
        assert t2.story == null
        assert t1.leaves.size() == 0
        assert t2.leaves.size() == 1

        t2.addLeave(st2)

        assert t2.leaves.size() == 2

        t2.addStoryBefore(st1)

        assert t2.leaves.size() == 3

        assert t2.leaves[0].story == st1
        assert t2.leaves[1].story == st1
        assert t2.leaves[2].story == st2

    }

}