package unit.auctionsniper

import auctionsniper._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.specification.Scope

/**
 * Created by lidan on 16/01/15.
 */
class AuctionSniperTest extends Specification with Mockito {
  trait mock extends Scope {
    lazy val sniperListener = mock[SniperListener]
    lazy val auction = mock[Auction]
    lazy val auctionSniper = new AuctionSniper(auction, sniperListener)
  }

  "Auction Sniper" should {
    "reports lost when auction closes immediately" in new mock {
      auctionSniper.auctionClosed()

      there was one(sniperListener).sniperLost()
    }

    "bids higher and reports bidding when new price arrives" in new mock {
      val price = 1001
      val increment = 25

      auctionSniper.currentPrice(price, increment, FromOtherBidder)

      got {
        one(auction).bid(price + increment)
        atLeastOne(sniperListener).sniperBidding()
      }
    }

    "reports winning when current price comes from sniper" in new mock {
      auctionSniper.currentPrice(123, 45, FromSniper)

      there was atLeastOne(sniperListener).sniperWinning()
    }

    "reports lost if auction closes when bidding" in new mock {
      /* Not implemented- need to use State Jmock feature, which is not supported by Mockito :(
      *
      * Haven't found an elegant solution that does not make a dependency between the code and the tests.
      * */

      auctionSniper.currentPrice(123, 45, FromOtherBidder)
      auctionSniper.auctionClosed()

      there was one(sniperListener).sniperLost()
     }

    "report won if auction closes when winning" in new mock {
      auctionSniper.currentPrice(123, 45, FromSniper)
      auctionSniper.auctionClosed()

      there was one(sniperListener).sniperWon()
    }
  }
}
