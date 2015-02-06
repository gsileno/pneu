package org.leibnizcenter.pneu.graphics.components

class Point {

    // to consider oblique direction
    // 1/tgRange < tg < tgRange
    static final tgRange = 4

    Integer x
    Integer y

    static Integer squaredDistance(Point point1, Point point2) {
        return (point1.x - point2.x)^2 + (point1.y - point2.y)^2
    }

    static Integer tg(Point point1, Point point2) {
        if (point1.x == point2.x) return null  // should be infinite
        return (point1.y - point2.y) / (point1.x - point2.x)
    }

    static ArrayList<Cardinality> getDirections(Point point1, Point point2) {
        Integer tg = tg(point1, point2)
        ArrayList<Cardinality> directions = []

        if (tg == null) {
            if (point2.y > point1.y) {
                directions << Cardinality.NORTH
            } else if (point2.y < point1.y) {
                directions << Cardinality.SOUTH
            }
        } else {
            if (tg > 1/tgRange || tg < tgRange) {
                if (point2.x > point1.x) {
                    directions << Cardinality.NORTH   // tikz requires first the vertical
                    directions << Cardinality.EAST    // then the horizontal (e.g. above right)
                } else if (point2.x < point1.x) {
                    directions << Cardinality.SOUTH
                    directions << Cardinality.WEST
                }
            } else {
                if (point2.x > point1.x) {
                    directions << Cardinality.EAST
                } else if (point2.x < point1.x) {
                    directions << Cardinality.WEST
                }
            }
        }
    }

}
