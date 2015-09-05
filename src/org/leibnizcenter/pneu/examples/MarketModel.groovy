package org.leibnizcenter.pneu.examples

import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.basicpetrinet.BasicTransition
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class MarketModel {

    static Net basicSaleInstance1() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offers")
        Transition t2 = sale.createTransition("accepts")
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSaleInstance1")
        sale
    }

    static Net basicSaleInstance2() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offers")
        Transition t2 = sale.createTransition("accepts")
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, t3)
        sale.createBridge(t3, tOut)

        sale.exportToLog("basicSaleInstance2")
        sale
    }

    static Net basicSaleModel() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offers")
        Transition t2 = sale.createTransition("accepts")
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSale")
        sale
    }

    static Net basicSaleWith2Parties() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offers")
        Transition t1b = sale.propagateTransition(t1s)
        Transition t2b = sale.createTransition("accepts")
        Transition t2s = sale.propagateTransition(t2b)
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2s, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSale2Parties")
        sale
    }

    static Net basicSaleWithWorld() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offers")
        Transition t1 = sale.propagateTransition(t1s)
        Transition t1b = sale.propagateTransition(t1)
        Transition t2b = sale.createTransition("accepts")
        Transition t2 = sale.propagateTransition(t2b)
        Transition t2s = sale.propagateTransition(t2)
        Transition t3b = sale.createTransition("pays")
        Transition t3 = sale.propagateTransition(t3b)
        Transition t4s = sale.createTransition("delivers")
        Transition t4 = sale.propagateTransition(t4s)
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3b)
        sale.createBridge(t2s, t4s)
        sale.createBridge(t3, tOut)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSaleWithWorld")
        sale
    }

    static Net basicSaleWithWorldAndTimeline() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offers")
        Transition t1 = sale.propagateTransition(t1s)
        Transition t1b = sale.propagateTransition(t1)
        Transition t2b = sale.createTransition("accepts")
        Transition t2 = sale.propagateTransition(t2b)
        Transition t2s = sale.propagateTransition(t2)
        Transition t3b = sale.createTransition("pays")
        Transition t3 = sale.propagateTransition(t3b)
        Transition t4s = sale.createTransition("delivers")
        Transition t4 = sale.propagateTransition(t4s)
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3b)
        sale.createBridge(t2s, t4s)

        // this is the message layer: it gives the timeline
        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, tOut)

        sale.exportToLog("basicSaleWithWorldAndTimeline")
        sale
    }

    static Net basicSwapSale() {
        Net sale = new BasicNet()

        sale.function = BasicTransition.build("sells")

        Transition tSellerIn = sale.createEmitterTransition()
        Transition tBuyerIn = sale.createEmitterTransition()
        Transition tSellerOut = sale.createCollectorTransition()
        Transition tBuyerOut = sale.createCollectorTransition()

        Transition t1s = sale.createTransition("offers")
        Transition t1b = sale.propagateTransition(t1s)
        Transition t2b = sale.createTransition("accepts")
        Transition t2s = sale.propagateTransition(t2b)
        Transition t3 = sale.createTransition("pays")
        Transition t4 = sale.createTransition("delivers")

        sale.createBridge(tSellerIn, t1s)
        sale.createBridge(tBuyerIn, t1b)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3)
        sale.createBridge(t3, tBuyerOut)
        sale.createBridge(t2s, t4)
        sale.createBridge(t4, tSellerOut)

        Net swap = new BasicNet()

        Net sale1 = sale.minimalClone()
        Net sale2 = sale.minimalClone()

        swap.include(sale1)
        swap.include(sale2)

        swap.createBridge((Transition) sale1.inputs[0], (Transition) sale2.inputs[1])
        swap.createBridge((Transition) sale1.inputs[1], (Transition) sale2.inputs[2])
        swap.createBridge((Transition) sale1.outputs[0], (Transition) sale2.outputs[1])
        swap.createBridge((Transition) sale1.outputs[1], (Transition) sale2.outputs[2])

        swap
    }

    static Net groundSaleNormativeModel() {
        Net sale = new BasicNet()

        Transition tIn = sale.createEmitterTransition()
        Transition tOut = sale.createCollectorTransition()

        String sellerPowerOffer = "power(offer)"
        String buyerPowerAccept = "power(accept)"
        String sellerDutyDeliver = "duty(deliver)"
        String buyerDutyPay = "duty(pay)"
        String buyerLiabilityPay = "liable(enforce(pay))"
        String sellerLiabilityDeliver = "liable(enforce(deliver))"

        String initBuyerPowerAccept = "init(power(offer))"
        String initBuyerDutyPay = "init(duty(pay))"
        String failureBuyerDutyPay = "failure(duty(pay))"
        String successBuyerDutyPay = "success(duty(pay))"
        String initSellerDutyDeliver = "init(duty(deliver))"
        String failureSellerDutyDeliver = "failure(duty(deliver))"
        String successSellerDutyDeliver = "success(duty(deliver))"

        String offerEvent = "offer"
        String acceptEvent = "accept"
        String payEvent = "pay"
        String deliverEvent = "deliver"

        String negPayEvent = "neg(pay)"
        String negDeliverEvent = "neg(deliver)"

        Place pSellerPowerOffer = sale.createPlace(sellerPowerOffer)
        Place pBuyerPowerAccept = sale.createPlace(buyerPowerAccept)
        Place pSellerDutyDeliver = sale.createPlace(sellerDutyDeliver)
        Place pBuyerDutyPay = sale.createPlace(buyerDutyPay)
        Place pInitBuyerPowerAccept = sale.createPlace(initBuyerPowerAccept)
        Place pInitBuyerDutyPay = sale.createPlace(initBuyerDutyPay)
        Place pSuccessBuyerDutyPay = sale.createPlace(successBuyerDutyPay)
        Place pFailureBuyerDutyPay = sale.createPlace(failureBuyerDutyPay)
        Place pLiabilityBuyerPay = sale.createPlace(buyerLiabilityPay)
        Place pInitSellerDutyDeliver = sale.createPlace(initSellerDutyDeliver)
        Place pSuccessSellerDutyDeliver = sale.createPlace(successSellerDutyDeliver)
        Place pFailureSellerDutyDeliver = sale.createPlace(failureSellerDutyDeliver)
        Place pLiabilitySellerDeliver = sale.createPlace(sellerLiabilityDeliver)

        Transition tInitBuyerDutyPay = sale.createLinkTransition()
        Transition tSuccessBuyerDutyPay = sale.createLinkTransition()
        Transition tFailureBuyerDutyPay = sale.createLinkTransition()
        Transition tInitSellerDutyDeliver = sale.createLinkTransition()
        Transition tSuccessSellerDutyDeliver = sale.createLinkTransition()
        Transition tFailureSellerDutyDeliver = sale.createLinkTransition()

        Transition tOfferEvent = sale.createTransition(offerEvent)
        Transition tAcceptEvent = sale.createTransition(acceptEvent)
        Transition tPayEvent = sale.createTransition(payEvent)
        Transition tDeliverEvent = sale.createTransition(deliverEvent)

        Transition tNegPayEvent = sale.createTransition(negPayEvent)
        Transition tNegDeliverEvent = sale.createTransition(negDeliverEvent)

        sale.createArc(tIn, pSellerPowerOffer)
        sale.createBridge(pSellerPowerOffer, tOfferEvent, pInitBuyerPowerAccept)
        sale.createBridge(pInitBuyerPowerAccept, pBuyerPowerAccept)
        sale.createArc(pBuyerPowerAccept, tAcceptEvent)

        Place constraintOnPayEvent = sale.createLinkPlace()
        Place constraintOnDeliverEvent = sale.createLinkPlace()

        sale.createArcs(tAcceptEvent, [pInitBuyerDutyPay, pInitSellerDutyDeliver, constraintOnPayEvent, constraintOnDeliverEvent])
        sale.createArc(pInitBuyerDutyPay, tInitBuyerDutyPay)
        sale.createArc(pInitSellerDutyDeliver, tInitSellerDutyDeliver)

        sale.createArcs(constraintOnPayEvent, [tPayEvent, tNegPayEvent])
        sale.createArcs(constraintOnDeliverEvent, [tDeliverEvent, tNegDeliverEvent])

        sale.createPlaceNexus(pBuyerDutyPay, [tInitBuyerDutyPay], [tSuccessBuyerDutyPay], [tPayEvent, tNegPayEvent, tFailureBuyerDutyPay], [], [])
        sale.createPlaceNexus(pSellerDutyDeliver, [tInitSellerDutyDeliver], [tSuccessSellerDutyDeliver], [tDeliverEvent, tNegDeliverEvent, tFailureSellerDutyDeliver], [], [])

        sale.createPersistentBridge(tNegPayEvent, pFailureBuyerDutyPay, tFailureBuyerDutyPay)
        sale.createDiodeBridge(tPayEvent, pSuccessBuyerDutyPay, tSuccessBuyerDutyPay)
        sale.createArc(tFailureBuyerDutyPay, pLiabilityBuyerPay)

        sale.createPersistentBridge(tNegDeliverEvent, pFailureSellerDutyDeliver, tFailureSellerDutyDeliver)
        sale.createDiodeBridge(tDeliverEvent, pSuccessSellerDutyDeliver, tSuccessSellerDutyDeliver)
        sale.createArc(tFailureSellerDutyDeliver, pLiabilitySellerDeliver)

        sale.createBridge(tSuccessBuyerDutyPay, tOut)
        sale.createBridge(tSuccessSellerDutyDeliver, tOut)

        sale.resetIds()
        sale
    }

}
