package org.leibnizcenter.pneu.animation.actorbased

import groovy.transform.Immutable

enum Signal { BOOT,                         // command from orchestrator to places
              EMIT, STATUS,                 // command from orchestrator to transitions
              RESERVE, RELEASE, TAKE, PUT,  // commands from transitions to places
              SYNC, FAILURE, SUCCESS }      // signals from places to transitions

@Immutable
class Message {
    Signal signal
    Integer n

    String toString() {
        "["+signal.toString() + ": " + n +"]"
    }
}