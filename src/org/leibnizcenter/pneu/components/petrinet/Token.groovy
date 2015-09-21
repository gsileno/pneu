package org.leibnizcenter.pneu.components.petrinet

abstract class Token {

    abstract Boolean compare(Token t)

    abstract Token minimalClone()

    abstract String label()

    abstract Boolean subsumes(Token t, Map<String, Map<String, String>> mapIdentifiers)
    abstract Boolean subsumes(Token t, Map<String, Map<String, String>> mapIdentifiers, Boolean negateHead)

}
