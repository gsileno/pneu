package org.leibnizcenter.pneu.builders

import groovy.xml.StreamingMarkupBuilder
import org.leibnizcenter.pneu.components.graphics.Point
import org.leibnizcenter.pneu.components.petrinet.*

// TODO: hierarchical Petri Nets

class PN2PNML {

    static Writable buildPNML(Net petriNet) {

        Integer currentPlaceId = 0
        Integer currentTransitionId = 0
        Integer currentArcId = 0

        def builder = new StreamingMarkupBuilder()
        builder.encoding = 'UTF-8'

        petriNet.setupGrid()

        def xml = builder.bind {
            mkp.xmlDeclaration()
            pnml {
                net ( type: "http://www.yasper.org/specs/epnml-1.1", id: "di0" ) {
                    for (Transition t in petriNet.transitionList) {
                        t.id = "tr"+currentTransitionId
                        transition (id: t.id) {
                            if (t.name) name {
                                text t.name
                            }
                            graphics {
                                Point point = t.position
                                point = petriNet.grid.inverseScalePoint(point)
                                position (x: point.x, y: point.y)
                                dimension (x: 32, y: 32)
                            }
                        }
                        currentTransitionId++
                    }
                    for (Place p in petriNet.placeList) {
                        p.id = "pl"+currentPlaceId
                        place (id: p.id) {
                            if (p.name) name {
                                text p.name
                            }
                            graphics {
                                Point point = p.position
                                point = petriNet.grid.inverseScalePoint(point)
                                position (x: point.x, y: point.y)
                                dimension (x: 20, y: 20)
                            }
                        }
                        currentPlaceId++
                    }
                    for (Arc a in petriNet.arcList) {
                        a.id = "a"+currentArcId

                        if (a.type == ArcType.NORMAL) {
                            arc (id: a.id, source: a.source.id, target: a.target.id )
                        } else if (a.type == ArcType.INHIBITOR) {
                            arc (id: a.id, source: a.target.id, target: a.source.id ) {
                                type {
                                    text "inhibitor"
                                }
                            }
                        }
                        currentArcId++
                    }
                }
            }
        }

        xml

    }
}


/* Example of PNML file

<?xml version="1.0" encoding="utf-8"?>
<pnml>
  <net type="http://www.yasper.org/specs/epnml-1.1" id="do1">
    <toolspecific tool="Yasper" version="1.2.4020.34351">
      <roles xmlns="http://www.yasper.org/specs/epnml-1.1/toolspec" />
    </toolspecific>
    <transition id="tr1">
      <graphics>
        <position x="165" y="132" />
        <dimension x="32" y="32" />
      </graphics>
      <toolspecific tool="Yasper" version="1.2.4020.34351">
        <emitor xmlns="http://www.yasper.org/specs/epnml-1.1/toolspec">
          <text>true</text>
        </emitor>
        <processingTime xmlns="http://www.yasper.org/specs/epnml-1.1/toolspec">
          <mean>
            <text>1</text>
          </mean>
          <deviation>
            <text>0</text>
          </deviation>
        </processingTime>
      </toolspecific>
    </transition>
    <place id="pl4">
      <graphics>
        <position x="231" y="132" />
        <dimension x="20" y="20" />
      </graphics>
      <toolspecific tool="Yasper" version="1.2.4020.34351">
        <tokenCaseSensitive xmlns="http://www.yasper.org/specs/epnml-1.1/toolspec">
          <text>true</text>
        </tokenCaseSensitive>
      </toolspecific>
    </place>
    <transition id="tr11">
      <graphics>
        <position x="297" y="132" />
        <dimension x="32" y="32" />
      </graphics>
      <toolspecific tool="Yasper" version="1.2.4020.34351">
        <collector xmlns="http://www.yasper.org/specs/epnml-1.1/toolspec">
          <text>true</text>
        </collector>
      </toolspecific>
    </transition>
    <arc id="a1" source="tr1" target="pl4" />
    <arc id="a2" source="pl4" target="tr11" />
  </net>
</pnml> */

