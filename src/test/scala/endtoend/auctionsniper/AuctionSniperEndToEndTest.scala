package endtoend.auctionsniper

/**
 * Created by lidan on 14/01/15.
 */

import org.specs2.mutable._
import org.specs2.specification.Scope

class AuctionSniperEndToEndTest extends Specification {
  sequential

  "AuctionSniper" should {
    "join auction until auction closes" in new after {
      // create auction chat in XMPP server
      auction.startSellingItem()
      // application start bidding
      application.startBiddingIn(auction)
      // check if the auction chat has received join request from the sniper application
      auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID)
      // close auction
      auction.announceClosed()
      // make sure that the application got LOST message from the auction
      application.showsSniperHasLostAuction()
    }

    "makes a higher bid but loses" in new after {
      auction.startSellingItem()
      application.startBiddingIn(auction)
      auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID)

      auction.reportPrice(1000, 98, "other bidder")
      application.hasShownSniperIsBidding()
      auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

      auction.announceClosed()
      application.showsSniperHasLostAuction()
    }

    "wins an auction by bidding higher" in new after {
      auction.startSellingItem()
      application.startBiddingIn(auction)
      auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID)

      auction.reportPrice(1000, 98, "other bidder")
      application.hasShownSniperIsBidding()

      auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID)

      auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID)
      application.hasShownSniperIsWinning()

      auction.announceClosed()
      application.showsSniperHasWonAuction()
    }
  }
}

trait after extends Scope with After {
  val auction = new FakeAuctionServer("item-54321")
  val application = new ApplicationRunner()

  override def after: Any = {
    auction.stop()
    application.stop()
  }
}
