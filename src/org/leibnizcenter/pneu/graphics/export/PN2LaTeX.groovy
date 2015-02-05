package org.leibnizcenter.pneu.graphics.export

import org.leibnizcenter.pneu.components.Net
import org.leibnizcenter.pneu.components.Place

/* See on http://www.texample.net/tikz/examples/nodetutorial/ for the preamble */


class PN2LaTeX {

    static enum Cardinality { NORTH, EAST, SOUTH, WEST }

    static convert(Net net) {
        String code = ""

        code += header+"\n"

        code += "  \\begin{scope}\n"

        for (int i = 0; i < net.placeList.size(); i++) {
            Place pl = net.placeList.get(i)

            code += "    \\node\t"
            code += "[place]\t"
            code += "("+pl.id+")\t"

            println "new place @@@@@@@@"

            // find the nearest from the remaining nodes
            Place proxiestx, proxiesty
            Integer minx, miny
            Cardinality dirx, diry
            Integer dx, dy

            if (i > 0) {

                for (int j = i - 1; j >= 0; j--) {
                    dx = dy = null

                    Place proxy = net.placeList.get(j)

                    if (proxy.id != pl.id) {
                        dx = proxy.position.x - pl.position.x
                        dy = proxy.position.y - pl.position.y

                        println "minX: "+minx +" - dX: "+dx
                        println "minY: "+miny +" - dY: "+dy

                        if (minx == null) minx = dx.abs()
                        else if (minx != 0) {
                            if (dx.abs() < minx) {
                                minx = dx.abs()
                                println "X: "+minx +": "+dx
                                if (dx > 0) {
                                    dirx = Cardinality.EAST
                                    proxiestx = proxy
                                } else if (dx < 0) {
                                    dirx = Cardinality.WEST
                                    proxiestx = proxy
                                } else {
                                    dirx = null
                                    proxiestx = null
                                }
                            }
                        }

                        if (miny == null) miny = dy.abs()
                        else if (miny != 0) {
                            if (dy.abs() < miny) {
                                miny = dy.abs()
                                println "Y: "+miny +": "+dy
                                if (dy > 0) {
                                    diry = Cardinality.NORTH
                                    proxiesty = proxy
                                } else if (dy < 0) {
                                    diry = Cardinality.SOUTH
                                    proxiesty = proxy
                                } else {
                                    diry = null
                                    proxiesty = null
                                }
                            }
                        }
                    }
                }

            }

            if (pl.name.trim() == "") pl.name = null

            if (dirx != null || diry != null || pl.name != null) {
                code += "["

                if (dirx != null) {
                    if (dirx == Cardinality.EAST) code += "left of="
                    if (dirx == Cardinality.WEST) code += "right of="
                    code += proxiestx.id
                    if (diry != null || pl.name != null) { code +="," }
                }

                if (diry != null) {
                    if (diry == Cardinality.NORTH) code += "above of="
                    if (diry == Cardinality.SOUTH) code += "below of="
                    code += proxiesty.id
                    if (pl.name != null) { code +="," }
                }

                if (pl.name != null) {
                    code += "label="+pl.name.trim()
                }

                code += "]\t"
            }

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


