package org.leibnizcenter.pneu.builders

import org.leibnizcenter.pneu.components.petrinet.Arc
import org.leibnizcenter.pneu.components.petrinet.ArcType
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

abstract class PN2abstract {

    // helper for better visualization
    static private String tab(Integer level = 1, String c = " ") {
        String output = ""
        for (int i = 0; i < level * 2; i++)
            output += c
        output
    }

    // reset all ids with the ordinal number
    static void resetIds(Net net) {
        setIds(net, true)
    }

    // set all the empty ids with the ordinal number
    static void setEmptyIds(Net net) {
        setIds(net, false)
    }

    // set ids
    static void setIds(Net net, Boolean force, String prefix = "", List<Net> alreadyConvertedNets = []) {

        List<Place> placeList = net.placeList
        List<Transition> transitionList = net.transitionList
        List<Arc> arcList = net.arcList
        List<Net> subNets = net.subNets

        if (placeList.size() > 0) {
            Integer i = 0
            for (pl in placeList) {
                if (!pl.id || force) {
                    pl.id = "p" + i + ((prefix == "") ? "" : "_" + prefix)
                }
                i++
            }
        }

        if (transitionList.size() > 0) {
            Integer i = 0
            for (tr in transitionList) {
                if (!tr.id || force) {
                    tr.id = "t" + i + ((prefix == "") ? "" : "_" + prefix)
                }
                i++
            }
        }

        if (subNets.size() > 0) {
            Integer i = 0
            for (subNet in net.subNets) {
                if (!alreadyConvertedNets.contains(subNet)) {
                    if (!subNet.function.id || force) {
                        subNet.function.id = "net" + ((prefix == "") ? i : "_" + prefix + i)
                    }
                    setIds(subNet, force, prefix + i.toString(), alreadyConvertedNets)
                    alreadyConvertedNets << subNet
                    i++
                }
            }
        }

        if (arcList.size() > 0) {
            Integer i = 0
            for (arc in arcList) {
                if (!arc.source.id || force) {
                    arc.source.id = "ext" + i + ((prefix == "") ? "" : "_" + prefix)
                }

                if (!arc.target.id || force) {
                    arc.target.id = "ext" + i + ((prefix == "") ? "" : "_" + prefix)
                }
            }
        }
    }

}
