package org.leibnizcenter.pneu.graphics.export

import org.leibnizcenter.pneu.components.Net

/* See on http://www.texample.net/tikz/examples/nodetutorial/ for the preamble */

class PN2LaTeX {

    static convert(Net net) {
        String code = ""

        code += header+"\n"

        code += "  \\begin{scope}\n"

        net.placeList.each() { pl ->
            code += "    \\node\t"
            code += "[place]\t"
            code += "("+pl.id+")\t"
            code += "{};\n"
        }

        code += "\n"

        net.transitionList.each() { tr ->
            code += "    \\node\t"
            code += "[transition]\t"
            code += "("+tr.id+")  "
            code += "{}\n"

            def edgesIn = net.arcList.findAll { arc -> arc.source.id == tr.id }
            edgesIn.each() { edge ->
                code += "      edge\t"
                code += "[pre]\t"
                code += "("+edge.target.id+")"
                code += "\n"
            }

            def edgesOut = net.arcList.findAll { arc -> arc.target.id == tr.id }
            edgesOut.each() { edge ->
                code += "      edge\t"
                code += "[post]\t"
                code += "("+edge.source.id+")"
                code += "\n"
            }

            code = code[0..-2]
            code += ";\n\n"
        }

        code += "  \\end{scope}\n"

        code += footer+"\n"
    }

    // preambles

    static String header = '''\\begin{tikzpicture}[node distance=1.3cm,>=stealth',bend angle=45,auto]
  \\tikzstyle{place}=[circle,thick,draw=blue!75,fill=blue!20,minimum size=6mm]
  \\tikzstyle{red place}=[place,draw=red!75,fill=red!20]
  \\tikzstyle{transition}=[rectangle,thick,draw=black!75,
  \t\t\t  fill=black!20,minimum size=4mm]

  \\tikzstyle{every label}=[red]'''

    static String footer = '''
\\end{tikzpicture}'''

}


