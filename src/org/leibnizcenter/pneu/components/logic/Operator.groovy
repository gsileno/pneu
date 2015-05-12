// ----------------------------------------------------------------------------
// Copyright (C) 2014 G. Sileno
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
// To contact the authors:
// http://www.leibnizcenter.org/~sileno
//----------------------------------------------------------------------------

package org.leibnizcenter.pneu.components.logic

import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Log4j

@Log4j @EqualsAndHashCode
class Operator {

  List<Operator> inputOperators = []
  InlineOperator op
  List<Integer> params = []

  //////////////////
  // Builders
  //////////////////

  static Operator buildInterfaceOperator(InlineOperator op, List<Integer> params = []) {
      log.trace "creating interface operator for "+op // TODO
      new Operator(op: op, inputOperators: [], params: params)
  }

  static Operator buildUnaryOperator(Operator input, InlineOperator op, List<Integer> params = []) {
      new Operator(op: op, inputOperators: [input], params: params)
  }

  static Operator buildBinaryOperator(Operator leftInput, Operator rightInput, InlineOperator op, List<Integer> params = []) {
      new Operator(op: op, inputOperators: [leftInput, rightInput], params: params)
  }

    static Operator buildNaryOperator(List<Operator> inputs, InlineOperator op, List<Integer> params = []) {

        // translate the CHOICE operator in more known descriptions
        // options[0] is the min of inputs to be chosen within the list
        // options[1] is the max of inputs to be chosen within the list
        if (op == InlineOperator.CHOICE) {
            if (params[0] == inputs.size() && params[0] == params[1]) op = InlineOperator.AND;
            else if (params[0] == 1 && params[1] == inputs.size()) op = InlineOperator.OR;
            else if (params[0] == 1 && params[1] == 1) op = InlineOperator.XOR;
        }

        new Operator(op: op, inputOperators: inputs, params: params)
    }

    //////////////////
    // Views
    //////////////////

    String opToString() {
        if (op != InlineOperator.EMPTY) {
            String output = op.toString()

            if (params.size() > 0) {
                output += "["
                output += params.join(',')
                output += "]"
            }

            output
        } else ""
    }

    String toString() {
      String output = opToString()

      if (inputOperators.size() > 0) {
          output += "(" + inputOperators.join(',') + ")"
      }

      output
    }

}
