package pneu.components.graphics

enum Cardinality { NORTH, EAST, SOUTH, WEST }

class Compass {

    Cardinality north = Cardinality.NORTH
    Cardinality east = Cardinality.EAST
    Cardinality south = Cardinality.SOUTH
    Cardinality west = Cardinality.WEST

    void flipHorizontal() {
        Cardinality tmp = east
        east = west
        west = tmp
    }

    void flipVertical() {
        Cardinality tmp = north
        north = south
        south = tmp
    }

    void rotate90ClockWise() {
        Cardinality tmp = north
        north = west
        west = south
        south = east
        east = tmp
    }

    void rotate90AntiClockWise() {
        Cardinality tmp = north
        north = east
        east = south
        south = west
        west = tmp
    }

    Cardinality transformDirection(Cardinality direction) {
        switch (direction) {
            case Cardinality.NORTH : return north
            case Cardinality.EAST : return east
            case Cardinality.SOUTH : return south
            case Cardinality.WEST : return west
        }
    }

    ArrayList<Cardinality> transformDirections(ArrayList<Cardinality> oldDirections) {
        ArrayList<Cardinality> newDirections = []
        for (direction in oldDirections) {
            newDirections << transformDirection(direction)
        }
        newDirections
    }
}
