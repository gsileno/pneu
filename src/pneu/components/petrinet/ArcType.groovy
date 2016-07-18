package pneu.components.petrinet

enum ArcType {
    NORMAL, INHIBITOR, RESET, LINK
}

// normal arcs
// inhibitor arcs
// reset arcs
// link arcs are normal arcs whose firing is immediate (they could be collapsed)