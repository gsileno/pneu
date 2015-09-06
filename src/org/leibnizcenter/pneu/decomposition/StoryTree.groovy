package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story

@Log4j
public class StoryTree {

    List<StoryTree> leaves = []
    StoryTree parent
    Story story
    String id = "root"

    StoryTreeType type

    // return the last part of the id
    private Integer lastSubId() {
        (id[id.lastIndexOf('_') + 1..-1]).toInteger()
    }

    void addLeaveBefore(StoryTree tree) {
        log.trace("add leave before current leaves. leave: " + tree)
        log.trace("current tree (pre): " + this)
        tree.resetId(id + "_" + "0")
        for (leave in leaves) {
            leave.resetId(id + "_" + (leave.lastSubId() + 1).toString())
        }
        leaves = [tree] + leaves
        tree.parent = this
        log.trace("current tree (post): " + this)
    }

    void addLeavesFromTrees(List<StoryTree> leaves) {
        for (tree in leaves) {
            addLeave(tree)
        }
    }

    void addLeaves(List<Story> storyList) {
        for (story in storyList) {
            addLeave(story)
        }
    }

    static StoryTree buildAltStoryTree(List<Story> stories) {
        log.trace("build alt story tree from: " + stories)
        StoryTree tree = new StoryTree(type: StoryTreeType.ALT)
        tree.addLeaves(stories)
        tree
    }

    static StoryTree createSeqStoryTree(List<Story> stories) {
        log.trace("build seq story tree from: " + stories)
        StoryTree tree = new StoryTree(type: StoryTreeType.SEQ)
        tree.addLeaves(stories)
        tree
    }

    void addLeave(Story story) {
        log.trace("build leave from story: " + story)
        log.trace("current tree (pre): " + this)
        if (!leaves) leaves = []
        StoryTree leave = new StoryTree(story: story, parent: this)
        leave.id = id + "_" + leaves.size()
        leaves << leave
        if (leaves.size() > 1 && type == null)
            throw new RuntimeException("You have to define the type of composition")
        log.trace("current tree (post): " + this)
    }

    private changePrefix(String oldPrefix, String newPrefix) {
//        log.trace("leave id before: ${id}")
        id = id.replaceFirst(oldPrefix, newPrefix)
//        log.trace("leave id after: ${id}")
        for (leave in leaves) {
            leave.changePrefix(oldPrefix, newPrefix)
        }
    }

    private resetId(String newId) {
//        log.trace("replace ${id} with ${newId}")
//        log.trace("root id before: ${id}")
        for (leave in leaves) {
            leave.changePrefix(id, newId)
        }
        id = newId
//        log.trace("root id after: ${id}")

    }

    void addLeave(StoryTree leave) {
        log.trace("build leave from story tree: " + leave)
        log.trace("current tree (pre): " + this)
        if (!leaves) leaves = []
        leave.resetId(id + "_" + leaves.size())
        leaves << leave
        leave.parent = this
        if (leaves.size() > 1 && type == null)
            throw new RuntimeException("You have to define the type of composition")
        log.trace("current tree (post): " + this)
    }

    void addStoryBefore(Story story) {
        addLeaveBefore(new StoryTree(story: story))
    }

    void addStoryTreeBefore(StoryTree storyTree) {
        addLeaveBefore(storyTree)
    }

    // dot output for graphviz
    void exportToDot(String filename, String path = "out/dot/") {
        exportToDot(this, filename, path)
    }

    static void exportToDot(StoryTree tree, String filename, String path = "out/dot/") {
        File folder
        String outputFile

        folder = new File(path)
        if (!folder.exists()) folder.mkdirs()

        outputFile = path + filename + ".dot"

        new File(outputFile).withWriter {
            out -> out.println(StoryTree2Dot.convert(tree))
        }
    }

    // dot output for graphviz
    void exportToLog(String filename, String path = "out/log/") {
        exportToLog(this, filename, path)
    }

    static void exportToLog(StoryTree tree, String filename, String path = "out/log/") {
        File folder
        String outputFile

        folder = new File(path)
        if (!folder.exists()) folder.mkdirs()

        outputFile = path + filename + ".log"

        new File(outputFile).withWriter {
            out -> out.println(tree.toLog())
        }
    }

    String toString() {
        String output = id + ":"

        if (story != null)
            output += story.id
        if (type != null)
            output += type.toString()
        if (leaves.size() > 0)
            output += leaves.toString()

        output
    }

    List<Story> getStories() {
        List<Story> stories = []

        if (story) stories << story
        else {
            for (leave in leaves) {
                stories += leave.getStories()
            }
        }
        stories
    }

    String toLog() {
        String output = "Summary: " + toString() + "\n"

        List<Story> stories = getStories()

        output += "Stories: \n"
        for (int i = 0; i < stories.size(); i++) {
            output += i + " (" + stories[i].id + "): " + stories[i] + "\n"
        }

        output
    }

}