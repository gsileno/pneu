package org.leibnizcenter.pneu.animation.monolithic.analysis

class StorySet {
    List<Story> set = []

    int addStory(Story story) {
        set << story
        set.size()
    }

    String toString() {
        String output = ""
        for (int i = 0; i<set.size(); i++) {
            output += set[i].toString() + "\n"
        }
        output
    }
}
