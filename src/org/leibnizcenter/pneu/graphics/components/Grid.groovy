package org.leibnizcenter.pneu.graphics.components

import org.leibnizcenter.pneu.components.Net

class Grid {

    Net net                           // reference net
    Float zoomRatio                   // tikz posizion = intermediate position * zoomRatio
    Integer inputDotGranularity       // pnml position / inputDotGranularity = intermediate position
                                      // Yasper use 33 dots per square

    Closure transformation            // possible change of coordinates, using the given closures

    def setZero = { Point point ->
        point.x = point.x - net.minX
        point.y = point.y - net.minY
        point
    }

    def flipHorizontal = { Point point ->
        point.x = net.maxX - point.x
        setZero(point)
    }

    def flipVertical = { Point point ->
        point.y = net.maxY - point.y
        setZero(point)
    }

    def rotate90ClockWise = { Point point ->
        point = setZero(point)
        // Integer tmp = point.y
        // point.y = -point.x
        // point.x = tmp
        point
    }

    def rotate90AntiClockWise = { Point point ->
        point = setZero(point)
        Integer tmp = point.y
        point.y = point.x
        point.x = -tmp
        point
    }

    Point transformCoordinates(Point point) {

        if (transformation == null) setZero(point)
        else return transformation(point)
    }

    String printScaled(Point point) {

        point = transformCoordinates(point)

        return Math.round(Math.round(point.x/inputDotGranularity)*zoomRatio * 100)/100 +", "+
               Math.round(Math.round(point.y/inputDotGranularity)*zoomRatio * 100)/100
    }
}
