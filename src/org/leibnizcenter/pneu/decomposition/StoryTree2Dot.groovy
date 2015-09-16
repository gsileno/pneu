package org.leibnizcenter.pneu.decomposition

class StoryTree2Dot {

    // helper for better visualization
    static private String tab(Integer level = 1, String c = " ") {
        String output = ""
        for (int i = 0; i < level * 2; i++)
            output += c
        output
    }

    static String innerConversion(StoryTree tree, Integer level = 1) {
        String code = ""

        if (tree.story != null) {
            code += "\n" + tab(level) + "subgraph story${tree.id} {\n" + tab(level + 1) + "node [shape=circle,fixedsize=true,width=.5];\n"
            String label = tree.story.id
            code += tab(level + 1) + tree.id + " [label=\"" + label + "\"] ;\n"
            code += tab(level) + "} \n"
        } else {
            if (tree.leaves.size() > 0) {

                code += "\n" + tab(level) + "subgraph cluster${tree.id} {\n"  // cluster is a prefix in graphviz
                if (tree.type) code += tab(level + 1) + "label=\"" + tree.type + "\" ;\n"
                if (tree.type == StoryTreeType.ALT)
                    code += tab(level + 1) + "color=lightblue ;\n"
                else if (tree.type == StoryTreeType.SEQ)
                    code += tab(level + 1) + "color=red ;\n"
                else
                    code += tab(level + 1) + "color=lightgray ;\n"

                code += "\n"

                code += tab(level + 1) + "subgraph group${tree.id} {\n" + tab(level + 2) + "node [shape=rect,height=.5,width=.5];\n"
                String label = tree.id
                code += tab(level + 2) + tree.id + " [label=\"" + label + "\"] ;\n"
                code += tab(level + 1) + "} \n"

                for (leave in tree.leaves) {
                    code += innerConversion(leave, level + 1)
                }

                code += "\n"

                for (leave in tree.leaves) {
                    code += tab(level) + tree.id
                    code += " -> "
                    code += leave.id
                    code += " ;\n"
                }

                code += "\n"

                code += tab(level) + "}\n"

            }
        }

        code

    }

    static String convert(StoryTree tree) {
        String code = ""

        code += "digraph G {\n  rankdir=\"LR\";\n  concentrate=true;\n"
        code += innerConversion(tree)
        code += "}\n"

        code
    }

}
