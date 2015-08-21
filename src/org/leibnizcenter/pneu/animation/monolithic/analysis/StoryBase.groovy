package org.leibnizcenter.pneu.animation.monolithic.analysis

class StoryBase {
    List<Story> base = []

    int addStory(Story story) {
        base << story
        base.size()
    }

    String toString() {
        String output = ""
        for (int i = 0; i < base.size(); i++) {
            output += base[i].toString() + "\n"
        }
        output
    }

    String toLog() {
        String storyLog = ""
        Integer i = 0
        for (story in base) {
            storyLog += i.toString()+": "
            for (eventList in story.events) {
                for (event in eventList) {
                    if (event.label() != "") {
                        storyLog += event.label()+", "
                    }
                }
            }
            storyLog = storyLog[0..-3] + ".\n"
            i++
        }
        storyLog
    }

    Integer getSize() {
        base.size()
    }
}
