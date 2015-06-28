package org.leibnizcenter.pneu.components.graphics

import org.leibnizcenter.pneu.components.petrinet.Net

class Grid {

    // grid dimensions
    Integer minX, maxX
    Integer minY, maxY

    // Yasper use 33 dots per square
    Integer inputDotGranularity       // pnml position / inputDotGranularity = intermediate position

    // -------------------

    Integer outputDotGranularity      // pnml position = intermediate position * outputDotGranularity
    Float zoomXRatio                  // tikz posizion = intermediate position * zoomRatio
    Float zoomYRatio

    Closure transformation            // possible change of coordinates, using the given closures

    // check the given coordinated with the currentState min, max
    // reset min/max if necessary
    void testMinMax(Integer x, Integer y) {
        if (minX == null) minX = x
        else if (x < minX) minX = x
        if (maxX == null) maxX = x
        else if (x > maxX) maxX = x
        if (minY == null) minY = y
        else if (y < minY) minY = y
        if (maxY == null) maxY = y
        else if (y > maxY) maxY = y
    }

    Closure setZero = { Point point ->
        point.x = point.x - minX
        point.y = point.y - minY
        point
    }

    Closure flipHorizontal = { Point point ->
        point.x = maxX - point.x
        setZero(point)
    }

    Closure flipVertical = { Point point ->
        point.y = maxY - point.y
        setZero(point)
    }

    Closure rotate90ClockWise = { Point point ->
        point = setZero(point)
        // Integer tmp = point.y
        // point.y = -point.x
        // point.x = tmp
        point
    }

    Closure rotate90AntiClockWise = { Point point ->
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

    Point scalePoint(Point point) {
        point = transformCoordinates(point)
        point.x = Math.round(Math.round(point.x/inputDotGranularity)*100)/100
        point.y = Math.round(Math.round(point.y/inputDotGranularity)*100)/100
        point
    }

    String printScaled(Point point) {
        point = scalePoint(point)
        return point.x +", "+ point.y
    }

    Point inverseScalePoint(Point point) {
        point = transformCoordinates(point)
        point.x = Math.round(point.x*outputDotGranularity) + outputDotGranularity
        point.y = Math.round(point.y*outputDotGranularity) + outputDotGranularity
        point
    }

    void setOutputDotGranularity(Integer outputDotGranularity) {
        this.outputDotGranularity = outputDotGranularity
        zoomXRatio = outputDotGranularity
        zoomYRatio = outputDotGranularity
    }

}
