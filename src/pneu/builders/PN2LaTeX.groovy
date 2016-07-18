package pneu.builders

import groovy.util.logging.Log
import pneu.components.petrinet.Arc
import pneu.components.petrinet.ArcType
import pneu.components.petrinet.Net
import pneu.components.petrinet.Place
import pneu.components.petrinet.Transition
import pneu.components.graphics.Cardinality
import pneu.components.graphics.Compass
import pneu.components.graphics.Grid
import pneu.components.graphics.Point

/* See on http://www.texample.net/tikz/examples/nodetutorial/ for the preamble */
/* modified with this http://tex.stackexchange.com/questions/119764/overlapping-nodes-in-tikz */

@Log
class PN2LaTeX {

    static final int maxCharPerLine = 13 // lpetri net label multi-line option

    static String helperLabel(String label, boolean enclosed = true) {
        String code = ""

        if (label.trim() == "") label = null

        if (label != null) {
            if (enclosed) code += "["
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

            if (enclosed) code += "]\t"
        }

        code
    }

    static String helperOptions(String label, List<Cardinality> directions, String id, boolean enclosed = true) {
        String code = ""

        if (label.trim() == "") label = null

        if (directions.size() > 0 || label != null) {
            if (enclosed) code += "["

            for (direction in directions) {
                if (direction == Cardinality.EAST) code += "left "
                else if (direction == Cardinality.WEST) code += "right "
                else if (direction == Cardinality.NORTH) code += "above "
                else if (direction == Cardinality.SOUTH) code += "below "
            }
            if (directions.size() > 0) {
                code += "= of "+id
                if (label != null) code +=","
            }

            if (label != null) code += helperLabel(label, false)

            if (enclosed) code += "]\t"
        }

        code
    }

    // First strategy:
    // first, do all the places
    // then put the position of transition in function of the places
    static convertRelative1(Net net) {

        Compass compass = new Compass()

        String code = ""

        code += header(5)+"\n"

        code += "  \\begin{scope}\n"

        for (int i = 0; i < net.placeList.size(); i++) {

            Place pl = net.placeList.get(i)

            code += "    \\node\t"
            code += "[place"
            int nMarking = pl.marking.size()
            if (nMarking > 0) code += ",tokens="+nMarking
            code += "]\t"
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

            code += helperOptions(pl.label(), directions, proxiestId)

            code += "{};\n"

        }

        code += "\n"

        List<Transition> orderedTransitionList = net.transitionList.collect().sort() {it.id}

        for (tr in orderedTransitionList) {
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

            code += helperOptions(tr.label(), directions, proxiestId)

            code += "{}\n"

            def edgesIn = net.arcList.findAll { arc -> arc.target.id == tr.id }
            edgesIn.each() { edge ->
                code += "      edge\t"
                code += "[pre]\t"
                code += "("+edge.source.id+")"
                code += "\n"
            }

            def edgesOut = net.arcList.findAll { arc -> arc.source.id == tr.id }
            edgesOut.each() { edge ->
                code += "      edge\t"
                code += "[post]\t"
                code += "("+edge.target.id+")"
                code += "\n"
            }

            code = code[0..-2]
            code += ";\n\n"
        }

        code += "  \\end{scope}\n"

        code += footer+"\n"
    }

    // Second strategy:
    // For each places
    //   write the place (give the directions in function of the know nodes)
    //   for each transition:
    //     write the transition if we know the places to which it is attached
    //     (give the directions in function of the know nodes)
   //
    static convertRelative2(Net net) {

        Compass compass = new Compass()
        List<Node> walkedNodes = []

        String code = ""

        code += header(5)+"\n"

        code += "  \\begin{scope}\n"

        for (pl in net.placeList) {

            log.info("seed place: "+pl.id)

            // if this place has not been written already
            if (!walkedNodes.contains(pl)) {

                log.info("..."+pl.id+" not drawn yet")

                code += "    \\node\t"
                code += "[place]\t"
                code += "(" + pl.id + ")\t"

                String plProxiestId
                List<Cardinality> plDirections = []
                Integer plDmin

                // find the nearest from the known places
                for (node in walkedNodes) {
                    int d = Point.squaredDistance(pl.position, node.position)
                    if (plDmin == null || d < plDmin) {
                        plDmin = d
                        plProxiestId = node.id
                        plDirections = compass.transformDirections(Point.getDirections(pl.position, node.position))
                    }
                }

                log.info("proxiest of known nodes: "+plProxiestId)
                log.info("direction: "+plDirections)
                log.info("distance: "+plDmin)

                code += helperOptions(pl.label(), plDirections, plProxiestId)

                code += "{};\n\n"

                walkedNodes << pl

                net.arcList.each() { arc ->
                    String id

                    if (arc.source.id == pl.id) {
                        id = arc.target.id
                    } else if (arc.target.id == pl.id) {
                        id = arc.source.id
                    }

                    if (id != null) {

                        Transition tr = net.transitionList.find() { it.id == id }

                        log.info(pl.id +" is connected to "+tr.id)

                        if (!walkedNodes.contains(tr)) {

                            log.info("..."+tr.id+" not drawn yet")

                            // all input and output arcs to the transition
                            def edgesIn = net.arcList.findAll { it.target.id == tr.id }
                            def edgesOut = net.arcList.findAll { it.source.id == tr.id }

                            // check if all places have already been drawn
                            // i.e. look for a place which has not been drawn
                            Boolean found = false
                            for (int i = 0; i < edgesIn.size() && !found; i++) {
                                found = (!walkedNodes.contains(edgesIn.get(i).source))
                            }
                            if (!found) for (int i = 0; i < edgesOut.size() && !found; i++) {
                                found = (!walkedNodes.contains(edgesOut.get(i).target))
                            }

                            if (!found) {
                                code += "    \\node\t"
                                code += "[transition]\t"
                                code += "(" + tr.id + ")\t"

                                String trProxiestId
                                List<Cardinality> trDirections = []
                                Integer trDmin

                                // find the nearest from the known places
                                for (node in walkedNodes) {
                                    int d = Point.squaredDistance(tr.position, node.position)
                                    if (trDmin == null || d < trDmin) {
                                        trDmin = d
                                        trProxiestId = node.id
                                        trDirections = compass.transformDirections(Point.getDirections(tr.position, node.position))
                                    }
                                }

                                log.info("proxiest of known nodes: "+trProxiestId)
                                log.info("direction: "+trDirections)
                                log.info("distance: "+trDmin)

                                code += helperOptions(tr.label(), trDirections, trProxiestId)

                                code += "{}\n"

                                edgesIn.each() { edge ->
                                    code += "      edge\t"
                                    code += "[pre]\t"
                                    code += "("+edge.source.id+")"
                                    code += "\n"
                                }

                                edgesOut.each() { edge ->
                                    code += "      edge\t"
                                    code += "[post]\t"
                                    code += "("+edge.target.id+")"
                                    code += "\n"
                                }


                                code = code[0..-2]
                                code += ";\n\n"

                                walkedNodes << tr
                            }
                        }
                    }
                }

            }
        }

        code += "  \\end{scope}\n"

        code += footer+"\n"
    }

    static convertAbsolute(Net net,
                           Float zoomXRatio = 0.5, Float zoomYRatio = 0.5, // grid zoom
                           Float minPlaceSize = 3, // min size for nodes (in mm)
                           Float minTransitionSize = 4, // min size for nodes (in mm)
                           Float inputDotGranularity = 33) {

        Grid grid = new Grid(zoomXRatio: zoomXRatio, zoomYRatio: zoomYRatio, inputDotGranularity: inputDotGranularity)
        grid.setTransformation(grid.flipVertical)
        net.grid = grid

        //TODO: refactor setupGrid: it is ambiguous now
        net.setupGrid()

        String code = ""

        code += header(minPlaceSize, minTransitionSize)+"\n"

        code += "  \\begin{scope}\n"

        for (int i = 0; i < net.placeList.size(); i++) {

            Place pl = net.placeList.get(i)

            if (pl.position == null) {
                throw new RuntimeException("The place ${pl} does not have an absolute position.")
            }

            code += "    \\node\t"
            code += "[place"
            int nMarking = pl.marking.size();
            if (nMarking > 0) code += ",tokens="+nMarking
            code += "]\t"
            code += "("+pl.id+")\t"

            code += helperLabel(pl.label())

            code += " at ("+grid.printScaled(pl.position)+")\t"
            code += "{};\n"

        }

        code += "\n"

        List<Arc> biflowArcs = []
        for (Arc a in net.arcList) {
            for (Arc b in net.arcList - a) {
                if (a.source == b.target && b.source == a.target && a.weight == b.weight) {
                    if (!biflowArcs.contains(a) && !biflowArcs.contains(b))
                      biflowArcs += [a, b]
                }
            }
        }

        List<Arc> nonBiflowArcs = net.arcList - biflowArcs

        List<Transition> orderedTransitionList = net.transitionList.collect().sort() {it.id}

        for (tr in orderedTransitionList) {

            if (tr.position == null) {
                throw new RuntimeException("The transition ${tr} does not have an absolute position.")
            }

            code += "    \\node\t"
            code += "[transition]\t"
            code += "("+tr.id+")\t"
            code += " at ("+grid.printScaled(tr.position)+")\t"
            code += helperLabel(tr.label())
            code += "{}\n"

            String postcode = ""

            // just take one of the two biflow arcs
            for (int i = 0; i < biflowArcs.size(); i += 2) {
                Arc edge = biflowArcs[i]
                if (edge.source.id == tr.id || edge.target.id == tr.id) {
                    if (edge.pointList.size() > 0) {
                        // for the incoming edges, the order of points should be inversed
                        postcode += "    \\draw\t[<->]\t(" + tr.id + ")"
                        edge.pointList.each() { point ->
                            postcode += "\t"
                            postcode += " -- (" + grid.printScaled(point) + ")"
                        }
                        postcode += " -- (" + edge.source.id + ");\n"
                    } else {
                        code += "      edge\t"
                        code += "[<->]\t"
                        code += "(" + edge.source.id + ")"
                        code += "\n"
                    }
                }
            }

            // normal and inhibitor arcs // TODO RESET
            def edgesIn = nonBiflowArcs.findAll { arc -> arc.target.id == tr.id }
            edgesIn.each() { edge ->
                String style
                if (edge.type == ArcType.INHIBITOR)
                    style = "o-"
                else
                    style = "pre"

                if (edge.pointList.size() > 0) {
                    // for the incoming edges, the order of points should be inversed
                    postcode += "    \\draw\t[$style]\t("+tr.id+")"
                    edge.pointList.reverse().each() { point ->
                        postcode += "\t"
                        postcode += " -- ("+grid.printScaled(point)+")"
                    }
                    postcode += " -- ("+edge.source.id+");\n"
                } else {
                    code += "      edge\t"
                    code += "[$style]\t"
                    code += "("+edge.source.id+")"
                    code += "\n"
                }
            }

            def edgesOut = nonBiflowArcs.findAll { arc -> arc.source.id == tr.id }
            edgesOut.each() { edge ->
                if (edge.pointList.size() > 0) {
                    postcode += "    \\draw\t[post]\t(" + tr.id +")"
                    edge.pointList.each() { point ->
                        postcode += "\t"
                        postcode += " -- (" + grid.printScaled(point) + ")"
                    }
                    postcode += " -- (" + edge.target.id + ");\n"
                } else {
                    code += "      edge\t"
                    code += "[post]\t"
                    code += "(" + edge.target.id + ")"
                    code += "\n"
                }
            }

            code = code[0..-2]
            code += ";\n\n"

            code += postcode+"\n"
        }

        code += "  \\end{scope}\n"

        code += footer+"\n"
    }

    // preambles

    static String header(Float minPlaceSize, Float minTransitionSize) { // add thick to have stronger design
        return """\\begin{tikzpicture}[node distance=1.3cm,>=stealth',shorten >=1pt,bend angle=45,auto,scale=1, every node/.style={transform shape}]
  \\tikzstyle{place}=[circle,drop shadow={opacity=.25, shadow xshift=0.07, shadow yshift=-0.07},draw=black!100,fill=white!20,minimum size=${minPlaceSize}mm]
  \\tikzstyle{transition}=[rectangle,drop shadow={opacity=.25, shadow xshift=0.07, shadow yshift=-0.07},draw=black!100,fill=white!20,minimum size=${minTransitionSize}mm]

  \\tikzstyle{every label}=[font=\\footnotesize,align=center,black]
"""
//        return """\\begin{tikzpicture}[node distance=1.3cm,>=stealth',shorten >=1pt,bend angle=45,auto]
//  \\tikzstyle{place}=[circle,drop shadow={opacity=.25, shadow xshift=0.07, shadow yshift=-0.07},draw=black!100,fill=white!20,minimum size=3.0mm]
//  \\tikzstyle{transition}=[rectangle,drop shadow={opacity=.25, shadow xshift=0.07, shadow yshift=-0.07},draw=black!100,fill=white!20,minimum size=4.0mm]
//  \\tikzstyle{every label}=[font=\\scriptsize,align=center,black]"""

//        return """\\begin{tikzpicture}[node distance=1.3cm,>=stealth',shorten >=1pt,bend angle=45,auto]
//  \\tikzstyle{place}=[circle,drop shadow={opacity=.25, shadow xshift=0.07, shadow yshift=-0.07},draw=black!100,fill=white!20,minimum size=${minPlaceSize}mm]
//  \\tikzstyle{transition}=[rectangle,drop shadow={opacity=.25, shadow xshift=0.07, shadow yshift=-0.07},draw=black!100,fill=white!20,minimum size=${minTransitionSize}mm]
//  \\tikzstyle{every label}=[font=\\scriptsize,align=center,black]"""
    }

    static String footer = """
\\end{tikzpicture}"""

}


