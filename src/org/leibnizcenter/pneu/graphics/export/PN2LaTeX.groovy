package org.leibnizcenter.pneu.graphics.export

import org.leibnizcenter.pneu.components.Net
import org.leibnizcenter.pneu.components.Place
import org.leibnizcenter.pneu.graphics.components.Cardinality
import org.leibnizcenter.pneu.graphics.components.Compass
import org.leibnizcenter.pneu.graphics.components.Point

/* See on http://www.texample.net/tikz/examples/nodetutorial/ for the preamble */
/* modified with this http://tex.stackexchange.com/questions/119764/overlapping-nodes-in-tikz */

class PN2LaTeX {

    static final int maxCharPerLine = 16

    static String helperOptions(String label, List<Cardinality> directions, String id) {
        String code = ""

        if (label.trim() == "") label = null

        if (directions.size() > 0 || label != null) {
            code += "["

            for (direction in directions) {
                if (direction == Cardinality.EAST) code += "left "
                else if (direction == Cardinality.WEST) code += "right "
                else if (direction == Cardinality.NORTH) code += "above "
                else if (direction == Cardinality.SOUTH) code += "below "
            }
            if (directions.size() > 0) {
                code += "= of "+id
                code +=","
            }


            if (label != null) {
                List<String> words = label.split(" ");
                List<String> lines = []

                String line = ""
                for (word in words) {
                    if (line.length() + word.trim().length() > maxCharPerLine) {
                        lines << line
                        line = ""
                    }
                    if (line.length() > 0) line += " "
                    line += word.trim()
                }
                lines << line

                code += "label={below:"+lines.join("\\\\")+"}"
            } else {
                code = code[0..-2]
            }

            code += "]\t"
        }

        code
    }

    static convert(Net net) {

        Compass compass = new Compass()

        String code = ""

        code += header+"\n"

        code += "  \\begin{scope}\n"

        for (int i = 0; i < net.placeList.size(); i++) {

            Place pl = net.placeList.get(i)

            code += "    \\node\t"
            code += "[place]\t"
            code += "("+pl.id+")\t"

            String proxiestId
            List<Cardinality> directions = []
            Integer dmin

            // find the nearest from the known places
            for (int j = 0; j < i; j++) {
                Place proxy = net.placeList.get(j)

                if (proxy != pl) {
                    int d = Point.squaredDistance(pl.position, proxy.position)
                    if (dmin == null || d < dmin) {
                        dmin = d
                        proxiestId = proxy.id
                        directions = compass.transformDirections(Point.getDirections(pl.position, proxy.position))
                    }
                }
            }

            code += helperOptions(pl.name, directions, proxiestId)

            code += "{};\n"

        }

        code += "\n"

        net.transitionList.each() { tr ->
            code += "    \\node\t"
            code += "[transition]\t"
            code += "("+tr.id+")\t"

            String proxiestId
            List<Cardinality> directions = []
            Integer dmin

            // find the nearest of all places
            for (pl in net.placeList) {
                int d = Point.squaredDistance(tr.position, pl.position)

                if (dmin == null || d < dmin) {
                    dmin = d
                    proxiestId = pl.id
                    directions = compass.transformDirections(Point.getDirections(tr.position, pl.position))
                }
            }

            code += helperOptions(tr.name, directions, proxiestId)

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
  \\tikzstyle{transition}=[rectangle,thick,draw=black!75,fill=black!20,minimum size=4mm]

  \\tikzstyle{every label}=[font=\\footnotesize,align=center,black]'''

    static String footer = '''
\\end{tikzpicture}'''

}


