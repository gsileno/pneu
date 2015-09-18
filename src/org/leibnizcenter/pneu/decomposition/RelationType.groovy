package org.leibnizcenter.pneu.decomposition


class RelationType {

    RelationFlag type

    enum RelationFlag {
        NONE,
        SUBSUMES,
        IS_SUBSUMED,
        EQUIVALENT,
        PARTIALLY_SUBSUMES,
        IS_PARTIALLY_SUBSUMED,
        IS_PARTIALLY_EQUIVALENT,
    }

    Integer leftLimit
    Integer rightLimit

}

