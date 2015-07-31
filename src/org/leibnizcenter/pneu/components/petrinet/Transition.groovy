package org.leibnizcenter.pneu.components.petrinet

import groovy.transform.AutoClone

class Transition extends Node {

    // transition by default of normal type
    TransitionType type = TransitionType.NORMAL

    boolean isEmitter() {
        return (type == TransitionType.EMITTER)
    }

    boolean isCollector() {
        return (type == TransitionType.COLLECTOR)
    }

    // Operational Semantics
    boolean isEnabled(boolean analysis = false) {

        if (inputs.size() == 0) {
            if (!analysis && isEmitter())
                return true
            else
                return false
        }

        for (elem in inputs) {
            // inhibitor
            if (elem.type == ArcType.INHIBITOR) {
                if (elem.source.marking.size() >= elem.weight) {
                    return false
                }
            } else if (elem.type == ArcType.NORMAL) {
              if (elem.source.marking.size() < elem.weight) {
                 return false
              }
            }
        }
        return true
    }

    void fire() {
        consumeInputTokens()
        produceOutputTokens()
    }

    void consumeInputTokens() {
        for (elem in inputs) {
            for (int i=0; i<elem.weight; i++) {
                elem.source.marking.pop()
            }
        }
    }

    void produceOutputTokens() {
        for (elem in outputs) {
            if (elem.type == ArcType.NORMAL) {
                for (int i = 0; i < elem.weight; i++) {
                    elem.target.marking.push(new Token())
                }
            } else if (elem.type == ArcType.INHIBITOR) {
                elem.target.flush()
            }
        }
    }

    String toString() {
        if (name != "") return name
        else return id
    }

}
