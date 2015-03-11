package org.leibnizcenter.pneu.execution

import groovy.transform.Immutable
import org.leibnizcenter.pneu.components.Token

enum Signal { RESERVE, RELEASE, TAKE, PUT, FAILURE, SUCCESS }

@Immutable
class Message {
    Signal signal
    Integer n
}