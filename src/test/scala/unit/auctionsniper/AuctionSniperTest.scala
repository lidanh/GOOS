package unit.auctionsniper

import auctionsniper.{Auction, AuctionSniper, SniperListener}
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.specification.Scope

/**
 * Created by lidan on 16/01/15.
 */
class AuctionSniperTest extends Specification with Mockito {
  trait mock extends Scope {
    val sniperListener = mock[SniperListener]
    val auction = mock[Auction]
    val auctionSniper = new AuctionSniper(auction, sniperListener)
  }

  "Auction Sniper" should {
    "reports lost when auction closes" in new mock {
      auctionSniper.auctionClosed()

      there was one(sniperListener).sniperLost()
    }

    "bids higher and reports bidding when new price arrives" in new mock {
      val price = 1001
      val increment = 25

      auctionSniper.currentPrice(price, increment)

      got {
        one(auction).bid(price + increment)
        atLeastOne(sniperListener).sniperBidding()
      }
    }
  }
}
