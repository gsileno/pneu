package org.leibnizcenter.pneu.examples

import org.leibnizcenter.pneu.components.basicpetrinet.BasicNet
import org.leibnizcenter.pneu.components.basicpetrinet.BasicTransition
import org.leibnizcenter.pneu.components.petrinet.Net
import org.leibnizcenter.pneu.components.petrinet.Place
import org.leibnizcenter.pneu.components.petrinet.Transition

class MarketModel {

    static Net basicSaleInstance1() {
        Net sale = new BasicNet()

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offer")
        Transition t2 = sale.createTransition("accept")
        Transition t3 = sale.createTransition("pay")
        Transition t4 = sale.createTransition("deliver")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, t4)
        sale.createBridge(t4, tOut)

        sale.resetIds()
        sale.exportToLog("basicSaleInstance1")

        sale
    }

    static Net basicSaleInstance2() {
        Net sale = new BasicNet()

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offer")
        Transition t2 = sale.createTransition("accept")
        Transition t3 = sale.createTransition("pay")
        Transition t4 = sale.createTransition("deliver")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, t3)
        sale.createBridge(t3, tOut)

        sale.resetIds()
        sale.exportToLog("basicSaleInstance2")
        sale
    }

    static Net basicSaleModel() {
        Net sale = new BasicNet()

        Transition tIn = sale.createEmitterTransition()
        Transition t1 = sale.createTransition("offer")
        Transition t2 = sale.createTransition("accept")
        Transition t3 = sale.createTransition("pay")
        Transition t4 = sale.createTransition("deliver")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1)
        sale.createBridge(t1, t2)
        sale.createBridge(t2, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2, t4)
        sale.createBridge(t4, tOut)

        sale.resetIds()
        sale.exportToLog("basicSale")
        sale
    }

    static Net basicSaleWith2Parties() {
        Net sale = new BasicNet()

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offer")
        Transition t1b = sale.propagateTransition(t1s)
        Transition t2b = sale.createTransition("accept")
        Transition t2s = sale.propagateTransition(t2b)
        Transition t3 = sale.createTransition("pay")
        Transition t4 = sale.createTransition("deliver")
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3)
        sale.createBridge(t3, tOut)
        sale.createBridge(t2s, t4)
        sale.createBridge(t4, tOut)

        sale.resetIds()
        sale.exportToLog("basicSale2Parties")
        sale
    }

    static Net basicSaleWithWorld() {
        Net sale = new BasicNet()

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offer")
        Transition t1 = sale.propagateTransition(t1s)
        Transition t1b = sale.propagateTransition(t1)
        Transition t2b = sale.createTransition("accept")
        Transition t2 = sale.propagateTransition(t2b)
        Transition t2s = sale.propagateTransition(t2)
        Transition t3b = sale.createTransition("pay")
        Transition t3 = sale.propagateTransition(t3b)
        Transition t4s = sale.createTransition("deliver")
        Transition t4 = sale.propagateTransition(t4s)
        Transition tOut = sale.createCollectorTransition()

        sale.createBridge(tIn, t1s)
        sale.createBridge(t1b, t2b)
        sale.createBridge(t1s, t2s)
        sale.createBridge(t2b, t3b)
        sale.createBridge(t2s, t4s)
        sale.createBridge(t3, tOut)
        sale.createBridge(t4, tOut)

        sale.resetIds()
        sale.exportToLog("basicSaleWithWorld")
        sale
    }

    static Net basicSaleWithWorldAndTimeline() {
        Net sale = new BasicNet()

        Transition tIn = sale.createEmitterTransition()
        Transition t1s = sale.createTransition("offer")
        Transition t1 = sale.propagateTransition(t1s)
        Transition t1b = sale.propagateTransition(t1)
        Transition t2b = sale.createTransition("accept")
        Transition t2 = sale.propagateTransition(t2b)
        Transition t2s = sale.propagateTransition(t2)
        Transition t3b = sale.createTransition("pay")
        Transition t3 = sale.propagateTransition(t3b)
        Transition t4s = sale.createTransition("deliver")
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

        sale.resetIds()
        sale.exportToLog("basicSaleWithWorldAndTimeline")
        sale
    }

    static Net basicSwapSale() {
        Net sale = new BasicNet()

        Transition tSellerIn = sale.createEmitterTransition()
        Transition tBuyerIn = sale.createEmitterTransition()
        Transition tSellerOut = sale.createCollectorTransition()
        Transition tBuyerOut = sale.createCollectorTransition()

        Transition t1s = sale.createTransition("offer")
        Transition t1b = sale.propagateTransition(t1s)
        Transition t2b = sale.createTransition("accept")
        Transition t2s = sale.propagateTransition(t2b)
        Transition t3 = sale.createTransition("pay")
        Transition t4 = sale.createTransition("deliver")

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

        swap.resetIds()
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

        String negPayEvent = "-pay"
        String negDeliverEvent = "-deliver"

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

    // we consider a very primitive propositional logic for labels: "-" for negation.
    static Net groundSaleScriptModel() {
        Net sale = new BasicNet()

        String commitmentBuyGood = "commits(buy)"
        String failureCommitmentBuyGood = "failure(commit(buy))"
        String successCommitmentBuyGood = "success(commit(buy))"

        String affordanceBuyGood = "affords(buy)"
        String initAffordanceBuyGood = "init(affords(buy))"

        String commitmentWaitForOffer = "waitForOffer"
        String failureCommitmentWaitForOffer = "failure(waitForDelivery)"
        String successCommitmentWaitForOffer = "success(waitForDelivery)"

        String commitmentWaitForDelivery = "waitForDelivery"
        String failureCommitmentWaitForDelivery = "failure(waitForDelivery)"
        String successCommitmentWaitForDelivery = "success(waitForDelivery)"

        String ownsMoney = "owns"

        String buyEvent = "buy"
        String negBuyEvent = "-buy"

        String offerEvent = "offer"
        String acceptEvent = "accept"
        String payEvent = "pay"
        String deliverEvent = "deliver"
        String enforceEvent = "enforce"

        String timeOutOffer = "timeout(offer)"
        String timeOutDelivery = "timeout(deliver)"

        Place pCommitmentBuyGood = sale.createPlace(commitmentBuyGood)
        Place pFailureCommitmentBuyGood = sale.createPlace(failureCommitmentBuyGood)
        Place pSuccessCommitmentBuyGood = sale.createPlace(successCommitmentBuyGood)

        Place pAffordanceBuyGood = sale.createPlace(affordanceBuyGood)
        Place pInitAffordanceBuyGood = sale.createPlace(initAffordanceBuyGood)

        Place pCommitmentWaitForOffer = sale.createPlace(commitmentWaitForOffer)
        Place pFailureCommitmentWaitForOffer = sale.createPlace(failureCommitmentWaitForOffer)
        Place pSuccessCommitmentWaitForOffer = sale.createPlace(successCommitmentWaitForOffer)

        Place pCommitmentWaitForDelivery = sale.createPlace(commitmentWaitForDelivery)
        Place pFailureCommitmentWaitForDelivery = sale.createPlace(failureCommitmentWaitForDelivery)
        Place pSuccessCommitmentWaitForDelivery = sale.createPlace(successCommitmentWaitForDelivery)

        Place pOwnsMoney = sale.createPlace(ownsMoney)

        Place pConstraintTimeoutOffer = sale.createLinkPlace()
        Place pConstraintTimeoutDelivery = sale.createLinkPlace()

        Transition tSuccessCommitmentBuyGood = sale.createLinkTransition()
        Transition tFailureCommitmentBuyGood = sale.createLinkTransition()
        Transition tInitCommitmentWaitForOffer = sale.createLinkTransition()
        Transition tSuccessCommitmentWaitForOffer = sale.createLinkTransition()
        Transition tFailureCommitmentWaitForOffer = sale.createLinkTransition()
        Transition tInitCommitmentWaitForDelivery = sale.createLinkTransition()
        Transition tSuccessCommitmentWaitForDelivery = sale.createLinkTransition()
        Transition tFailureCommitmentWaitForDelivery = sale.createLinkTransition()
        Transition tInitAffordanceBuyGood = sale.createLinkTransition()

        Transition tBuyEvent = sale.createTransition(buyEvent)
        Transition tNegBuyEvent = sale.createTransition(negBuyEvent)

        Transition tOfferEvent = sale.createTransition(offerEvent)
        Transition tAcceptEvent = sale.createTransition(acceptEvent)
        Transition tPayEvent = sale.createTransition(payEvent)
        Transition tDeliverEvent = sale.createTransition(deliverEvent)
        Transition tEnforceEvent = sale.createTransition(enforceEvent)

        Transition tTimeOutOffer = sale.createTransition(timeOutOffer)
        Transition tTimeOutDelivery = sale.createTransition(timeOutDelivery)

        Transition tMainIn = sale.createEmitterTransition()
        Transition tCatalystIn = sale.createEmitterTransition()

        sale.createPlaceNexus(pCommitmentBuyGood, [tMainIn], [tFailureCommitmentBuyGood, tSuccessCommitmentBuyGood], [tBuyEvent, tNegBuyEvent], [], [])
        sale.createBridge(tNegBuyEvent, pFailureCommitmentBuyGood, tFailureCommitmentBuyGood)
        sale.createBridge(tBuyEvent, pSuccessCommitmentBuyGood, tSuccessCommitmentBuyGood)

        sale.createPlaceNexus(pCommitmentWaitForOffer, [tInitCommitmentWaitForOffer], [tFailureCommitmentWaitForOffer, tSuccessCommitmentWaitForOffer], [tTimeOutOffer, tOfferEvent], [], [])
        sale.createBridge(tTimeOutOffer, pFailureCommitmentWaitForOffer, tFailureCommitmentWaitForOffer)
        sale.createBridge(tOfferEvent, pSuccessCommitmentWaitForOffer, tSuccessCommitmentWaitForOffer)
        sale.createArc(pConstraintTimeoutOffer, tTimeOutOffer)
        sale.createArc(pConstraintTimeoutOffer, tOfferEvent)

        sale.createPlaceNexus(pCommitmentWaitForDelivery, [tInitCommitmentWaitForDelivery], [tFailureCommitmentWaitForDelivery, tSuccessCommitmentWaitForDelivery], [tTimeOutDelivery, tDeliverEvent], [], [])
        sale.createBridge(tNegBuyEvent, pFailureCommitmentWaitForDelivery, tFailureCommitmentWaitForDelivery)
        sale.createBridge(tBuyEvent, pSuccessCommitmentWaitForDelivery, tSuccessCommitmentWaitForDelivery)
        sale.createArc(pConstraintTimeoutDelivery, tTimeOutDelivery)
        sale.createArc(pConstraintTimeoutDelivery, tDeliverEvent)

        sale.createArc(tCatalystIn, pOwnsMoney)
        Transition tCatalyst = sale.createLinkTransition()
        sale.createBiflowArc(pOwnsMoney, tCatalyst)
        sale.createPersistentBridge(tCatalyst, pInitAffordanceBuyGood, tInitAffordanceBuyGood)
        sale.createDiodeArc(tInitAffordanceBuyGood, pAffordanceBuyGood)

        Transition tStartAction = sale.createLinkTransition()
        Place pStartAction = sale.createLinkPlace()

        sale.createDiodeArc(tStartAction, pStartAction)
        sale.createBiflowArc(pStartAction, tInitCommitmentWaitForOffer)

        sale.createBiflowArc(tStartAction, pCommitmentBuyGood)
        sale.createBiflowArc(tStartAction, pAffordanceBuyGood)
        sale.createBridge(tStartAction, tInitCommitmentWaitForOffer)
        sale.createBridge(tSuccessCommitmentWaitForOffer, tAcceptEvent)
        sale.createBridge(tFailureCommitmentWaitForDelivery, tEnforceEvent)

        Place pFailureSink = sale.createLinkPlace()
        sale.createArc(tFailureCommitmentWaitForOffer, pFailureSink)
        sale.createArc(tFailureCommitmentWaitForDelivery, pFailureSink)
        sale.createArc(pFailureSink, tNegBuyEvent)

        Place pBridge1 = sale.createBridge(tAcceptEvent, tPayEvent)
        Place pBridge2 = sale.createBridge(tPayEvent, tBuyEvent)

        sale.createResetArc(pStartAction, tFailureCommitmentBuyGood)
        sale.createResetArc(pBridge1, tFailureCommitmentBuyGood)
        sale.createResetArc(pBridge2, tFailureCommitmentBuyGood)

        sale.createBridge(tAcceptEvent, tInitCommitmentWaitForDelivery)
        sale.createBridge(tSuccessCommitmentWaitForDelivery, tBuyEvent)

        sale.createArc(tInitCommitmentWaitForOffer, pConstraintTimeoutOffer)
        sale.createArc(tInitCommitmentWaitForDelivery, pConstraintTimeoutDelivery)

        sale.resetIds()
        sale
    }

}
