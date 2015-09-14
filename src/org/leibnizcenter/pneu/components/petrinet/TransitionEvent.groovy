package org.leibnizcenter.pneu.components.petrinet

class TransitionEvent {
    Transition transition
    Token token

    String toString() {

        if (transition.id == "t2")
            print("CIAO")

        String output = ""
        output += transition.id
        if (token.label() != "") {
            output += ":"+token.label()
        }
        output
    }

    String label() {

        if (transition.id == "t2")
            print("CIAO")

        if (token !=null && token.label() != "") {
            token.label()
        } else if (transition.label() != "") {
            transition.label()
        } else {
            transition.id
        }
    }
}
