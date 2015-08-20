package org.leibnizcenter.pneu.decomposition

import org.leibnizcenter.pneu.components.petrinet.Net

// from Munoz-Gama2014

/* It is well known that checking conformance of large logs and models is a challenging problem.
The size of log and model and the complexity of the underlying process
strongly influence the time needed to compute fitness and to create optimal alignments.
Divide-and-conquer strategies are a way to address this problem.
We do not just want to partition the traces in the event log (providing a trivial way to distribute
conformance checking). The potential gains are much higher if also the model is
decomposed and traces are split into smaller ones.
To decompose conformance checking problems, the overall system net SN
is broken down into a collection of subnets fSN1;SN2;…SNng such that the union of these subnets yields the original system net.

 */

class SESEDecomposer {

    // A multi-terminal graph has to be transformed to a two-terminal graph
    // if there are more inputs they are all attached to a new input node
    // if there are more outputs they are all attached to a new input node

    Net convertMTGtoTTG(Net source) {

        Net target = source.

        if (target.inputs.size() == 0 || target.outputs.size() == 0 ) {
            throw new RuntimeException("The net should have at least one input and one output node.")
        }

        if (target.inputs[0].class != target.outputs[0].class) {
            throw new RuntimeException("Input/output terminals should be of the same type (places or transitions).")
        }

        if (target.inputs.size() > 1) {

        } else {

        }

    }

    List<Net> kDecomposition(List<Net> netRPST, Integer k) {

        // this is the root
        Node V = netRPST[0].inputs[0]
        List<Net> decomposition


    }


}
