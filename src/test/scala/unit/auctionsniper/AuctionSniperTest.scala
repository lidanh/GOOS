package unit.auctionsniper

import auctionsniper.SniperState._
import auctionsniper._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.specification.Scope

/**
 * Created by lidan on 16/01/15.
 */
object AuctionSniperTest {
  val ITEM_ID = "item-id"
}

class AuctionSniperTest extends Specification with Mockito {
  import unit.auctionsniper.AuctionSniperTest._

  trait mock extends Scope {
    lazy val sniperListener = mock[SniperListener]
    lazy val auction = mock[Auction]
    lazy val auctionSniper = new AuctionSniper(ITEM_ID, auction, sniperListener)
  }

  def withState(state: SniperState) = {
    be_==(state) ^^ { (snapshot: SniperSnapshot) => snapshot.state }
  }

  "Auction Sniper" should {
    "reports lost when auction closes immediately" in new mock {
      auctionSniper.auctionClosed()

      there was one(sniperListener).sniperStateChanged(withState(Lost))
    }

    "bids higher and reports bidding when new price arrives" in new mock {
      val price = 1001
      val increment = 25
      val bid = price + increment

      auctionSniper.currentPrice(price, increment, FromOtherBidder)

      got {
        one(auction).bid(bid)
        atLeastOne(sniperListener).sniperStateChanged(SniperSnapshot(ITEM_ID, price, bid, SniperState.Bidding))
      }
    }

    "reports winning when current price comes from sniper" in new mock {
      auctionSniper.currentPrice(123, 12, FromOtherBidder)
      auctionSniper.currentPrice(135, 45, FromSniper)

      there was atLeastOne(sniperListener).sniperStateChanged(withState(Bidding))
      there was atLeastOne(sniperListener).sniperStateChanged(SniperSnapshot(ITEM_ID, 135, 135, SniperState.Winning))
    }

    "reports lost if auction closes when bidding" in new mock {
      auctionSniper.currentPrice(123, 45, FromOtherBidder)
      auctionSniper.auctionClosed()

      there was one(sniperListener).sniperStateChanged(withState(Lost))
     }

    "report won if auction closes when winning" in new mock {
      auctionSniper.currentPrice(123, 45, FromSniper)
      auctionSniper.auctionClosed()

      there was one(sniperListener).sniperStateChanged(withState(Won))
    }
  }
}
