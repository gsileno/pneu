package pneu.components.graphics

import groovy.util.logging.Log

@Log
class Point {

    // to consider oblique direction
    // 1/tgRange < tg < tgRange
    static final tgRange = 4

    Integer x
    Integer y

    static Integer squaredDistance(Point point1, Point point2) {
        return (point1.x - point2.x)*(point1.x - point2.x) + (point1.y - point2.y)*(point1.y - point2.y)
    }

    static Integer tg(Point point1, Point point2) {
        if (point1.x == point2.x) return null  // should be infinite
        return (point1.y - point2.y) / (point1.x - point2.x)
    }

    void traslate(Integer plusX, Integer plusY) {
        x += plusX
        y += plusY
    }

    static ArrayList<Cardinality> getDirections(Point point1, Point point2) {
        Integer tg = tg(point1, point2)
        ArrayList<Cardinality> directions = []

        log.info("point1: "+point1.x+"/"+point1.y+", point2: "+ point2.x+"/"+point2.y)

        if (tg == null) {
            log.info("tg: infinite")
            if (point2.y > point1.y) {
                directions << Cardinality.NORTH
            } else if (point2.y < point1.y) {
                directions << Cardinality.SOUTH
            }
        } else {
            log.info("tg:"+ tg)

            if (tg > 1/tgRange && tg < tgRange) {
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

        log.info("directions: "+directions)

        directions
    }

}
