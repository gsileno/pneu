package org.leibnizcenter.pneu.components.petrinet

class TransitionEvent {
    Transition transition
    Token token

    String toString() {
        String output = ""
        output += transition.id
        if (token.label() != "") {
            output += ":"+token.label()
        }
        output
    }

    String label() {
        if (token.label() != "") {
            token.label()
        } else if (transition.label() != "") {
            transition.label()
        } else {
            transition.id
        }
    }
}
