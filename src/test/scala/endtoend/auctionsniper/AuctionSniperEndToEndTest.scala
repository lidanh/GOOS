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
      auction.hasReceivedJoinRequestFromSniper
      // close auction
      auction.announceClosed()
      // make sure that the application got LOST message from the auction
      application.showsSniperHasLostAuction()
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
