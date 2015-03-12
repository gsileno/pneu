package org.leibnizcenter.pneu.execution

import groovy.transform.Immutable
import org.leibnizcenter.pneu.components.Token

enum Signal { RESERVE, RELEASE, TAKE, PUT,  // commands from transitions to places
              BOOT, FAILURE, SUCCESS }      // signals from places to transitions

@Immutable
class Message {
    Signal signal
    Integer n

    String toString() {
        "["+signal.toString() + ": " + n +"]"
    }
}