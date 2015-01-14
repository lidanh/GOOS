/**
 * Created by lidan on 14/01/15.
 */

import org.specs2.mutable._

class AuctionSniperE2ETest extends Specification with After {
  isolated

  val auction = new FakeAuctionServer("item-54321")
  val application = new ApplicationRunner()

  override def after: Any = {
    auction.stop()
    application.stop()
  }

  "Sniper joins auction until auction closes" should {
    "test" in {
      auction.startSellingItem()
      application.startBiddingIn(auction)
      auction.hasReceivedJoinRequestFromSniper()
      auction.annouceClosed()
      application.showsSniperHasLostAuction()
    }
  }
}
