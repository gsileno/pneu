package org.leibnizcenter.pneu.decomposition

import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Node
import org.leibnizcenter.pneu.components.petrinet.Place

// from Munoz-Gama2014

/* It is well known that checking conformance of large logs and models is a challenging problem. The size of log and model and the complexity of the underlying process
strongly influence the time needed to compute fitness and to create optimal alignments. Divide-and-conquer strategies are a way to address this problem. We do not just want to partition the traces in the event log (providing a trivial way to distribute
conformance checking). The potential gains are much higher if also the model is decomposed and traces are split into smaller ones. To decompose conformance checking problems, the overall system net SN
is broken down into a collection of subnets fSN1;SN2;…SNng such that the union of these subnets yields the original system net. */

class ModelSESEDecomposer {

    // A multi-terminal graph has to be transformed to a two-terminal graph
    // if there are more inputs they are all attached to a new input node
    // if there are more outputs they are all attached to a new input node

    Net convertMTGtoTTG(Net source) {

        Net target = source.minimalClone()

        if (target.inputs.size() == 0 || target.outputs.size() == 0 ) {
            throw new RuntimeException("The net should have at least one input and one output node.")
        }

        if (target.inputs[0].class != target.outputs[0].class) {
            throw new RuntimeException("Input/output terminals should be of the same type (places or transitions).")
        }

        if (target.inputs.size() > 1) {
            if (target.inputs[0].isPlaceLike()) {
                Place pIn = target.createPlace()
                for (pInput in target.inputs) {
                    target.createBridge(pIn, (Place) pInput)
                }
                target.inputs = [pIn]
            } else {
                throw new RuntimeException("Not yet implemented")
            }
        }

        if (target.outputs.size() > 1) {
            if (target.outputs[0].isPlaceLike()) {
                Place pOut = target.createPlace()
                for (pOutput in target.outputs) {
                    target.createBridge(pOut, (Place) pOutput)
                }
                target.outputs = [pOut]
            } else {
                throw new RuntimeException("Not yet implemented")
            }
        }

        target
    }

    // a fragment is a subnet with just two boundary nodes; one entry and one exit
    // The Refined Process Structure Tree (RPST) is the set of all canonical or objective fragments, i.e.
    // of all fragments which are disjoint or nested, i.e. which do not overlap

    List<Net> kDecomposition(List<Net> netRPST, Integer k) {

        // this is the root
        Node V = netRPST[0].inputs[0]
        List<Net> decomposition = []

        decomposition
    }


}
