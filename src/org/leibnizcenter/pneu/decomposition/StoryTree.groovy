package org.leibnizcenter.pneu.decomposition

import groovy.util.logging.Log4j
import org.leibnizcenter.pneu.animation.monolithic.analysis.State
import org.leibnizcenter.pneu.animation.monolithic.analysis.Story

@Log4j
public class StoryTree {

    List<StoryTree> leaves = []
    StoryTree parent
    Story story
    String id = "nd"

    StoryTreeType type

    // return the last part of the id
    private Integer lastSubId() {
        (id[id.lastIndexOf('_') + 1..-1]).toInteger()
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

    void addLeavesBefore(List<StoryTree> leaves) {
        for (tree in leaves) {
            addLeaveBefore(tree)
        }
    }

    void addLeaves(List<StoryTree> leaves) {
        for (tree in leaves) {
            addLeave(tree)
        }
    }

    void addStories(List<Story> storyList) {
        for (story in storyList) {
            addStory(story)
        }
    }

    void addStory(Story story) {
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

    void addStoryBefore(Story story) {
        addLeaveBefore(new StoryTree(story: story))
    }

    StoryTree addSeqStory(Story story) {
        log.trace("adding SEQ $story to $this")

        if (type == null) type = StoryTreeType.SEQ

        if (type == StoryTreeType.ALT) {
            return createSeqBranch(story)
        } else {
            if (leaves.size() > 0) {
                if (getLastStep() != story.steps.first())
                    throw new RuntimeException("These stories are not in sequence!")
            }
            addStory(story)
        }
        this
    }

    // return the first node of the last story of the story tree
    State getFirstStep() {
        getFirstStory().steps.first()
    }

    // return the first story of the story tree
    Story getFirstStory() {
        if (story != null) {
            story
        } else if (leaves.size() > 0) {
            leaves.first().getFirstStory()
        } else
            null
    }

    // return the last node of the last story of the story tree
    State getLastStep() {
        getLastStory().steps.last()
    }

    // return the last story of the story tree
    Story getLastStory() {
        if (story != null) {
            story
        } else if (leaves.size() > 0) {
            leaves.last().getLastStory()
        } else
            null
    }

    StoryTree addAltStory(Story story) {
        log.trace("adding ALT $story to $this")

        if (type == null) type = StoryTreeType.ALT

        if (type == StoryTreeType.SEQ) {
            return createAltBranch(story)
        } else {
            if (leaves.size() > 0) {
                if (leaves.first().getFirstStep() != story.steps.first())
                    throw new RuntimeException("These alternatives do no share the same beginning!")
            }
            addStory(story)
        }
        this
    }

    // add a seq story, if the current story is an alt, create an alt branch and returns it
    StoryTree createSeqBranch(Story story) {
        StoryTree currentStoryTree = this
        StoryTree seqStoryTree = buildSeqStoryTree([story])
        log.trace("adding SEQ ${seqStoryTree} to ${currentStoryTree}")
        currentStoryTree.addLeave(seqStoryTree)
        seqStoryTree
    }

    // create an alt branch from the current tree and returns it
    StoryTree createAltBranch(Story story) {
        StoryTree currentStoryTree = this
        StoryTree altStoryTree = buildAltStoryTree([story])
        log.trace("adding ALT ${altStoryTree} to ${currentStoryTree}")
        currentStoryTree.addLeave(altStoryTree)
        altStoryTree
    }

    static StoryTree buildAltStoryTree(List<Story> stories) {
        log.trace("build ALT story tree from: " + stories)
        StoryTree tree = new StoryTree(type: StoryTreeType.ALT)
        tree.addStories(stories)
        tree
    }

    static StoryTree buildSeqStoryTree(List<Story> stories) {
        log.trace("build SEQ story tree from: " + stories)
        StoryTree tree = new StoryTree(type: StoryTreeType.SEQ)
        tree.addStories(stories)
        tree
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
        // String output = "StoryTree@${hashCode()} " + id + ":"
        String output = id + ":"

        if (story != null)
            output += story.id
        if (type != null)
            output += type.toString()
        if (leaves.size() > 0)
            output += leaves.toString()

        /* if (parent)
            output += "[child of StoryTree@${parent.hashCode()}]" */

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
        String output = "Summary: \n" + toString() + "\n"

        output += "------ hierarchical view ------\n"

        output += toCheck() + "\n"

        output += "------ flat view ------\n"

        output += toFlatView() + "\n"

        output += "------ stories ------\n"

        List<Story> stories = getStories()

        output += "\nStories: \n"
        for (int i = 0; i < stories.size(); i++) {
            output += i + " (" + stories[i].id + "): " + stories[i] + "\n"
        }

        output
    }

    String toFlatView(List<StoryTree> alreadyPrinted = []) {
        String output = ""

        // output += hashCode() + " | "
        output += id + ": "

        if (leaves.size() > 0) {
            output += type.toString() + " " + leaves.size()
        } else {
            output += "--- " + story.steps.size()
        }

        if (parent) output += ", child of " + /* parent.hashCode() + " | " + */ parent.id + "\n"
        else output += ", root\n"

        alreadyPrinted << this

        for (leave in leaves) {
            if (!alreadyPrinted.contains(leave))
                output += leave.toFlatView(alreadyPrinted)
        }

        if (parent != null) {
            if (!alreadyPrinted.contains(parent))
                output += parent.toFlatView(alreadyPrinted)
        }

        output
    }

    String toCheck(Integer level = 0) {
        String output = ""

        String pre = ""
        for (int i = 0; i < level * 4; i++) {
            pre += " "
        }

        if (leaves.size() > 0) {
            output += pre + type + " " + leaves.size()
            output += "\n"
            for (leave in leaves) {
                if (leave.parent != this)
                    throw new RuntimeException("Error in tree")

                output += leave.toCheck(level + 1)
            }
        } else {
            output += pre + "--- " + story.steps.size() + "\n"
        }
        output
    }

    StoryTree root() {
        if (parent == null) {
            return this
        } else {
            parent.root()
        }
    }

}